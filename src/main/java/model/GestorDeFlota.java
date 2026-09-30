import java.util.ArrayList;
import java.util.List;

public class GestorDeFlota{
    private List<UnidadDeTransporte> unidades;
    public GestorDeFlota(){
        this.unidades= new ArrayList<>();
    }

    //Registro de unidades
    public void RegistrarUnidad(String placa, String modelo, int capacidad){
        String numeroAsignado= "U-" + (unidades.size()+1); //asignarle numero de manera automatica
        UnidadDeTransporte nuevaUnidad= new UnidadDeTransporte(numeroAsignado, placa, modelo, capacidad);
        unidades.add(nuevaUnidad);
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

    //Cambiar esatdo de la unidad
    public void cambiarEstado(String placa, EstadoOperativo estado){
        UnidadDeTransporte unidad= buscarPorPlaca(placa);
        if(unidad!=null){
            unidad.setEstado(estado);
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
        }

    }

     //Editar unidad
    public void EditarUnidad(String PlacaAbuscar, String nuevaPlaca, String modelo, int capacidad){
        UnidadDeTransporte unidad= buscarPorPlaca(PlacaAbuscar);
        if(PlacaAbuscar!=null){
            unidad.setPlaca(nuevaPlaca);
            unidad.setModelo(modelo);
            unidad.setCapacidadDePasajeros(capacidad);
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
        }
    }

    //Eliminar unidad
    public void EliminarUnidad(String Placa){
        UnidadDeTransporte unidad= buscarPorPlaca(Placa);
        if(unidad!=null){
            unidades.remove(unidad);
            System.out.println("Unidad eliminada con éxito.");
        }else{
            System.out.println("ERROR: No se encontró ningún vehículo con esa placa.");
        }
    }
}