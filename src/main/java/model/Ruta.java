package model;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private String nombreRuta;      // Ej: "Plaza Venezuela - UCV"
    private String origen;          // Ej: "Plaza Venezuela"
    private String destino;         // Ej: "Rectorado UCV"
    private TipoRuta tipoRuta;      // URBANA o EXTRAURBANA
    private List<String> paradas;   // Lista de nombres de las paradas

    // Enum interno o externo para el tipo de ruta
    public enum TipoRuta {
        URBANA,
        EXTRAURBANA
    }

    // Constructor vacío (necesario para que Jackson/ObjectMapper pueda deserializar el JSON)
    public Ruta() {
        this.paradas = new ArrayList<>();
    }

    // Constructor completo
    public Ruta( String nombreRuta, String origen, String destino, TipoRuta tipoRuta) {
        
        this.nombreRuta = nombreRuta;
        this.origen = origen;
        this.destino = destino;
        this.tipoRuta = tipoRuta;
        this.paradas = new ArrayList<>();
    }

    // Método de conveniencia para añadir paradas fácilmente
    public void agregarParada(String parada) {
        if (parada != null && !parada.trim().isEmpty()) {
            this.paradas.add(parada);
        }
    }



    public String getNombreRuta() {
        return nombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        this.nombreRuta = nombreRuta;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public TipoRuta getTipoRuta() {
        return tipoRuta;
    }

    public void setTipoRuta(TipoRuta tipoRuta) {
        this.tipoRuta = tipoRuta;
    }

    public List<String> getParadas() {
        return paradas;
    }

    public void setParadas(List<String> paradas) {
        this.paradas = paradas;
    }
}