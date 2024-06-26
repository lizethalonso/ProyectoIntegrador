package Gomez_Alonso.ClinicaOdontologica;

import Gomez_Alonso.ClinicaOdontologica.entity.Domicilio;
import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PacientesIntegracionTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void RegistrarPacienteTest() throws Exception{
        // Crear el objeto paciente
        Paciente paciente = new Paciente("Lucia","Casas","1515", LocalDate.of(2024,6,20),new Domicilio("calle falsa",123,"La Rioja","Argentina"),"lucia@lucia.com");

        // Convertir el objeto odontólogo a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String json = objectMapper.writeValueAsString(paciente);

        // Realizar la solicitud POST con el contenido JSON
        MvcResult respuesta= mockMvc.perform(post("/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Lucia"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apellido").value("Casas"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cedula").value("1515"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.domicilio.calle").value("calle falsa"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("lucia@lucia.com"))
                .andReturn();
        // Verificar que el cuerpo de la respuesta no esté vacío
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }

    @Test
    public void ListarTodosPacientesTest() throws Exception{
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/pacientes").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void BuscarPacienteTest() throws Exception{
        long idPaciente = 2L;
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/{id}", idPaciente)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void BuscarPacienteCedulaTest() throws Exception{
        String cedulaPaciente = "123";
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/cedula/{cedula}", cedulaPaciente)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void BuscarPacienteEmailTest() throws Exception{
        String emailPaciente = "camila@email.com";
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/email/{email}", emailPaciente)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void testEliminarPaciente() throws Exception {
        long idPaciente = 3L;
        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.delete("/pacientes/eliminar/{id}", idPaciente))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Paciente con ID 3 eliminado con éxito"))
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
        assertEquals("Paciente con ID 3 eliminado con éxito", respuesta.getResponse().getContentAsString());

    }
}


