package Gomez_Alonso.ClinicaOdontologica.Service;


import Gomez_Alonso.ClinicaOdontologica.dao.BD;
import Gomez_Alonso.ClinicaOdontologica.model.Paciente;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class PacienteServiceTest {
    @Test
    public void buscarPacientePorID(){
        BD.crearTablas();
        PacienteService pacienteService= new PacienteService();
        Integer id=2;
        Paciente paciente= pacienteService.buscarPorID(id);
        Assertions.assertTrue(paciente!=null);
    }
}
