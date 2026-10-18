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
    private final File archivoTickets = new File("src/main/java/resource/data/tickets.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private GestorMonedero gestorMonederos;

    public GestorDeTicket(GestorMonedero gestorMonederos) {
        this.tickets = new ArrayList<>();
        this.gestorMonederos = gestorMonederos;
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

    private boolean guardarEnTickets() {
        File carpetaPadre = archivoTickets.getParentFile();
        if (carpetaPadre != null && !carpetaPadre.exists()) {
            carpetaPadre.mkdirs();
        }
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoTickets, tickets);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar tickets: " + e.getMessage());
            return false;
        }
    }

    // Simulación de la compra de ticket con cobro en monedero
    public boolean procesarCompraTicket(String cedulaUsuario, Ruta ruta, String rolUsuario, double tarifaACobrar) {
        // 1. Intentar hacer el débito en el monedero del usuario
        boolean debitoExitoso = gestorMonederos.descontarMonedero(cedulaUsuario, tarifaACobrar);

        if (!debitoExitoso) {
            System.out.println("ERROR: No se pudo procesar la compra. Saldo insuficiente o usuario no registrado.");
            return false;
        }

        // 2. Si el débito fue exitoso, generar y guardar el ticket
        String idTicket = "TICK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Ticket nuevoTicket = new Ticket(
                idTicket,
                cedulaUsuario,
                ruta.getNombreRuta(),
                ruta.getTipoRuta().toString(),
                rolUsuario,
                tarifaACobrar);

        tickets.add(nuevoTicket);
        guardarEnTickets();

        System.out.println("ÉXITO: Ticket generado con ID " + idTicket + " por un monto de " + tarifaACobrar + " BS.");
        return true;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}