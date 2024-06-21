package Gomez_Alonso.ClinicaOdontologica.Service;

import Gomez_Alonso.ClinicaOdontologica.entity.Domicilio;
import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
import Gomez_Alonso.ClinicaOdontologica.entity.Turno;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import Gomez_Alonso.ClinicaOdontologica.service.TurnoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TurnoServiceTest {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private OdontologoService odontologoService;
    @Autowired
    private PacienteService pacienteService;


    @Test
    @Order(1)
    public void guardarTurno(){
        Paciente paciente = new Paciente("Julieta","Muñoz","2222", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"julieta@digitalhouse.com");
        Odontologo odontologo = new Odontologo("HIJ977", "Maria", "Mesa");

        paciente = pacienteService.guardarPaciente(paciente);
        odontologo = odontologoService.guardarOdontologo(odontologo);

        Turno turno= new Turno(paciente,odontologo, LocalDate.of(2024,12,2), Time.valueOf("13:00:00"));
        Turno turnoGuardado= turnoService.guardarTurno(turno);
        assertEquals(1L,turnoGuardado.getId());
    }

    @Test
    @Order(2)
    public void buscarTurnoPorId(){
        Long id= 1L;
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(id);
        assertNotNull(turnoBuscado.get());
    }

    @Test
    @Order(3)
    public void actualizarTurno(){
        Long id= 1L;
        Paciente paciente = new Paciente("Julieta","Muñoz","3333", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"julieta@digitalhouse.com");
        Odontologo odontologo = new Odontologo("HIJ945", "Maria", "Mesa");

        paciente = pacienteService.guardarPaciente(paciente);
        odontologo = odontologoService.guardarOdontologo(odontologo);
        Turno turno= new Turno(id,paciente, odontologo, LocalDate.of(2024,12,4), Time.valueOf("13:00:00"));
        turnoService.actualizarTurno(turno);
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(id);
        assertEquals("HIJ945", turnoBuscado.get().getOdontologo().getmatricula());
    }

    @Test
    @Order(4)
    public void ListarTodos(){
        List<Turno> listaTurnos= turnoService.buscarTodos();
        assertEquals(1,listaTurnos.size());
    }
    @Test
    @Order(5)
    public void eliminarTurno(){
        turnoService.eliminarTurno(1L);
        Optional<Turno> turnoEliminado= turnoService.buscarPorId(1L);
        assertFalse(turnoEliminado.isPresent());
    }
}

