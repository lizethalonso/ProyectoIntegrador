package Gomez_Alonso.ClinicaOdontologica.model;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Paciente {
    private Integer id;
    private String nombre;
    private String apellido;
    private String cedula;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;
    private String email;
    private Odontologo odontologo;

    public Paciente(String nombre, String apellido, String cedula, LocalDate fechaIngreso, Domicilio domicilio, String email, Odontologo odontologo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.email = email;
        this.odontologo = odontologo;
    }

    public Paciente(Integer id, String nombre, String apellido, String cedula, LocalDate fechaIngreso, Domicilio domicilio, String email, Odontologo odontologo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
        this.email= email;
        this.odontologo = odontologo;
    }

    public Paciente() {
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", cedula='" + cedula + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", domicilio=" + domicilio +
                ", email='" + email + '\'' +
                ", odontologo=" + odontologo +
                '}';
    }




}
