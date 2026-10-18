package model.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestorMonedero{
    private List<Monedero> monederos;
    private final File archivoMonederos = new File("src/main/resources/data/monederos.json");
    private final ObjectMapper mapper = new ObjectMapper();

    public GestorMonedero() {
        this.monederos = new ArrayList<>();
        cargarDesdeMonederos();
    }

    // Cargar monederos desde el archivo JSON
    private void cargarDesdeMonederos() {
        if (archivoMonederos.exists()) {
            try {
                monederos = mapper.readValue(archivoMonederos, new TypeReference<List<Monedero>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar los monederos: " + e.getMessage());
                monederos = new ArrayList<>();
            }
        }
    }

    // Guardar lista de monederos en el JSON
    private boolean guardarEnMonederos() {
        File carpetaPadre = archivoMonederos.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoMonederos, monederos);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar los monederos: " + e.getMessage());
            return false;
        }
    }

    // Buscar el monedero de un usuario por su cédula
    public Monedero buscarPorCedula(String cedulaUsuario) {
        for (Monedero m : monederos) {
            if (m.getCedulaUsuario().equalsIgnoreCase(cedulaUsuario)) {
                return m;
            }
        }
        return null;
    }

    // Crear o registrar un monedero para un usuario
    public boolean crearMonedero(String cedulaUsuario, double saldoInicial) {
        if (buscarPorCedula(cedulaUsuario) != null) {
            System.out.println("ERROR: El usuario ya posee un monedero registrado.");
            return false;
        }
        Monedero nuevoMonedero = new Monedero(cedulaUsuario, saldoInicial);
        monederos.add(nuevoMonedero);
        guardarEnMonederos();
        return true;
    }

    // Recargar saldo mediante Pago Móvil
    public boolean recargarMonedero(String cedulaUsuario, double monto, String codigoConfirmacion) {
        Monedero m = buscarPorCedula(cedulaUsuario);
        if (m != null) {
            boolean exito = m.recargarConPagoMovil(monto, codigoConfirmacion);
            if (exito) {
                guardarEnMonederos();
                return true;
            }
        } else {
            System.out.println("ERROR: No se encontró el monedero para la cédula indicada.");
        }
        return false;
    }

    // Descontar saldo para compra de ticket/pasaje
    public boolean descontarMonedero(String cedulaUsuario, double monto) {
        Monedero m = buscarPorCedula(cedulaUsuario);
        if (m != null) {
            boolean exito = m.descontar(monto);
            if (exito) {
                guardarEnMonederos();
                return true;
            } else {
                System.out.println("ERROR: Saldo insuficiente o monto inválido.");
            }
        } else {
            System.out.println("ERROR: No se encontró el monedero para la cédula indicada.");
        }
        return false;
    }

    public List<Monedero> getMonederos() {
        return monederos;
    }
}