package model;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class GestorDeFlota{
    private List<UnidadDeTransporte> unidades;
    private final File flota= new File("src/main/java/resource/data/unidades.json");
    private final ObjectMapper mapper= new ObjectMapper();
    
    public GestorDeFlota(){
        this.unidades= new ArrayList<>();
        cargarDesdeFlota();
    }

    //Recuperar autobuses guardados
    private void cargarDesdeFlota(){
        if(flota.exists()){
            try{
                unidades= mapper.readValue(flota, new TypeReference<List<UnidadDeTransporte>>(){});
            }catch(IOException e){
                System.err.println("Error al cargar la flota desde el archivo: " + e.getMessage());
                unidades= new ArrayList<>();
            }
        }
    }

    //Guardar las unidades en el archivo JSON
    private boolean guardarEnFlota(){
        File carpetaPadre = flota.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }
        
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(flota, unidades);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar la unidad en el archivo: " + e.getMessage());
            return false;
        }
    }


    //Registro de unidades
    public boolean RegistrarUnidad(String placa, String modelo, int capacidad){
        if(buscarPorPlaca(placa)!=null){
            System.out.println("ERROR: Ya existe una unidad registrada con la placa: " + placa);
            return false;
        }
        String numeroAsignado= "U-" + (unidades.size()+1); //asignarle numero de manera automatica
        UnidadDeTransporte nuevaUnidad= new UnidadDeTransporte(numeroAsignado, placa, modelo, capacidad);
        unidades.add(nuevaUnidad);
        guardarEnFlota();
        return true;
    }

    //Buscar unidad
    public UnidadDeTransporte buscarPorPlaca(String placa){
        for(UnidadDeTransporte unidadActual: unidades){
            if(unidadActual.getPlaca().equalsIgnoreCase(placa)){
                return unidadActual;
            }
        }
        return null;
    }

    //Cambiar estado de la unidad
    public boolean cambiarEstado(String placa, EstadoOperativo estado){
        UnidadDeTransporte unidad= buscarPorPlaca(placa);
        if(unidad!=null){
            unidad.setEstadoOperativo(estado);
            guardarEnFlota();
            return true;
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
            return false;
        }

    }

     //Editar unidad
    public boolean EditarUnidad(String PlacaAbuscar, String nuevaPlaca, String modelo, int capacidad){
        UnidadDeTransporte unidad= buscarPorPlaca(PlacaAbuscar);
        if(unidad!=null){
            UnidadDeTransporte unidadNuevaPlaca= buscarPorPlaca(nuevaPlaca);
            if(unidadNuevaPlaca!=null && unidadNuevaPlaca!=unidad){
                System.out.println("ERROR: La nueva placa ya pertenece a otro vehículo.");
                return false;
            }
            unidad.setPlaca(nuevaPlaca);
            unidad.setModelo(modelo);
            unidad.setCapacidad_de_pasajeros(capacidad);
            guardarEnFlota();
            return true;
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
            return false;
        }
    }

    //Eliminar unidad
    public boolean EliminarUnidad(String Placa){
        UnidadDeTransporte unidad= buscarPorPlaca(Placa);
        if(unidad!=null){
            unidades.remove(unidad);
            guardarEnFlota();
            System.out.println("Unidad eliminada con éxito.");
            return true;
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
            return false;
        }
    }

    public List<UnidadDeTransporte> getUnidades(){
        return unidades;
    }
}