package model;

public class ControlDeItinerario {
    private String id;
    private Ruta ruta;
    private UnidadDeTransporte unidad;
    private Usuario conductor;
    private String fechaHoraSalida;
    private String fechaHoraLlegadaEstimada;

    public ControlDeItinerario() {
    }

    public ControlDeItinerario(String id, Ruta ruta, UnidadDeTransporte unidad, Usuario conductor,
            String fechaHoraSalida, String fechaHoraLlegadaEstimada) {
        this.id = id;
        this.ruta = ruta;
        this.unidad = unidad;
        this.conductor = conductor;
        this.fechaHoraSalida = fechaHoraSalida;
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

    public String getId() { return id; }
    public Ruta getRuta() { return ruta; }
    public UnidadDeTransporte getUnidad() { return unidad; }
    public Usuario getConductor() { return conductor; }
    public String getFechaSalida() { return fechaHoraSalida; }
    public String getFechaLlegada() { return fechaHoraLlegadaEstimada; }

    public void setId(String id) { this.id = id; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
    public void setUnidad(UnidadDeTransporte unidad) { this.unidad = unidad; }
    public void setConductor(Usuario conductor) { this.conductor = conductor; }
    public void setFechaSalida(String fechaHoraSalida) { this.fechaHoraSalida = fechaHoraSalida; }
    public void setFechaLlegada(String fechaHoraLlegadaEstimada) {
        this.fechaHoraLlegadaEstimada = fechaHoraLlegadaEstimada;
    }

        
}
