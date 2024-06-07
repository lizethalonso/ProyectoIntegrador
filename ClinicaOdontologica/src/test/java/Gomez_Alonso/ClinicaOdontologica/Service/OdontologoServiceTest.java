package Gomez_Alonso.ClinicaOdontologica.Service;


import Gomez_Alonso.ClinicaOdontologica.dao.BD;
import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
public class OdontologoServiceTest{
    @Test
    public void listarTodosLosOdontologos() {
        BD.crearTablas();
        OdontologoService odontologoService = new OdontologoService();
        //Odontologo odontologo1 = new Odontologo(1,"12345", "Juan", "Perez");
        //Odontologo odontologo2 = new Odontologo(2,"67890", "Ana", "Lopez");
        //odontologoService.guardarOdontologo(odontologo1);
        //odontologoService.guardarOdontologo(odontologo2);
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        Assertions.assertEquals(2, odontologos.size());
    }
}

