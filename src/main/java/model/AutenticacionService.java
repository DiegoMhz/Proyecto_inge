package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutenticacionService {
    private static final String RUTA_JSON = "src/main/java/resource/data/usuarios.json";
    private final ObjectMapper mapper = new ObjectMapper();

    // Cargar usuarios existentes desde el archivo JSON
    public List<Usuario> obtenerUsuarios() {
        File file = new File(RUTA_JSON);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(file, new TypeReference<List<Usuario>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Registrar un nuevo usuario en el JSON
    public boolean registrarUsuario(String name, String lastName, String cedula, String password) {
        List<Usuario> lista = obtenerUsuarios();

        // Validar si el nombre de usuario ya existe
        for (Usuario u : lista) {
            if (u.getname().equalsIgnoreCase(name)) {
                return false; // Usuario duplicado
            }
        }

        // Agregar el nuevo usuario y guardar la lista actualizada en el archivo
        lista.add(new Usuario(name, password));
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(RUTA_JSON), lista);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}