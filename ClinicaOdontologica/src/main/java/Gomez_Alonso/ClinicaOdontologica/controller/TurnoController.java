package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.model.Paciente;
import Gomez_Alonso.ClinicaOdontologica.model.Turno;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import Gomez_Alonso.ClinicaOdontologica.service.TurnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private TurnoService turnoService;
    private PacienteService pacienteService;
    private OdontologoService odontologoService;

    public TurnoController() {
        turnoService= new TurnoService();
        pacienteService= new PacienteService();
        odontologoService= new OdontologoService();
    }

    @PostMapping
    public ResponseEntity<Turno> guardarTurno(@RequestBody Turno turno){
        Paciente pacienteBuscado= pacienteService.buscarPorID(turno.getPaciente().getId());
        Odontologo odontologoBuscado= odontologoService.buscarPorID(turno.getOdontologo().getId());
        if(pacienteBuscado!=null&&odontologoBuscado!=null){
            return ResponseEntity.ok(turnoService.guardarTurno(turno));
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        Turno turnoBuscado = turnoService.buscarPorId(id);
        if (turnoBuscado != null){
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
        Turno turnoBuscado= turnoService.buscarPorId(turno.getId());
        if(turnoBuscado!=null){
            turnoService.actualizarTurno(turno);
            return ResponseEntity.ok("Turno actualizado con éxito");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El turno con ID " + turno.getId() +
                    " no se encuentra registrado para realizar la actualización");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Integer id){
        Turno turnoBuscado= turnoService.buscarPorId(id);
        if (turnoBuscado != null) {
            turnoService.eliminarTurno(id);
            return ResponseEntity.ok().body("Turno con ID " + id + " eliminado con éxito");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede eliminar un turno que no existe");
        }
    }
}
