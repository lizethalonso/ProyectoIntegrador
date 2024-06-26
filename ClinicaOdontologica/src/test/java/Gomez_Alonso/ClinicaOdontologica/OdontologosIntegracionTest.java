package Gomez_Alonso.ClinicaOdontologica;

import Gomez_Alonso.ClinicaOdontologica.controller.OdontologoController;
import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OdontologosIntegracionTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void RegistrarOdontologoTest() throws Exception{
        // Crear el objeto odontólogo
        Odontologo odontologo = new Odontologo("MP10", "Gina", "Arias");

        // Convertir el objeto odontólogo a JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(odontologo);

        // Realizar la solicitud POST con el contenido JSON
        MvcResult respuesta= mockMvc.perform(post("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matricula").value("MP10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Gina"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apellido").value("Arias"))
                .andReturn();

        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }

    @Test
    public void ListarTodosLosOdontolgosTest() throws Exception{
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/odontologos").accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void BuscarOdontolgoTest() throws Exception{
        long idOdontologo = 2L;
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/{id}", idOdontologo)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void BuscarOdontolgoMatriculaTest() throws Exception{
        String matriculaOdontologo = "ABC123";
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/matricula/{matricula}", matriculaOdontologo)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());
    }

    @Test
    public void testEliminarOdontologo() throws Exception {
        long idOdontologo = 1L;
        MvcResult respuesta = mockMvc.perform(MockMvcRequestBuilders.delete("/odontologos/eliminar/{id}", idOdontologo))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Odontólogo con ID: 1 eliminado con éxito"))
                .andReturn();
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }

}


