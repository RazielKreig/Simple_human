package org.candidatos.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.candidatos.modelos.Candidato;
import org.candidatos.repositorios.CandidatoRepository;
import org.springframework.http.*;
import java.io.ByteArrayInputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.candidatos.modelos.CandidatoDto;
import org.candidatos.servicios.CandidatoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/*
 *  Controlador que gestiona las operaciones relacionadas con los candidatos.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/candidatos")
public class CandidatoController {

    private final CandidatoService candidatoService;
    private final CandidatoRepository candidatoRepository;

    /*
     *  Inyección de dependencias mediante constructor.
     */
    public CandidatoController(CandidatoService candidatoService, CandidatoRepository candidatoRepository) {
        this.candidatoService = candidatoService;
        this.candidatoRepository = candidatoRepository;
    }

    /*
     *  Endpoint para dar de alta un candidato nuevo
     */
    @PostMapping("/alta")
    public String altaCandidato(@RequestBody CandidatoDto candidatoDto) {

        String respuesta = candidatoService.altaCandidato(candidatoDto);
        return respuesta;

    }


    /*
     *  Endpoint para recibir la lista de todos los candidatos
     */
    @GetMapping("/lista")
    public List<CandidatoDto> listarCandidatos() {
        return candidatoService.obtenerTodosLosCandidatos();
    }


    /*
     *  Endpoint para recibir un candidato por medio de su id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CandidatoDto> obtenerCandidatoPorId(@PathVariable int id) {
        try {
            CandidatoDto candidatoDto = candidatoService.obtenerCandidatoPorId(id);
            return ResponseEntity.ok(candidatoDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    /*
     *  Genera un archivo PDF con los candidatos preseleccionados.
     */
    @GetMapping("/reporte")
    public ResponseEntity<byte[]> generarReporte() {
        ByteArrayInputStream pdfStream = candidatoService.generarReportePDF();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=reporte_candidatos.pdf");

        byte[] pdfBytes = pdfStream.readAllBytes(); // no tira IOException

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);

    }


    /*
     *  Genera un archivo JSON con todos los candidatos registrados.
     */
    @GetMapping("/json")
    public ResponseEntity<byte[]> descargarJSON() {
        List<Candidato> candidatos = candidatoRepository.findAll();

        try {
            ObjectMapper mapper = new ObjectMapper();
            byte[] jsonBytes = mapper.writeValueAsBytes(candidatos);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=candidatos.json");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonBytes);

        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}