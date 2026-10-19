package model.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import model.entities.Municipio;

public class TarifaService {

    private static final String RUTA_MUNICIPIOS = "src/main/resources/data/municipios.json";
    private static final String RUTA_DESCUENTOS = "src/main/resources/data/descuentos.json";
    private static final String CLAVE_DESCUENTO_ESTUDIANTE = "DescuentoEstudiante";
    private static final String CLAVE_DESCUENTO_EMPLEADO = "DescuentoEmpleado";

    private final File archivoMunicipios;
    private final File archivoDescuentos;
    private final ObjectMapper mapper;
    private float DescuentoEstudiante;
    private float DescuentoEmpleado;

    public TarifaService() {
        this(new File(RUTA_MUNICIPIOS), new File(RUTA_DESCUENTOS));
    }

    public TarifaService(File archivoMunicipios, File archivoDescuentos) {
        this.archivoMunicipios = Objects.requireNonNull(archivoMunicipios);
        this.archivoDescuentos = Objects.requireNonNull(archivoDescuentos);
        this.mapper = new ObjectMapper();
        cargarDescuentos();
    }

    public List<Municipio> obtenerMunicipios() {
        if (!archivoMunicipios.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(archivoMunicipios, new TypeReference<List<Municipio>>() {});
        } catch (IOException e) {
            System.err.println("Error al leer los municipios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardarMunicipios(List<Municipio> municipios) {
        if (municipios == null || municipios.stream().anyMatch(this::municipioInvalido)) {
            System.err.println("No se pueden guardar municipios nulos o con datos inválidos.");
            return false;
        }

        try {
            crearDirectorioPadre(archivoMunicipios);
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoMunicipios, municipios);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar los municipios: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarTarifaMunicipio(String nombreMunicipio, float nuevaTarifa) {
        if (nombreMunicipio == null || nombreMunicipio.isBlank()
                || !Float.isFinite(nuevaTarifa) || nuevaTarifa < 0) {
            System.err.println("El nombre del municipio y la tarifa deben ser válidos.");
            return false;
        }

        List<Municipio> municipios = obtenerMunicipios();
        for (Municipio municipio : municipios) {
            if (municipio != null && municipio.getMunicipioNombre() != null
                    && municipio.getMunicipioNombre().equalsIgnoreCase(nombreMunicipio.trim())) {
                municipio.setMunicipioTarifa(nuevaTarifa);
                return guardarMunicipios(municipios);
            }
        }

        return false;
    }

    public boolean cargarDescuentos() {
        if (!archivoDescuentos.exists()) {
            System.err.println("No existe el archivo de descuentos: " + archivoDescuentos);
            return false;
        }

        try {
            Map<String, Float> descuentos = mapper.readValue(
                    archivoDescuentos, new TypeReference<Map<String, Float>>() {});
            Float descuentoEstudiante = descuentos.get(CLAVE_DESCUENTO_ESTUDIANTE);
            Float descuentoEmpleado = descuentos.get(CLAVE_DESCUENTO_EMPLEADO);
            if (!porcentajeValido(descuentoEstudiante) || !porcentajeValido(descuentoEmpleado)) {
                System.err.println("El archivo de descuentos debe contener porcentajes válidos para estudiante y empleado.");
                return false;
            }

            DescuentoEstudiante = descuentoEstudiante;
            DescuentoEmpleado = descuentoEmpleado;
            return true;
        } catch (IOException e) {
            System.err.println("Error al leer los descuentos: " + e.getMessage());
            return false;
        }
    }

    public boolean guardarDescuentos(float descuentoEstudiante, float descuentoEmpleado) {
        if (!porcentajeValido(descuentoEstudiante) || !porcentajeValido(descuentoEmpleado)) {
            System.err.println("Los porcentajes de descuento deben ser números finitos no negativos.");
            return false;
        }

        Map<String, Float> descuentos = new LinkedHashMap<>();
        descuentos.put(CLAVE_DESCUENTO_ESTUDIANTE, descuentoEstudiante);
        descuentos.put(CLAVE_DESCUENTO_EMPLEADO, descuentoEmpleado);

        try {
            crearDirectorioPadre(archivoDescuentos);
            mapper.writerWithDefaultPrettyPrinter().writeValue(archivoDescuentos, descuentos);
            DescuentoEstudiante = descuentoEstudiante;
            DescuentoEmpleado = descuentoEmpleado;
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar los descuentos: " + e.getMessage());
            return false;
        }
    }

    public float getDescuentoEstudiante() {
        return DescuentoEstudiante;
    }

    public float getDescuentoEmpleado() {
        return DescuentoEmpleado;
    }

    private boolean municipioInvalido(Municipio municipio) {
        return municipio == null || municipio.getMunicipioNombre() == null
                || municipio.getMunicipioNombre().isBlank()
                || !Float.isFinite(municipio.getMunicipioTarifa())
                || municipio.getMunicipioTarifa() < 0;
    }

    private boolean porcentajeValido(Float porcentaje) {
        return porcentaje != null && Float.isFinite(porcentaje) && porcentaje >= 0;
    }

    private void crearDirectorioPadre(File archivo) throws IOException {
        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()
                && !directorioPadre.mkdirs() && !directorioPadre.exists()) {
            throw new IOException("No se pudo crear el directorio " + directorioPadre);
        }
    }
}
