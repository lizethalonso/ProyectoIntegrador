package Gomez_Alonso.ClinicaOdontologica.Service;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OdontologoServiceTest {
    @Autowired
    private OdontologoService odontologoService;

    @Test
    @Order(1)
    public void guardarOdontologo(){
        Odontologo odontologo= new Odontologo("HIJ987", "luisa", "Toro");
        Odontologo odontologoGuardado= odontologoService.guardarOdontologo(odontologo);
        assertEquals(3L,odontologoGuardado.getId());
    }

    @Test
    @Order(2)
    public void buscarOdontologoPorId(){
        Long id= 1L;
        Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorID(id);
        assertNotNull(odontologoBuscado.get());
    }

    @Test
    @Order(3)
    public void actualizarOdontologo(){
        Long id= 3L;
        Odontologo odontologo= new Odontologo(id,"HIJ987", "Lulu", "Toro");
        odontologoService.actualizarOdontologo(odontologo);
        Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorID(id);
        assertEquals("Lulu", odontologoBuscado.get().getNombre());
    }

    @Test
    @Order(4)
    public void ListarTodos(){
        List<Odontologo> listaOdontologos= odontologoService.buscarTodos();
        assertEquals(3,listaOdontologos.size());
    }
    @Test
    @Order(5)
    public void eliminarOdontologo(){
        odontologoService.eliminarOdontologo(1L);
        Optional<Odontologo> odontologoEliminado= odontologoService.buscarPorID(1L);
        assertFalse(odontologoEliminado.isPresent());
    }
    @Test
    @Order(6)
    public void buscarPorMatricula(){
        String matricula = "HIJ987";
        Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorMatricula(matricula);
        assertNotNull(odontologoBuscado.get().getmatricula());
    }

}

