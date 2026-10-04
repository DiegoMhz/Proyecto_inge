package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



public class ItinerarioService {

    private static final String RUTA_ARCHIVO = "src/main/resources/data/itinerarios.json";
    private final ObjectMapper mapper;

    public ItinerarioService() {
        // Inicialización limpia: sin JavaTimeModule ni configuraciones extra de fecha
        this.mapper = new ObjectMapper();
    }

    /**
     * Carga todos los itinerarios desde el archivo JSON.
     */
    public List<ControlDeItinerario> obtenerTodos() {
        File archivo = new File(RUTA_ARCHIVO);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(archivo, new TypeReference<List<ControlDeItinerario>>() {});
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de itinerarios: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Guarda la lista completa de itinerarios en el archivo JSON.
     */
    public boolean guardarTodos(List<ControlDeItinerario> itinerarios) {
        try {
            File archivo = new File(RUTA_ARCHIVO);

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

    /**
     * Registra un nuevo itinerario agregándolo a los existentes y guardando los cambios.
     */
    public boolean agregarItinerario(ControlDeItinerario nuevoItinerario) {
        List<ControlDeItinerario> lista = obtenerTodos();

        // Validar si ya existe un itinerario con el mismo ID
        for (ControlDeItinerario it : lista) {
            if (it.getId() != null && it.getId().equalsIgnoreCase(nuevoItinerario.getId())) {
                System.out.println("Error: Ya existe un itinerario con el ID " + nuevoItinerario.getId());
                return false;
            }
        }

        lista.add(nuevoItinerario);
        return guardarTodos(lista);
    }
}