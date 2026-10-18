package model.services;

import model.entities.Ruta;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RutaService {

    private final String RUTA_ARCHIVO = "src/main/resources/data/rutas.json";
    private final ObjectMapper mapper = new ObjectMapper();

    // Lee el archivo JSON
    public List<Ruta> obtenerTodasLasRutas() {
        File file = new File(RUTA_ARCHIVO);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(file, new TypeReference<List<Ruta>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Retorna todas las rutas listas para el JComboBox
    public ComboBoxModel<Ruta> obtenerModeloRutas() {
        List<Ruta> rutas = obtenerTodasLasRutas();
        return new DefaultComboBoxModel<>(rutas.toArray(new Ruta[0]));
    }

    public ComboBoxModel<Ruta> obtenerModeloRutasPorTipo(Ruta.TipoRuta tipo) {
        List<Ruta> rutasFiltradas = obtenerTodasLasRutas().stream()
                .filter(r -> r.getTipoRuta() == tipo) // Asegúrate de que el getter en Ruta coincida
                .toList();
        return new DefaultComboBoxModel<>(rutasFiltradas.toArray(new Ruta[0]));
    }

}