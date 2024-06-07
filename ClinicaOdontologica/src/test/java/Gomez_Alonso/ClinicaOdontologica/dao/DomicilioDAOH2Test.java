package Gomez_Alonso.ClinicaOdontologica.dao;

import Gomez_Alonso.ClinicaOdontologica.dao.BD;
import Gomez_Alonso.ClinicaOdontologica.model.Domicilio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

class DomicilioDAOH2Test {
    DomicilioDAOH2 domicilioDAOH2 = new DomicilioDAOH2();

    @Test
    void testGuardar() {
        BD.crearTablas();
        Domicilio expected = new Domicilio("calle", 0, "localidad", "provincia");
        Domicilio result = domicilioDAOH2.guardar(expected);
        // Comparar atributos individualmente
        Assertions.assertNotNull(result, "El resultado de guardar no debería ser nulo");
        Assertions.assertEquals(expected.getId(), result.getId(), "El id del domicilio no coincide");
        Assertions.assertEquals(expected.getCalle(), result.getCalle(), "La calle del domicilio no coincide");
        Assertions.assertEquals(expected.getNumero(), result.getNumero(), "El número del domicilio no coincide");
        Assertions.assertEquals(expected.getLocalidad(), result.getLocalidad(), "La localidad del domicilio no coincide");
        Assertions.assertEquals(expected.getProvincia(), result.getProvincia(), "La provincia del domicilio no coincide");
    }

    @Test
    void testBuscarPorId() {
        BD.crearTablas();
        Domicilio expected = new Domicilio("calle", 2, "localidad", "provincia");
        domicilioDAOH2.guardar(expected);
        Domicilio result = domicilioDAOH2.buscarPorId(3);

        // Comparar atributos individualmente
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expected.getId(), result.getId());
        Assertions.assertEquals(expected.getCalle(), result.getCalle());
        Assertions.assertEquals(expected.getNumero(), result.getNumero());
        Assertions.assertEquals(expected.getLocalidad(), result.getLocalidad());
        Assertions.assertEquals(expected.getProvincia(), result.getProvincia());
    }

    @Test
    void testEliminar() {
        int id = 3;
        // Agregar un domicilio para la prueba
        //Domicilio domicilio = new Domicilio( "calle", 0, "localidad", "provincia");
        //domicilioDAOH2.guardar(domicilio);

        // En guardar y buscar por id limpiaba la bd porq en ambas necesitaba guardar la dirección
        // y en buscar por id necesitaba tener esa en la bd, aquí como ya tenemos guardadas las
        // dos que vienen por defecto más la q sse agrega o en guardar o en buscarpor id, sólo
        // se necesita decirle q id borrar sin guardar una nueva.

        // Verificar que el domicilio existe antes de eliminarlo
        Domicilio domicilioExistente = domicilioDAOH2.buscarPorId(id);
        Assertions.assertNotNull(domicilioExistente);

        // Llamar al método eliminar
        domicilioDAOH2.eliminar(id);

        // Verificar que el domicilio ya no existe después de eliminarlo
        Domicilio domicilioEliminado = domicilioDAOH2.buscarPorId(id);
        Assertions.assertNull(domicilioEliminado);
    }

    @Test
    void testActualizar() {
        domicilioDAOH2.actualizar(new Domicilio(Integer.valueOf(0), "calle", Integer.valueOf(0), "localidad", "provincia"));
    }

    @Test
    void testBuscarTodos() {
       }

    @Test
    void testBuscarPorString() {
        }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme