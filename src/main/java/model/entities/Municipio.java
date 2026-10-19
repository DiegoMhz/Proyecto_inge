package model.entities;

public class Municipio {
    private String municipioNombre;
    private float municipioTarifa;

    public Municipio() {
    }

    public Municipio(String municipioNombre, float municipioTarifa) {
        this.municipioNombre = municipioNombre;
        this.municipioTarifa = municipioTarifa;
    }

    public String getMunicipioNombre() {
        return municipioNombre;
    }

    public void setMunicipioNombre(String municipioNombre) {
        this.municipioNombre = municipioNombre;
    }

    public float getMunicipioTarifa() {
        return municipioTarifa;
    }

    public void setMunicipioTarifa(float municipioTarifa) {
        this.municipioTarifa = municipioTarifa;
    }

    @Override
    public String toString() {
        return municipioNombre;
    }
}
