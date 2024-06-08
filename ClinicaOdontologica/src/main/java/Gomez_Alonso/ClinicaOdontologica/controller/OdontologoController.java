package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
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
    public ResponseEntity<?> guardarOdontologo(@RequestBody Odontologo odontologo){
        Optional<Odontologo> odontologoExistente = odontologoService.buscarPorMatricula(odontologo.getmatricula());
        if (odontologoExistente.isPresent()){
            return ResponseEntity.badRequest().body("Ya existe un odontólogo con matrícula: " + odontologo.getmatricula());
        }else {
            return ResponseEntity.ok(odontologoService.guardarOdontologo(odontologo));
        }
    }

    @GetMapping
    public ResponseEntity<List<Odontologo>> buscarTodos(){
        List<Odontologo> odontologos = odontologoService.buscarTodos();
        if (!odontologos.isEmpty()) {
            return ResponseEntity.ok(odontologos);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorOdontologo(@PathVariable Long id){
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Odontólogo con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/matricula/{matricula}")
    public ResponseEntity<?> buscarPorCampo(@PathVariable String matricula){
        Optional<Odontologo> odontologoBuscado = odontologoService.buscarPorMatricula(matricula);
        if (odontologoBuscado.isPresent()) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Odontólogo con " + matricula + " no encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarOdontologo(@RequestBody Odontologo odontologo){
        Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorID(odontologo.getId());
        if(odontologoBuscado.isPresent()){
            odontologoService.actualizarOdontologo(odontologo);
            return ResponseEntity.ok().body("Odontólogo actualizado con éxito");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Odontólogo con ID " + odontologo.getId() +
                    " no se encuentra registrado para realizar la actualización");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity <?> eliminarOdontologo(@PathVariable Long id){
        if (odontologoService.buscarPorID(id).isPresent()) {
            odontologoService.eliminarOdontologo(id);
            return ResponseEntity.ok().body("Odontólogo con ID: " + id + " eliminado con éxito ");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede eliminar un odontólogo que no existe");
        }
    }
}
