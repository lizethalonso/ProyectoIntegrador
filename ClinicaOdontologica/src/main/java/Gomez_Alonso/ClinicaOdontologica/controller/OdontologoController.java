package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.exception.BadRequestException;
import Gomez_Alonso.ClinicaOdontologica.exception.NoContentException;
import Gomez_Alonso.ClinicaOdontologica.exception.ResourceNotFoundException;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
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

    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    public ResponseEntity<?> guardarOdontologo(@RequestBody Odontologo odontologo) throws BadRequestException {
        Optional<Odontologo> odontologoExistente = odontologoService.buscarPorMatricula(odontologo.getmatricula());
        if (odontologoExistente.isPresent()){
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
            throw new ResourceNotFoundException("Odontólogo con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<?> buscarPorMatricula(@PathVariable String matricula) throws ResourceNotFoundException{
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorMatricula(matricula);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
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
            throw new ResourceNotFoundException("Odontólogo no encontrado para eliminar");
        }
    }
}
