package model;


public class ControlDeItinerario {
    private String id;
    private Ruta ruta;
    private UnidadDeTransporte unidad;
    private Conductor conductor;
    private LocalDateTime fechaHoraSalida;
    private LocalDateTime fechaHoraLlegadaEstimada;

    public ControlDeItinerario(String _id, Ruta _ruta, UnidadDeTransporte _unidad, Conductor _conductor, LocalDateTime _fechahorasalida, LocalDateTime _fechahorallegadaestim){
    this.id = _id;
    this.Ruta = _ruta;
    this.UnidadDeTransporte = _unidad;
    this.conductor = _conductor;
    this.fechaHoraSalida = _fechahorasalida;
    this.fechaHoraLlegadaEstimada = _fechahorallegadaestim;
   } 

   public String getId () {return id;}
   public Ruta getRuta () {return ruta;}
   public UnidadDeTransporte getUnidad () {return unidad;}
   public Conductor getConductor () {return conductor;}
   public LocalDateTime getFechaSalida () {return fechaHoraSalida;}
   public LocalDateTime getFechaLlegada () {return fechaHoraLlegadaEstimada;}

   public void setId (String id) {this.id = id; }
   public void setRuta (Ruta ruta) {this.ruta = ruta; }
   public void setUnidad (UnidadDeTransporte unidad) {this.unidad = unidad; }
   public void setConductor (Conductor conductor) {this.conductor = conductor; }
   public void setFechaSalida (LocalDateTime fechaHoraSalida) {this.fechaHoraSalida= fechaHoraSalida; }
   public void setFechaLlegada (LocalDateTime fechaHoraLlegadaEstimada) {this.fechaHoraLlegadaEstimada= fechaHoraLlegadaEstimada; }
dadasd


/*      if (itinerario.getFechaHoraSalida().isBefore(LocalDateTime.now())) {
            System.out.println("No se puede modificar un itinerario que ya inició o transcurrió.");
        }

        Verificar solapamiento entre dos rangos de horas
        if (salidaNuevos.isBefore(llegadaExistente) && llegadaNueva.isAfter(salidaExistente)) {
            System.out.println("Error: Choque de horarios.");
        }
            
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Permite serializar/deserializar LocalDateTime
        // 
        */

        
}
