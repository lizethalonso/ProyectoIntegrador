package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Paciente;
import Gomez_Alonso.ClinicaOdontologica.exception.BadRequestException;
import Gomez_Alonso.ClinicaOdontologica.exception.NoContentException;
import Gomez_Alonso.ClinicaOdontologica.exception.ResourceNotFoundException;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
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


@RestController //para trabajar sin tecnologia de vista
// @Controller<-- es controller pq vamos a usar una tecnologia de vista

@RequestMapping("/pacientes")
@Tag(name = "Controller de Paciente", description = "Permite registrar, buscar, actualizar, eliminar y listar")
public class PacienteController {
    @Autowired
    private PacienteService pacienteService;
    private static final Logger logger = Logger.getLogger(PacienteController.class);

    @Operation(summary = "nos permite registrar", description = "Enviar paciente sin id")
    @ApiResponse(responseCode = "200", description = "Paciente creado con exito")
    @ApiResponse(responseCode = "400", description = "Ya existe un paciente con la cédula")
    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    public ResponseEntity<?> guardarPaciente(@RequestBody Paciente paciente) throws BadRequestException {
        Optional<Paciente> pacienteExistente = pacienteService.buscarPorCedula(paciente.getCedula());
        if (pacienteExistente.isPresent()){
            logger.error("Ya existe un paciente registrado con la cédula proporcionada");
            throw new BadRequestException("Ya existe un paciente con cédula " + paciente.getCedula());
        }else {
            return ResponseEntity.ok(pacienteService.guardarPaciente(paciente));
        }
    }

    @GetMapping
    @Parameter(description = "Nos permite listar todos los pacientes")
    @ApiResponse(responseCode = "200", description = "Lista mostrada con éxito")
    @ApiResponse(responseCode = "204", description = "Lista vacía")
    public ResponseEntity<List<Paciente>> buscarTodos() throws NoContentException {
        List<Paciente> pacientes = pacienteService.buscarTodos();
        if (!pacientes.isEmpty()) {
            return ResponseEntity.ok(pacientes);
        } else {
            logger.error("La lista de pacientes está vacía");
            throw new NoContentException("La lista de pacientes está vacía");
        }
    }

    @GetMapping("/{id}")
    @Parameter(description = "Nos permite buscar un paciente por Id")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "No se puede encontrar el paciente")
    public ResponseEntity<?> buscarPorPaciente(@PathVariable Long id) throws ResourceNotFoundException{
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorID(id);
        if (pacienteBuscado.isPresent()){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            logger.error("Paciente no encontrado");
            throw new ResourceNotFoundException("Paciente con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/email/{email}")
    @Parameter(description = "Nos buscar un paciente por email")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "No se puede encontrar el paciente")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) throws ResourceNotFoundException{
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorEmail(email);
        if (pacienteBuscado.isPresent()){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            logger.error("Paciente no encontrado");
            throw new ResourceNotFoundException("Paciente con email " + email + " no encontrado");
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con email " + email + " no encontrado");
        }
    }

    @GetMapping("/cedula/{cedula}")
    @Parameter(description = "Nos buscar un paciente por cédula")
    @ApiResponse(responseCode = "200", description = "Paciente encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "No se puede encontrar el paciente")
    public ResponseEntity<?> buscarPorCedula(@PathVariable String cedula) throws ResourceNotFoundException{
        Optional<Paciente> pacienteBuscado = pacienteService.buscarPorCedula(cedula);
        if (pacienteBuscado.isPresent()){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            logger.error("Paciente no encontrado");
            throw new ResourceNotFoundException("Paciente con cedula " + cedula + " no encontrado");
        }
    }

    @PutMapping
    @Operation(summary = "Nos permite actualizar", description = "Enviar paciente con id")
    @ApiResponse(responseCode = "200", description = "Paciente actualizado con exito")
    @ApiResponse(responseCode = "400", description = "Paciente no existe para actualizar")
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente) throws BadRequestException {
        Optional<Paciente> pacienteBuscado= pacienteService.buscarPorID(paciente.getId());
        if(pacienteBuscado.isPresent()){
            pacienteService.actualizarPaciente(paciente);
            return ResponseEntity.ok("Paciente actualizado con éxito");
        }else{
            logger.error("Paciente no encontrado para actualizar");
            throw new BadRequestException("El paciente con ID " + paciente.getId() +
                    " no se encuentra registrado para realizar la actualización");
            //return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    @Operation(summary = "Nos permite eliminar", description = "Enviar id de paciente")
    @ApiResponse(responseCode = "200", description = "Paciente eliminado con exito")
    @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Long id) throws ResourceNotFoundException {
        if (pacienteService.buscarPorID(id).isPresent()){
            pacienteService.eliminarPaciente(id);
            return ResponseEntity.ok().body("Paciente con ID " + id + " eliminado con éxito");
        }else{
            logger.error("Paciente no encontrado");
            throw new ResourceNotFoundException("Paciente no encontrado para eliminar");
        }
    }

}
