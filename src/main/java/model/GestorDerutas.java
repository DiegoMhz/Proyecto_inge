package model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorDerutas{
    private List<Ruta> rutas;
    private final File archivoRutas = new File("src/main/java/resource/data/rutas.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public GestorDerutas(){
        this.rutas= new ArrayList<>();
        cargarDesdeRutas();
    }

    private void cargarDesdeRutas(){
        if(archivoRutas.exists()){
            try {
                rutas = mapper.readValue(archivoRutas, new TypeReference<List<Ruta>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar las rutas: " + e.getMessage());
                rutas = new ArrayList<>();
            }
        }
    } 

    //agregar ruta a la lista
    private boolean guardarEnRutas() {
        File carpetaPadre = archivoRutas.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoRutas, rutas);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar las rutas: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarRuta(String _nombreRuta, String _origen, String _destino, Ruta.TipoRuta _tipoDeRuta, List<String> _municipios){
        if(buscarPorNombre(_nombreRuta)!=null){
            System.out.println("ERROR: Ya existe una ruta con ese nombre.");
            return false;
        }

        Ruta nueva= new Ruta(_nombreRuta, _origen, _destino, _tipoDeRuta, _municipios);
        rutas.add(nueva);
        guardarEnRutas();
        return true;
    }

    //Buscar por nombre
    public Ruta buscarPorNombre(String nombreRuta){
        for(Ruta r: rutas){
            if(r.getNombreRuta().equalsIgnoreCase(nombreRuta)){
                return r;
            }
        }

        return null;
    }

    //editar ruta
    public boolean editarRuta(String _nombreRuta, String _origen, String _destino, Ruta.TipoRuta _tipoDeRuta, List<String> _municipios){
        Ruta ruta= buscarPorNombre((_nombreRuta));
        if(ruta!=null){
            Ruta otraRuta= buscarPorNombre(_nombreRuta);
            if(otraRuta!=null && otraRuta!=ruta){
                System.out.println("ERROR: El nuevo nombre ya pertenece a otra ruta.");
                return false;
            }
            ruta.setNombreRuta(_nombreRuta);
            ruta.setOrigen(_origen);
            ruta.setDestino(_destino);
            ruta.setMunicipios(_municipios);
            guardarEnRutas();
            return true;
        }else{
            System.out.println("ERROR: No se encontró la ruta.");
            return false;
        }
    }

    //eliminar ruta
    public boolean eliminarRuta(String _nombreRuta){
        Ruta ruta= buscarPorNombre(_nombreRuta);
        if(ruta!=null){
            rutas.remove(ruta);
            guardarEnRutas();
            return true;
        }else{
            System.out.println("ERROR: No se encontró la ruta.");
            return false;
        }

    }

    public List<Ruta> getRutas() {
        return rutas;
    }

}