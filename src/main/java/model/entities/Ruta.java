package model.entities;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private String nombreRuta; // Ej: "Plaza Venezuela - UCV"
    private String origen; // Ej: "Plaza Venezuela"
    private String destino; // Ej: "Rectorado UCV"
    private TipoRuta tipoDeRuta; // URBANA o EXTRAURBANA
    private List<String> paradas; // Lista de nombres de las paradas
    private List<String> municipios;

    // Enum interno o externo para el tipo de ruta
    public enum TipoRuta {
        URBANA,
        EXTRAURBANA
    }

    // Constructor vacío (necesario para que Jackson/ObjectMapper pueda deserializar
    // el JSON)
    public Ruta() {
        this.paradas = new ArrayList<>();
        this.municipios = new ArrayList<>();
    }

    // Constructor completo
    public Ruta(String _nombreRuta, String _origen, String _destino, TipoRuta _tipoRuta, List<String> municipios) {
        this.nombreRuta = _nombreRuta;
        this.origen = _origen;
        this.destino = _destino;
        this.tipoDeRuta = _tipoRuta;
        if (municipios != null) {
            this.municipios = municipios;
        } else {
            this.municipios = new ArrayList<>();
        }
    }

    // Método de conveniencia para añadir paradas fácilmente
    public void agregarParada(String parada) {
        if (parada != null && !parada.trim().isEmpty()) {
            this.paradas.add(parada);
        }
    }

    public List<String> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<String> municipios) {
        this.municipios = municipios;
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
        return tipoDeRuta;
    }

    public void setTipoRuta(TipoRuta tipoRuta) {
        this.tipoDeRuta = tipoRuta;
    }

    public List<String> getParadas() {
        return paradas;
    }

    public void setParadas(List<String> paradas) {
        this.paradas = paradas;
    }

    @Override
    public String toString() {
        return nombreRuta + " (" + origen + " -> " + destino + ")";
    }
}