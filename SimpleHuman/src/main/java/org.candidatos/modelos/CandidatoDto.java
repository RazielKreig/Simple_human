package org.candidatos.modelos;

import java.math.BigDecimal;


/**
 * DTO que representa los datos transferidos de un candidato.
 */
public class CandidatoDto {

    private int id;

    private String mail;

    private String nombreCompleto;

    private String institucionEducativa;

    private String carrera;

    private BigDecimal promedioAcademico;

    private String habilidades;

    private String experienciaLaboral;


    //getters y setters//

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getInstitucionEducativa() {
        return institucionEducativa;
    }

    public void setInstitucionEducativa(String institucionEducativa) {
        this.institucionEducativa = institucionEducativa;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public BigDecimal getPromedioAcademico() {
        return promedioAcademico;
    }

    public void setPromedioAcademico(BigDecimal promedioAcademico) {
        this.promedioAcademico = promedioAcademico;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public String getExperienciaLaboral() {
        return experienciaLaboral;
    }

    public void setExperienciaLaboral(String experienciaLaboral) {
        this.experienciaLaboral = experienciaLaboral;
    }
}