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
    private void guardarEnFlota(){
        File carpetaPadre = flota.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }
        
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(flota, unidades);
        } catch (IOException e) {
            System.err.println("Error al guardar la unidad en el archivo: " + e.getMessage());
        }
    }


    //Registro de unidades
    public void RegistrarUnidad(String placa, String modelo, int capacidad){
        String numeroAsignado= "U-" + (unidades.size()+1); //asignarle numero de manera automatica
        UnidadDeTransporte nuevaUnidad= new UnidadDeTransporte(numeroAsignado, placa, modelo, capacidad);
        unidades.add(nuevaUnidad);
        guardarEnFlota();
    }

    //Buscar unidad
    public UnidadDeTransporte buscarPorPlaca(String placa){
        for(UnidadDeTransporte unidadActual: unidades){
            if(unidadActual.getPlaca().equals(placa)){
                return unidadActual;
            }

        }
        return null;
    }

    //Cambiar estado de la unidad
    public void cambiarEstado(String placa, EstadoOperativo estado){
        UnidadDeTransporte unidad= buscarPorPlaca(placa);
        if(unidad!=null){
            unidad.setEstadoOperativo(estado);
            guardarEnFlota();
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
        }

    }

     //Editar unidad
    public void EditarUnidad(String PlacaAbuscar, String nuevaPlaca, String modelo, int capacidad){
        UnidadDeTransporte unidad= buscarPorPlaca(PlacaAbuscar);
        if(unidad!=null){
            unidad.setPlaca(nuevaPlaca);
            unidad.setModelo(modelo);
            unidad.setCapacidad_de_pasajeros(capacidad);
            guardarEnFlota();
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
        }
    }

    //Eliminar unidad
    public void EliminarUnidad(String Placa){
        UnidadDeTransporte unidad= buscarPorPlaca(Placa);
        if(unidad!=null){
            unidades.remove(unidad);
            guardarEnFlota();
            System.out.println("Unidad eliminada con éxito.");
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
        }
    }

    public List<UnidadDeTransporte> getUnidades(){
        return unidades;
    }
}