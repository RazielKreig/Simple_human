package org.candidatos.modelos;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "candidatos")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCandidatos")
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String mail;

    @Column(nullable = false, length = 50)
    private String nombreCompleto;

    @Column(nullable = false, length = 50)
    private String institucionEducativa;

    @Column(nullable = false, length = 50)
    private String carrera;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal promedioAcademico;

    @Column(length = 300)
    private String habilidades;

    @Column(length = 300)
    private String experienciaLaboral;


    //getters y setters//

    public String getExperienciaLaboral() {
        return experienciaLaboral;
    }

    public void setExperienciaLaboral(String experienciaLaboral) {
        this.experienciaLaboral = experienciaLaboral;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public BigDecimal getPromedioAcademico() {
        return promedioAcademico;
    }

    public void setPromedioAcademico(BigDecimal promedioAcademico) {
        this.promedioAcademico = promedioAcademico;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getInstitucionEducativa() {
        return institucionEducativa;
    }

    public void setInstitucionEducativa(String institucionEducativa) {
        this.institucionEducativa = institucionEducativa;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
