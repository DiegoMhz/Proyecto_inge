package model;
import java.util.ArrayList;
import java.util.List;

public class Ruta{
    private String nombreRuta;
    private String origen;
    private String destino;
    private TipoRuta tipoDeRuta;
    private List<String> municipios;

    enum TipoRuta{
        URBANA,
        EXTRAURBANA
    }

    public Ruta(){
        this.municipios= new ArrayList<>();
    }

    public Ruta(String _nombreRuta, String _origen, String _destino, TipoRuta _tipoRuta, List<String> municipios){
        this.nombreRuta= _nombreRuta;
        this.origen= _origen;
        this.destino= _destino;
        this.tipoDeRuta= _tipoRuta;
        if(municipios!=null){
            this.municipios= municipios;
        }else{
            this.municipios= new ArrayList<>();
        }
    }

    public String getNombreRuta(){
        return nombreRuta;
    }
    void setNombreRuta(String _nombreRuta){
        this.nombreRuta= _nombreRuta;
    }

    public String getOrigen(){
        return origen;
    }
    void setOrigen(String _origen){
        this.origen= _origen;
    }

    public String getDestino(){
        return destino;
    }
    void setDestino(String _destino){
        this.destino= _destino;
    }

    public TipoRuta geTipoRuta(){
        return tipoDeRuta;
    }
    void setTipoRuta(TipoRuta _tipoRuta){
        this.tipoDeRuta= _tipoRuta;
    }

    public List<String> getMunicipios() { return municipios; }
    public void setMunicipios(List<String> municipios) { this.municipios = municipios; }

}