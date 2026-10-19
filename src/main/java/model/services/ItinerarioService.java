package model.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.entities.ControlDeItinerario;
import model.entities.Usuario;



public class ItinerarioService {

    private static final String RUTA_ARCHIVO = "src/main/resources/data/itinerarios.json";
    private static final Pattern PATRON_HORA = Pattern.compile("(?:[01]\\d|2[0-3]):[0-5]\\d");
    private final File archivo;
    private final ObjectMapper mapper;
    private final TarifaService tarifaService;

    public ItinerarioService() {
        this(new File(RUTA_ARCHIVO), new TarifaService());
    }

    public ItinerarioService(TarifaService tarifaService) {
        this(new File(RUTA_ARCHIVO), tarifaService);
    }

    public ItinerarioService(File archivo) {
        this(archivo, new TarifaService());
    }

    public ItinerarioService(File archivo, TarifaService tarifaService) {
        this.archivo = archivo;
        this.mapper = new ObjectMapper();
        this.tarifaService = tarifaService;
    }

    public String obtenerSiguienteId() {
        return generarSiguienteId(obtenerTodos());
    }

    private String generarSiguienteId(List<ControlDeItinerario> itinerarios) {
        int maximo = 0;
        for (ControlDeItinerario itinerario : itinerarios) {
            try {
                maximo = Math.max(maximo, Integer.parseInt(itinerario.getId()));
            } catch (NumberFormatException | NullPointerException ignorado) {
                // Ignora IDs antiguos que no sean numéricos.
            }
        }
        return String.format(Locale.ROOT, "%02d", maximo + 1);
    }

    /*
      Carga todos los itinerarios desde el archivo JSON.
     */
    public List<ControlDeItinerario> obtenerTodos() {
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try {
            List<ControlDeItinerario> itinerarios = mapper.readValue(archivo,
                    new TypeReference<List<ControlDeItinerario>>() {});
           
            return itinerarios;
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de itinerarios: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<ControlDeItinerario> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return obtenerTodos().stream()
                .filter(itinerario -> id.equals(itinerario.getId()))
                .findFirst();
    }

    public boolean eliminarItinerario(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }

        List<ControlDeItinerario> itinerarios = obtenerTodos();
        boolean eliminado = itinerarios.removeIf(itinerario -> itinerario != null
                && id.equals(itinerario.getId()));
        return eliminado && guardarTodos(itinerarios);
    }

    public boolean reservarAsiento(String idItinerario) {
        List<ControlDeItinerario> itinerarios = obtenerTodos();
        for (ControlDeItinerario itinerario : itinerarios) {
            if (idItinerario.equals(itinerario.getId())) {
                UnidadDeTransporte unidad = itinerario.getUnidad();
                if (unidad == null || unidad.getAsientosDisponibles() <= 0) {
                    return false;
                }
                unidad.setAsientosDisponibles(unidad.getAsientosDisponibles() - 1);
                return guardarTodos(itinerarios);
            }
        }
        return false;
    }

    public boolean liberarAsiento(String idItinerario) {
        List<ControlDeItinerario> itinerarios = obtenerTodos();
        for (ControlDeItinerario itinerario : itinerarios) {
            if (idItinerario.equals(itinerario.getId())) {
                UnidadDeTransporte unidad = itinerario.getUnidad();
                if (unidad == null || unidad.getAsientosDisponibles() >= unidad.getCapacidad()) {
                    return false;
                }
                unidad.setAsientosDisponibles(unidad.getAsientosDisponibles() + 1);
                return guardarTodos(itinerarios);
            }
        }
        return false;
    }

    /*
      Guarda la lista completa de itinerarios en el archivo JSON.
     */
    public boolean guardarTodos(List<ControlDeItinerario> itinerarios) {
        try {
            // Crear los directorios si no existen
            if (archivo.getParentFile() != null && !archivo.getParentFile().exists()) {
                archivo.getParentFile().mkdirs();
            }

            // Escribir el JSON formateado con sangrías
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, itinerarios);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo de itinerarios: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Optional<String> validarConflictoHorario(ControlDeItinerario nuevoItinerario) {
        return encontrarConflicto(obtenerTodos(), nuevoItinerario);
    }

    private Optional<String> encontrarConflicto(List<ControlDeItinerario> itinerarios,
            ControlDeItinerario nuevoItinerario) {
        if (nuevoItinerario.getRuta() == null
                || nuevoItinerario.getRuta().getMunicipios() == null
                || nuevoItinerario.getRuta().getMunicipios().size() != 1
                || nuevoItinerario.getRuta().getMunicipios().get(0) == null
                || nuevoItinerario.getRuta().getMunicipios().get(0).isBlank()) {
            return Optional.of("Seleccione el municipio al que pertenece la ruta.");
        }
        String municipioSeleccionado = nuevoItinerario.getRuta().getMunicipios().get(0).trim();
        boolean municipioExiste = tarifaService.obtenerMunicipios().stream()
                .anyMatch(municipio -> municipio != null
                        && municipio.getMunicipioNombre() != null
                        && municipio.getMunicipioNombre().equalsIgnoreCase(
                                municipioSeleccionado));
        if (!municipioExiste) {
            return Optional.of("El municipio seleccionado ya no existe en la base de datos de municipios.");
        }

        Integer salidaNueva = convertirHoraAEntero(nuevoItinerario.getFechaSalida());
        Integer llegadaNueva = convertirHoraAEntero(nuevoItinerario.getFechaLlegada());
        if (salidaNueva == null || llegadaNueva == null) {
            return Optional.of("Ingrese las horas con el formato HH:mm, entre 00:00 y 23:59.");
        }
        if (salidaNueva >= llegadaNueva) {
            return Optional.of("La llegada estimada debe ser posterior a la salida.");
        }

        for (ControlDeItinerario existente : itinerarios) {
            Integer salidaExistente = convertirHoraAEntero(existente.getFechaSalida());
            Integer llegadaExistente = convertirHoraAEntero(existente.getFechaLlegada());
            if (salidaExistente == null || llegadaExistente == null
                    || salidaNueva >= llegadaExistente || llegadaNueva <= salidaExistente) {
                continue;
            }

            boolean mismoConductor = mismoConductor(existente.getConductor(), nuevoItinerario.getConductor());
            boolean mismaUnidad = mismaUnidad(existente.getUnidad(), nuevoItinerario.getUnidad());
            if (mismoConductor && mismaUnidad) {
                return Optional.of("Ya existe un itinerario a esa hora con el mismo conductor y la misma unidad.");
            }
            if (mismoConductor) {
                return Optional.of("El conductor ya tiene un itinerario a esa hora.");
            }
            if (mismaUnidad) {
                return Optional.of("La Unidad de Transporte ya está asignada a un itinerario a esa hora.");
            }
        }

        return Optional.empty();
    }


    private Integer convertirHoraAEntero(String hora) {

        if (hora == null || !PATRON_HORA.matcher(hora).matches()) {
            return null;
        }
        return Integer.parseInt(hora.replace(":", ""));
    }

    private boolean mismoConductor(Usuario primero, Usuario segundo) {
        if (primero == null || segundo == null) {
            return false;
        }
        return mismoValor(primero.getCedula(), segundo.getCedula())
                || mismoValor(primero.getCorreo(), segundo.getCorreo());
    }

    private boolean mismaUnidad(UnidadDeTransporte primera, UnidadDeTransporte segunda) {
        if (primera == null || segunda == null) {
            return false;
        }
        return mismoValor(primera.getPlaca(), segunda.getPlaca())
                || mismoValor(primera.getNumeroAsignado(), segunda.getNumeroAsignado());
    }

    private boolean mismoValor(String primero, String segundo) {
        return primero != null && segundo != null && !primero.isBlank() && !segundo.isBlank()
                && primero.trim().equalsIgnoreCase(segundo.trim());
    }

    /**
     * Registra un nuevo itinerario agregándolo a los existentes y guardando los cambios.
     */
    public boolean agregarItinerario(ControlDeItinerario nuevoItinerario) {
        List<ControlDeItinerario> lista = obtenerTodos();

        if (encontrarConflicto(lista, nuevoItinerario).isPresent()) {
            return false;
        }

        nuevoItinerario.setId(generarSiguienteId(lista));
        lista.add(nuevoItinerario);
        return guardarTodos(lista);
    }
}