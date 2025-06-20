package org.candidatos.servicios;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.candidatos.modelos.*;
import org.candidatos.repositorios.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 *  Servicio para gestionar la logica relacionada con los candidatos
 */
@Service
public class CandidatoService {

    @Autowired
    private CandidatoRepository candidatoRepository;

    public void guardarCandidatosEnArchivoJson() throws IOException {
        List<Candidato> candidatos = candidatoRepository.findAll();

        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("candidatos.json");

        mapper.writerWithDefaultPrettyPrinter().writeValue(archivo, candidatos);
    }


    /**
     *  Da de alta un nuevo candidato después de validar los datos.
     */
     public String altaCandidato(CandidatoDto candidatoDto) {

            if (candidatoDto.getNombreCompleto() == null || candidatoDto.getNombreCompleto().isEmpty()) {
                throw new RuntimeException("El nombre no puede ser nulo o vacío");
            }
            if (candidatoDto.getMail() == null || candidatoDto.getMail().isEmpty()) {
                throw new RuntimeException("El correo electrónico no puede ser nulo o vacío");
            }
            if (candidatoDto.getInstitucionEducativa() == null || candidatoDto.getInstitucionEducativa().isEmpty()) {
                throw new RuntimeException("La institución educativa no puede ser nula o vacía");
            }
            if (candidatoDto.getCarrera() == null || candidatoDto.getCarrera().isEmpty()) {
                throw new RuntimeException("La carrera no puede ser nula o vacía");
            }
            if (candidatoDto.getPromedioAcademico() == null) {
                throw new RuntimeException("El promedio académico no puede ser nulo");
            }

            Candidato candidato = new Candidato();
            candidato.setId(candidatoDto.getId());
            candidato.setMail(candidatoDto.getMail());
            candidato.setNombreCompleto(candidatoDto.getNombreCompleto());
            candidato.setInstitucionEducativa(candidatoDto.getInstitucionEducativa());
            candidato.setCarrera(candidatoDto.getCarrera());
            candidato.setPromedioAcademico(candidatoDto.getPromedioAcademico());
            candidato.setHabilidades(candidatoDto.getHabilidades());
            candidato.setExperienciaLaboral(candidatoDto.getExperienciaLaboral());

            candidatoRepository.save(candidato);
            return "Alta exitosa";
     }


    /**
     *  Recupera todos los candidatos de la base de datos y los convierte a DTO.
     */
     public List<CandidatoDto> obtenerTodosLosCandidatos() {
            List<Candidato> candidatos = candidatoRepository.findAll();
            return candidatos.stream()
                    .map(this::convertirADto)
                    .collect(Collectors.toList());
     }


    /**
     *  Convierte una entidad Candidato en un objeto DTO.
     */
     private CandidatoDto convertirADto(Candidato candidato) {
            CandidatoDto dto = new CandidatoDto();
            dto.setId(candidato.getId());
            dto.setMail(candidato.getMail());
            dto.setNombreCompleto(candidato.getNombreCompleto());
            dto.setInstitucionEducativa(candidato.getInstitucionEducativa());
            dto.setCarrera(candidato.getCarrera());
            dto.setPromedioAcademico(candidato.getPromedioAcademico());
            dto.setHabilidades(candidato.getHabilidades());
            dto.setExperienciaLaboral(candidato.getExperienciaLaboral());
            return dto;
     }


    /**
     *  Busca un candidato por su ID y lo convierte en un DTO.
     */
    public CandidatoDto obtenerCandidatoPorId(int id) {
        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidato no encontrado con id: " + id));
        return convertirADto(candidato);
    }


    /**
     *  Genera un reporte en formato PDF de los candidatos preseleccionados.
     */
    public ByteArrayInputStream generarReportePDF() {
        List<PreseleccionadoDto> preseleccionados = preseleccionarCandidatos(); // Podés filtrar solo preseleccionados si tenés ese dato

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph titulo = new Paragraph("Reporte de Candidatos Preseleccionados", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);

            PdfPTable tabla = new PdfPTable(5);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[]{4f, 5f, 5f, 2f, 2f});

            // Encabezados
            Stream.of("Nombre", "Institución", "Carrera", "Promedio", "Puntaje")
                    .forEach(header -> {
                        PdfPCell celda = new PdfPCell();
                        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        celda.setPhrase(new Phrase(header));
                        celda.setNoWrap(false);
                        tabla.addCell(celda);
                    });

            Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // Datos
            for (PreseleccionadoDto c : preseleccionados) {
                PdfPCell cNombre = new PdfPCell(new Phrase(c.getNombreCompleto(), font));
                cNombre.setNoWrap(false);
                tabla.addCell(cNombre);

                PdfPCell cInstitucion = new PdfPCell(new Phrase(c.getInstitucionEducativa(), font));
                cInstitucion.setNoWrap(false);
                tabla.addCell(cInstitucion);

                PdfPCell cCarrera = new PdfPCell(new Phrase(c.getCarrera(), font));
                cCarrera.setNoWrap(false);
                tabla.addCell(cCarrera);

                PdfPCell cPromedio = new PdfPCell(new Phrase(c.getPromedioAcademico().toString(), font));
                cPromedio.setNoWrap(false);
                tabla.addCell(cPromedio);

                PdfPCell cPuntaje = new PdfPCell(new Phrase(c.getPuntajePreseleccion(), font));
                cPuntaje.setNoWrap(false);
                tabla.addCell(cPuntaje);
            }

            document.add(tabla);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private List<PreseleccionadoDto> preseleccionarCandidatos() {
        List<Candidato> candidatos = candidatoRepository.findAll();
        List<PreseleccionadoDto> preseleccionados = new ArrayList<>();

        List<String> instituciones = Arrays.stream(InstitucionesDePrestigio.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        List<String> habilidades = Arrays.stream(Habilidades.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        for (Candidato candidato : candidatos) {
            if (candidato.getPromedioAcademico() != null &&
                    candidato.getPromedioAcademico().compareTo(new BigDecimal("7.5")) > 0) {
                PreseleccionadoDto preseleccionado = new PreseleccionadoDto();
                preseleccionado.setNombreCompleto(candidato.getNombreCompleto());
                preseleccionado.setInstitucionEducativa(candidato.getInstitucionEducativa());
                preseleccionado.setCarrera(candidato.getCarrera());
                preseleccionado.setPromedioAcademico(candidato.getPromedioAcademico());

                boolean cumpleInstitucion = false;

                for (String inst : instituciones) {
                    if (inst.equalsIgnoreCase(candidato.getInstitucionEducativa())) {
                        cumpleInstitucion = true;
                        break;  // Sale del ciclo porque ya encontró una coincidencia
                    }
                }

                boolean cumpleHabilidad = false;
                if (candidato.getHabilidades() != null && !candidato.getHabilidades().isEmpty()) {
                    for (String habilidad : habilidades) {
                        if (candidato.getHabilidades().toLowerCase().contains(habilidad.toLowerCase())) {
                            cumpleHabilidad = true;
                            break;
                        }
                    }
                }

                int condicionesCumplidas = 1; // Ya cumple promedio > 7.5

                if (cumpleInstitucion) condicionesCumplidas++;
                if (cumpleHabilidad) condicionesCumplidas++;

                String puntaje;
                if (condicionesCumplidas == 3) {
                    puntaje = "Excelente";
                } else if (condicionesCumplidas == 2) {
                    puntaje = "Muy bueno";
                } else {
                    puntaje = "Bueno";
                }

                preseleccionado.setPuntajePreseleccion(puntaje);

                preseleccionados.add(preseleccionado);
            }
        }

        return preseleccionados;



    }
}




