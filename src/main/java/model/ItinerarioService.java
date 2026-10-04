package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



public class ItinerarioService {

    private static final String RUTA_ARCHIVO = "src/main/java/resources/data/itinerarios.json";
    private static final Pattern PATRON_HORA = Pattern.compile("(?:[01]\\d|2[0-3]):[0-5]\\d");
    private final File archivo;
    private final ObjectMapper mapper;

    public ItinerarioService() {
        this(new File(RUTA_ARCHIVO));
    }

    public ItinerarioService(File archivo) {
        this.archivo = archivo;
        this.mapper = new ObjectMapper();
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
                return Optional.of("La unidad o placa ya está asignada a un itinerario a esa hora.");
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