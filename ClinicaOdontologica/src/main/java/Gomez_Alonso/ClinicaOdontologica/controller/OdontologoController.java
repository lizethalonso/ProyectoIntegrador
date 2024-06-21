package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.exception.BadRequestException;
import Gomez_Alonso.ClinicaOdontologica.exception.NoContentException;
import Gomez_Alonso.ClinicaOdontologica.exception.ResourceNotFoundException;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    @Autowired
    private OdontologoService odontologoService;
    private static final Logger logger = Logger.getLogger(OdontologoController.class);

    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    public ResponseEntity<?> guardarOdontologo(@RequestBody Odontologo odontologo) throws BadRequestException {
        Optional<Odontologo> odontologoExistente = odontologoService.buscarPorMatricula(odontologo.getmatricula());
        if (odontologoExistente.isPresent()){
            logger.error("Ya existe un odontólogo registrado con la matrícula proporcionada");
            throw new BadRequestException("Ya existe un odontólogo con matrícula: " + odontologo.getmatricula());
        }else {
            return ResponseEntity.ok(odontologoService.guardarOdontologo(odontologo));
        }
    }

    @GetMapping
    public ResponseEntity<List<Odontologo>> buscarTodos() throws NoContentException {
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        if (!odontologos.isEmpty()) {
            return ResponseEntity.ok(odontologos);
        } else {
            logger.error("La lista de odontólogos está vacía");
            throw new NoContentException("La lista de odontólogos está vacía");
            //return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
            logger.error("Odontólogo no existe");
            throw new ResourceNotFoundException("Odontólogo con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<?> buscarPorMatricula(@PathVariable String matricula) throws ResourceNotFoundException{
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorMatricula(matricula);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
            logger.error("Odontólogo no existe");
            throw new ResourceNotFoundException("Odontólogo con " + matricula + " no encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarOdontologo(@RequestBody Odontologo odontologo) throws BadRequestException {
        Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorID(odontologo.getId());
        if(odontologoBuscado.isPresent()){
            odontologoService.actualizarOdontologo(odontologo);
            return ResponseEntity.ok().body("Odontólogo actualizado con éxito");
        }else{
            logger.error("Odontólogo no existe para actualizar");
            throw new BadRequestException("Odontólogo con ID " + odontologo.getId() +
                    " no se encuentra registrado para realizar la actualización");

        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity <String> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        if (odontologoService.buscarPorID(id).isPresent()) {
            odontologoService.eliminarOdontologo(id);
            return ResponseEntity.ok().body("Odontólogo con ID: " + id + " eliminado con éxito ");
        } else {
            logger.error("Odontólogo no existe");
            throw new ResourceNotFoundException("Odontólogo no encontrado para eliminar");
        }
    }
}
