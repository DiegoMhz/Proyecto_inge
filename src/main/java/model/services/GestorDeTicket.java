package model.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.entities.Ruta;
import model.entities.Ticket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GestorDeTicket {
    private List<Ticket> tickets;
    private final File archivoTickets;
    private final ObjectMapper mapper = new ObjectMapper();
    private final GestorMonedero gestorMonederos;

    public GestorDeTicket(GestorMonedero gestorMonederos) {
        this(gestorMonederos, new File("src/main/resources/data/tickets.json"));
    }

    public GestorDeTicket(GestorMonedero gestorMonederos, File archivoTickets) {
        this.tickets = new ArrayList<>();
        this.gestorMonederos = gestorMonederos;
        this.archivoTickets = archivoTickets;
        cargarDesdeTickets();
    }

    private void cargarDesdeTickets() {
        if (archivoTickets.exists()) {
            try {
                tickets = mapper.readValue(archivoTickets, new TypeReference<List<Ticket>>() {
                });
            } catch (IOException e) {
                System.err.println("Error al cargar tickets: " + e.getMessage());
                tickets = new ArrayList<>();
            }
        }
    }

    private boolean guardarEnTickets(List<Ticket> ticketsAGuardar) {
        File carpetaPadre = archivoTickets.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            if (!carpetaPadre.mkdirs() && !carpetaPadre.exists()) {
                System.err.println("No se pudo crear el directorio para los tickets.");
                return false;
            }
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoTickets, ticketsAGuardar);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar tickets: " + e.getMessage());
            return false;
        }
    }

    // Simulación de la compra de ticket con cobro en monedero
    public boolean procesarCompraTicket(String cedulaUsuario, Ruta ruta, String rolUsuario, double tarifaACobrar) {
        if (ruta == null || cedulaUsuario == null || !Double.isFinite(tarifaACobrar)
                || tarifaACobrar <= 0 || gestorMonederos.buscarPorCedula(cedulaUsuario) == null) {
            System.err.println("No se puede comprar el ticket con los datos indicados.");
            return false;
        }

        String idTicket = "TICK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ticket nuevoTicket = new Ticket(
                idTicket,
                cedulaUsuario,
                ruta.getNombreRuta(),
                ruta.getTipoRuta().toString(),
                rolUsuario,
                tarifaACobrar);

        List<Ticket> ticketsAnteriores = new ArrayList<>(tickets);
        List<Ticket> ticketsActualizados = new ArrayList<>(ticketsAnteriores);
        ticketsActualizados.add(nuevoTicket);
        if (!guardarEnTickets(ticketsActualizados)) {
            return false;
        }

        if (!gestorMonederos.descontarMonedero(cedulaUsuario, tarifaACobrar)) {
            if (!guardarEnTickets(ticketsAnteriores)) {
                System.err.println("No se pudo revertir el ticket luego de fallar el débito.");
            }
            return false;
        }

        tickets = ticketsActualizados;
        System.out.println("ÉXITO: Ticket generado con ID " + idTicket + " por un monto de " + tarifaACobrar + " BS.");
        return true;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}