package Gomez_Alonso.ClinicaOdontologica;

import Gomez_Alonso.ClinicaOdontologica.dao.BD;
import Gomez_Alonso.ClinicaOdontologica.model.Domicilio;
import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.model.Paciente;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;

import java.time.LocalDate;
import java.util.List;

public class Cliente {
    public static void main(String[] args) {
        BD.crearTablas();
        // Listar odontologos

        OdontologoService odontologoService = new OdontologoService();
        Odontologo odontologo0 = new Odontologo("12457SDF0","Leopoldo","Giraldo");
        Odontologo odontologo1 = new Odontologo("32323232","Lina","Gómez");

        odontologoService.guardarOdontologo(odontologo0);
        odontologoService.guardarOdontologo(odontologo1);

        /*List<Odontologo> odontologos = odontologoService.buscarTodos();

        System.out.println("Odontologos ensayo: ");
        for (Odontologo odontologo : odontologos) {
            System.out.println(odontologo.toString());
        }*/
        //Buscar por ID paciente
        Paciente paciente= new Paciente("Luisa","Toro","111111", LocalDate.of(2024,5,21),new Domicilio("calle falsa",123,"cali","Colombia"),"luisa@email.com",odontologo0);
        PacienteService pacienteService= new PacienteService();
        pacienteService.guardarPaciente(paciente);
        System.out.println("Paciente encontrado: "+ pacienteService.buscarPorID(3));
        System.out.printf("Odontologo encontrado: " + odontologoService.buscarPorID(1));
    }
}
