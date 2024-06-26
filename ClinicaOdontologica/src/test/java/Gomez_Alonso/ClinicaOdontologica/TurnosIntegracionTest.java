package Gomez_Alonso.ClinicaOdontologica;

import Gomez_Alonso.ClinicaOdontologica.entity.Domicilio;
import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
import Gomez_Alonso.ClinicaOdontologica.entity.Turno;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import Gomez_Alonso.ClinicaOdontologica.service.TurnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TurnosIntegracionTest {
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private MockMvc mockMvc;

    private void cargarDatos(){
        Paciente paciente= pacienteService.guardarPaciente(new Paciente("Jorgito","pereyra","11111", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"jorge.pereyra@digitalhouse.com"));
        Odontologo odontologo= odontologoService.guardarOdontologo(new Odontologo("MP10","Gina","Arias"));
        Turno turno= turnoService.guardarTurno(new Turno(paciente,odontologo,LocalDate.of(2024,6,20), Time.valueOf("20:00:00")));

    }

    @Test
    public void RegistrarTurnoTest() throws Exception{
        Paciente paciente = new Paciente(1L,"Julian","Callejas", "123", LocalDate.of(2024,6,18),new Domicilio("40",12,"Quibdó","Chocó"),"julian@email.com");
        Odontologo odontologo = new Odontologo("ABC123","Lina","Gómez");
        Turno turno= new Turno(paciente,odontologo, LocalDate.of(2024,12,2), Time.valueOf("13:00:00"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(turno);

        MvcResult respuesta= mockMvc.perform(post("/turnos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.paciente.nombre").value("Julian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.odontologo.nombre").value("Lina"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fecha").value("2024-12-02"))
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }

    @Test
    public void ListarTodosLosTurnosTest() throws Exception{
        cargarDatos();
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/turnos").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }

    @Test
    public void BuscarTurnoTest() throws Exception{
        long idTurno = 1L;
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/turnos/{id}", idTurno)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void testEliminarTurno() throws Exception {
        long idTurno = 2L;
        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/eliminar/{id}", idTurno))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Turno con ID 2 eliminado con éxito"))
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }
}
