package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
import Gomez_Alonso.ClinicaOdontologica.exception.ResourceNotFoundException;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController //para trabajar sin tecnologia de vista
// @Controller<-- es controller pq vamos a usar una tecnologia de vista

@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;

    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    public ResponseEntity<?> guardarPaciente(@RequestBody Paciente paciente){
        Optional<Paciente> pacienteExistente = pacienteService.buscarPorCedula(paciente.getCedula());
        if (pacienteExistente.isPresent()){
            return ResponseEntity.badRequest().body("Ya existe un paciente con cédula " + paciente.getCedula());
        }else {
            return ResponseEntity.ok(pacienteService.guardarPaciente(paciente));
        }
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> buscarTodos(){
        List<Paciente> pacientes = pacienteService.buscarTodos();
        if (!pacientes.isEmpty()) {
            return ResponseEntity.ok(pacientes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorPaciente(@PathVariable Long id){
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(id);
        if (pacienteBuscado.isPresent()){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email){
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorEmail(email);
        if (pacienteBuscado.isPresent()){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con email " + email + " no encontrado");
        }
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<?> buscarPorCedula(@PathVariable String cedula){
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorCedula(cedula);
        if (pacienteBuscado.isPresent()){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con cedula " + cedula + " no encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente){
        Optional<Paciente> pacienteBuscado= pacienteService.buscarPorID(paciente.getId());
        if(pacienteBuscado.isPresent()){
            pacienteService.actualizarPaciente(paciente);
            return ResponseEntity.ok("Paciente actualizado con éxito");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El paciente con ID " + paciente.getId() +
                    " no se encuentra registrado para realizar la actualización");
            //return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) throws ResourceNotFoundException {
        if (pacienteService.buscarPorID(id).isPresent()){
            pacienteService.eliminarPaciente(id);
            return ResponseEntity.ok().body("Paciente con ID " + id + " eliminado con éxito");
        }else{
            throw new ResourceNotFoundException("Paciente no encontrado para eliminar");
        }
    }

}
