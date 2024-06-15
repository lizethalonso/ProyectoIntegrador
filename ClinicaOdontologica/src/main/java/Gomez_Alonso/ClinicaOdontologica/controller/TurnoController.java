package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
import Gomez_Alonso.ClinicaOdontologica.entity.Turno;
import Gomez_Alonso.ClinicaOdontologica.exception.BadRequestException;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import Gomez_Alonso.ClinicaOdontologica.service.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;

    @PostMapping
    public ResponseEntity<Turno> guardarTurno(@RequestBody Turno turno) throws BadRequestException {
        // Buscamos al paciente y al odontólgo, se usa optional obligatoriamente porque es un objeto q no sabe si existe o no
        //Optional<Paciente> pacienteBuscado= pacienteService.buscarPorID(turno.getPaciente().getId());
        //Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorID(turno.getOdontologo().getId());
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorCedula(turno.getPaciente().getCedula());
        Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorMatricula(turno.getOdontologo().getmatricula());
        if(pacienteBuscado.isPresent()&&odontologoBuscado.isPresent()){
            // Como si está, se asigna el paciente y odontolgo encontrados al campo id q ponemos en postman
            turno.setPaciente(pacienteBuscado.get()); // pacienteBuscado.get() con esto podemos extraer el objeto del optional de arriba
            turno.setOdontologo(odontologoBuscado.get());
            return ResponseEntity.ok(turnoService.guardarTurno(turno));
        }else{
            throw new BadRequestException("No se puede guardar el turno porque el paciente u odontólogo no existe");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        Optional<Turno> turnoBuscado = turnoService.buscarPorId(id);
        if (turnoBuscado.isPresent()){
            return ResponseEntity.ok(turnoBuscado);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Turno con ID " + id + " no encontrado");
        }
    }

    @GetMapping
    public ResponseEntity<List<Turno>> buscarTodos(){
        return ResponseEntity.ok(turnoService.buscarTodos());
    }

    @PutMapping
    public ResponseEntity<?>actualizarTurno(@RequestBody Turno turno){
        // Buscamos el turno para ver si existe
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(turno.getId());
        if(turnoBuscado.isPresent()){
            Turno turnoExistente = turnoBuscado.get(); // Lo guardamos en un objeto puro Turno, porque estamos seguras que si está
            // Buscamos el paciente y odontólogo
            //Optional<Paciente> pacienteOptional = pacienteService.buscarPorID(turno.getPaciente().getId());
            //Optional<Odontologo> odontologoOptional = odontologoService.buscarPorID(turno.getPaciente().getId());
            Optional<Paciente> pacienteBuscado = pacienteService.buscarPorCedula(turno.getPaciente().getCedula());
            Optional<Odontologo> odontologoBuscado= odontologoService.buscarPorMatricula(turno.getOdontologo().getmatricula());
            if (pacienteBuscado.isPresent() && odontologoBuscado.isPresent()) {
                // Como si están, se asignan estos objetos 
                turnoExistente.setPaciente(pacienteBuscado.get());
                turnoExistente.setOdontologo(odontologoBuscado.get());
                // Se setean fecha y hora a actualizar
                turnoExistente.setFecha(turno.getFecha());
                turnoExistente.setHora(turno.getHora());
                turnoService.actualizarTurno(turnoExistente);
                return ResponseEntity.ok("Turno actualizado con éxito");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontró el Paciente u Odontólogo asociado al Turno que desea actualizar");
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El turno con ID " + turno.getId() +
                    " no se encuentra registrado para realizar la actualización");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id){
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(id);
        if (turnoBuscado.isPresent()) {
            turnoService.eliminarTurno(id);
            return ResponseEntity.ok().body("Turno con ID " + id + " eliminado con éxito");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede eliminar un turno que no existe");
        }
    }
}
