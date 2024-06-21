package Gomez_Alonso.ClinicaOdontologica;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
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

import static org.junit.jupiter.api.Assertions.assertFalse;

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
        MvcResult respuesta= mockMvc.perform(MockMvcRequestBuilders.post("/odontologos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matricula").value("MP10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nombre").value("Gina"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.apellido").value("Arias"))
                .andReturn();
        // Verificar que el cuerpo de la respuesta no esté vacío
        assertFalse(respuesta.getResponse().getContentAsString().isEmpty());

    }
}
