package model;

public class Monedero {
    private String cedulaUsuario;
    private double saldo;

    // Constructor vacío obligatorio para JSON
    public Monedero() {
    }

    // Constructor con parámetros
    public Monedero(String cedulaUsuario, double saldoInicial) {
        this.cedulaUsuario = cedulaUsuario;
        if (saldoInicial >= 0) {
            this.saldo = saldoInicial;
        } else {
            this.saldo = 0;
        }
    }

    // Recargar dinero al monedero validando el código de confirmación de Pago Móvil
    public boolean recargarConPagoMovil(double monto, String codigoConfirmacion) {
        if (codigoConfirmacion != null && !codigoConfirmacion.trim().isEmpty() && monto > 0) {
            this.saldo += monto;
            return true;
        }
        System.out.println("ERROR: Código de confirmación inválido o monto incorrecto.");
        return false;
    }

    // Descontar dinero (cuando compre el ticket)
    public boolean descontar(double monto) {
        if (monto > 0 && this.saldo >= monto) {
            this.saldo -= monto;
            return true;
        }
        return false;
    }

    public String getCedulaUsuario() {
        return cedulaUsuario;
    }

    public void setCedulaUsuario(String cedulaUsuario) {
        this.cedulaUsuario = cedulaUsuario;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

}