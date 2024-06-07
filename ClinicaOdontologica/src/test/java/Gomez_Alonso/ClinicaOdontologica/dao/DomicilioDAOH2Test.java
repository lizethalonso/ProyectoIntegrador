package Gomez_Alonso.ClinicaOdontologica.dao;

import Gomez_Alonso.ClinicaOdontologica.entity.Domicilio;
import Gomez_Alonso.ClinicaOdontologica.repository.DomicilioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class DomicilioDAOH2Test {
    private DomicilioRepository domicilioRepository;

    @Test
    void testGuardar() {
        Domicilio expected = new Domicilio("calle", 0, "localidad", "provincia");
        Domicilio result = domicilioRepository.save(expected);
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
        Domicilio expected = new Domicilio("calle", 2, "localidad", "provincia");
        domicilioRepository.save(expected);
        Optional<Domicilio> resultOp = domicilioRepository.findById(Long.valueOf(2));

        Assertions.assertTrue(resultOp.isPresent());
        Domicilio result = resultOp.get();


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
        Long id = Long.valueOf(3);
        // Agregar un domicilio para la prueba
        //Domicilio domicilio = new Domicilio( "calle", 0, "localidad", "provincia");
        //domicilioDAOH2.guardar(domicilio);

        // En guardar y buscar por id limpiaba la bd porq en ambas necesitaba guardar la dirección
        // y en buscar por id necesitaba tener esa en la bd, aquí como ya tenemos guardadas las
        // dos que vienen por defecto más la q sse agrega o en guardar o en buscarpor id, sólo
        // se necesita decirle q id borrar sin guardar una nueva.

        // Verificar que el domicilio existe antes de eliminarlo
        Optional<Domicilio> domicilioExistente = domicilioRepository.findById(id);
        Assertions.assertNotNull(domicilioExistente);

        // Llamar al método eliminar
        domicilioRepository.deleteById(id);

        // Verificar que el domicilio ya no existe después de eliminarlo
        Optional<Domicilio> domicilioEliminado = domicilioRepository.findById(id);
        Assertions.assertNull(domicilioEliminado);
    }

    @Test
    void testActualizar() {
        domicilioRepository.save(new Domicilio(Long.valueOf(0), "calle", 0, "localidad", "provincia"));
    }

    @Test
    void testBuscarTodos() {
       }

    @Test
    void testBuscarPorString() {
        }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme