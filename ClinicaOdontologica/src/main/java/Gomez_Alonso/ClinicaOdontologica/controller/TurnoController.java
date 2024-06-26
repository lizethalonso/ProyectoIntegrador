package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
import Gomez_Alonso.ClinicaOdontologica.entity.Turno;
import Gomez_Alonso.ClinicaOdontologica.exception.BadRequestException;
import Gomez_Alonso.ClinicaOdontologica.exception.NoContentException;
import Gomez_Alonso.ClinicaOdontologica.exception.ResourceNotFoundException;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import Gomez_Alonso.ClinicaOdontologica.service.TurnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/turnos")
@Tag(name = "Controller de Turnos", description = "Permite registrar, buscar, actualizar, eliminar y listar")
public class TurnoController {
    @Autowired
    private TurnoService turnoService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private OdontologoService odontologoService;

    private static final Logger logger = Logger.getLogger(PacienteController.class);

    @PostMapping
    @Operation(summary = "nos permite registrar", description = "enviar cédula del paciente, matrícula del odontólogo")
    @ApiResponse(responseCode = "200", description = "Turno creado con exito")
    @ApiResponse(responseCode = "400", description = "No se puede guardar el turno porque el paciente u odontólogo no existen")
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
            logger.error("No se puede guardar el turno porque el paciente u odontólogo no existe");
            throw new BadRequestException("No se puede guardar el turno porque el paciente u odontólogo no existe");
        }
    }

    @GetMapping("/{id}")
    @Parameter(description = "Nos permite buscar un turno por Id")
    @ApiResponse(responseCode = "200", description = "Turno encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "No se puede encontrar el turno")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<Turno> turnoBuscado = turnoService.buscarPorId(id);
        if (turnoBuscado.isPresent()){
            return ResponseEntity.ok(turnoBuscado);
        }else {
            logger.error("Turno no encontrado");
            throw new ResourceNotFoundException("Turno con ID " + id + " no encontrado");
        }
    }

    @GetMapping
    @Parameter(description = "Nos permite listar todos los turnos")
    @ApiResponse(responseCode = "200", description = "Lista mostrada con éxito")
    @ApiResponse(responseCode = "204", description = "Lista vacía")
    public ResponseEntity<List<Turno>> buscarTodos() throws NoContentException {
        List<Turno> turnosBuscados = turnoService.buscarTodos();
        if (!turnosBuscados.isEmpty()) {
            return ResponseEntity.ok(turnosBuscados);
        } else {
            logger.error("La lista de turnos está vacía");
            throw new NoContentException("La lista de turnos está vacía");
        }
    }

    @PutMapping
    @Operation(summary = "Nos permite actualizar", description = "Enviar turno con cédula del paciente y matrícula del odontólogo")
    @ApiResponse(responseCode = "200", description = "Turno actualizado con exito")
    @ApiResponse(responseCode = "400", description = "Odontólogo o paciente no existe para actualizar turno")
    @ApiResponse(responseCode = "400", description = "Turno no existe para actualizar")
    public ResponseEntity<?>actualizarTurno(@RequestBody Turno turno) throws BadRequestException {
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
                logger.error("No se encontró el Paciente u Odontólogo asociado al Turno que desea actualizar");
                throw new BadRequestException("No se encontró el Paciente u Odontólogo asociado al Turno que desea actualizar");
                //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se encontró el Paciente u Odontólogo asociado al Turno que desea actualizar");
            }
        }else {
            logger.error("Turno no encontrado para realizar la actualización");
            throw new BadRequestException("El turno con ID " + turno.getId() +
                    " no se encuentra registrado para realizar la actualización");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Nos permite eliminar", description = "Enviar id de turno")
    @ApiResponse(responseCode = "200", description = "Turno eliminado con exito")
    @ApiResponse(responseCode = "404", description = "Turno no encontrado")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id) throws ResourceNotFoundException{
        Optional<Turno> turnoBuscado= turnoService.buscarPorId(id);
        if (turnoBuscado.isPresent()) {
            turnoService.eliminarTurno(id);
            return ResponseEntity.ok().body("Turno con ID " + id + " eliminado con éxito");
        }else {
            logger.error("No se puede eliminar un turno que no existe");
            throw new ResourceNotFoundException("No se puede eliminar un turno que no existe");
        }
    }
}
