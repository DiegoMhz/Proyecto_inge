package model;

public class UnidadDeTransporte {
   private String numeroAsignado;
   private String placa;
   private String modelo;
   private int capacidad_de_pasajeros;
   private EstadoOperativo estado;

   public UnidadDeTransporte() {
   }

   public UnidadDeTransporte(String _numeroAsignado, String _placa, String _modelo, int _capacidad_de_pasajeros) {
      this.numeroAsignado = _numeroAsignado;
      this.placa = _placa;
      this.modelo = _modelo;
      this.capacidad_de_pasajeros = _capacidad_de_pasajeros;
      this.estado = EstadoOperativo.activo;
   }

   public String getNumeroAsignado() {
      return numeroAsignado;
   }

   public void setNumeroAsignado(String _numeroAsignado) {
      this.numeroAsignado = _numeroAsignado;
   }

   public String getPlaca() {
      return placa;
   }

   public void setPlaca(String _placa) {
      this.placa = _placa;
   }

   public String getModelo() {
      return modelo;
   }

   public void setModelo(String _modelo) {
      this.modelo = _modelo;
   }

   public int getCapacidad_de_pasajeros() {
      return capacidad_de_pasajeros;
   }

   public void setCapacidad_de_pasajeros(int _capacidad) {
      this.capacidad_de_pasajeros = _capacidad;
   }

   public EstadoOperativo getEstadoOperativo() {
      return estado;
   }

   public void setEstadoOperativo(EstadoOperativo _estado) {
      this.estado = _estado;
   }

   public int getCapacidad() {
      return capacidad_de_pasajeros;
   }

   public void setEstado(EstadoOperativo _estado) {
      this.estado = _estado;
   }

   public void setCapacidadDePasajeros(int _capacidad) {
      this.capacidad_de_pasajeros = _capacidad;
   }
}