package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AutenticacionService {
    private static final String RUTA_JSON = "src/main/resources/data/usuarios.json";
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

    public Usuario BuscarPorCedula(String cedula){
        List<Usuario> lista= obtenerUsuarios();
        for(Usuario u: lista){
            if(u.getCedula() != null && u.getCedula().equalsIgnoreCase(cedula)){
                return u;
            }
        }
        return null;
    }

    // Registrar un nuevo usuario en el JSON
    public boolean registrarUsuario(RolUsuario rol, String name, String lastName, String cedula, String correo, String password) {
        List<Usuario> lista = obtenerUsuarios();

        // Validar si el usuario ya se encuentra registrado
        if(BuscarPorCedula(cedula)!=null){
            return false;
        }

        // Agregar el nuevo usuario y guardar la lista actualizada en el archivo
        lista.add(new Usuario(rol,name, lastName, cedula, correo, password));

        File archivo = new File(RUTA_JSON);
        File carpetaPadre = archivo.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, lista);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}