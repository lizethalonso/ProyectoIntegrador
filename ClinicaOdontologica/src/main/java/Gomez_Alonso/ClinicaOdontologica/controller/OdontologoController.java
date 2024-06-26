package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.entity.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.exception.BadRequestException;
import Gomez_Alonso.ClinicaOdontologica.exception.NoContentException;
import Gomez_Alonso.ClinicaOdontologica.exception.ResourceNotFoundException;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
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
@RequestMapping("/odontologos")
@Tag(name = "Controller de Odontologos", description = "Permite registrar, eliminar y listar")
public class OdontologoController {
    @Autowired
    private OdontologoService odontologoService;
    private static final Logger logger = Logger.getLogger(OdontologoController.class);

    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    @Operation(summary = "nos permite registrar", description = "enviar odontologo sin id")
    @ApiResponse(responseCode = "200", description = "odontologo creado con exito")
    @ApiResponse(responseCode = "400", description = "Ya existe un odontólogo con la matricula")
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
    @Parameter(description = "nos permite listar todos los odontologos")
    @ApiResponse(responseCode = "200", description = "Lista mostrada con éxito")
    @ApiResponse(responseCode = "204", description = "Lista vacía")
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
    @Parameter(description = "Nos permite buscar un odontólogo por Id")
    @ApiResponse(responseCode = "200", description = "Odontólogo encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "No se puede encontrar el odontologo")
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
    @Parameter(description = "Nos buscar un odontólogo por matrícula")
    @ApiResponse(responseCode = "200", description = "Odontólogo encontrado con éxito")
    @ApiResponse(responseCode = "404", description = "No se puede encontrar el odontologo")
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
    @Operation(summary = "Nos permite actualizar", description = "Enviar odontologo con id")
    @ApiResponse(responseCode = "200", description = "Odontólogo actualizado con exito")
    @ApiResponse(responseCode = "400", description = "Odontólogo no existe para actualizar")
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
    @Operation(summary = "Nos permite eliminar", description = "Enviar id de odontólogo")
    @ApiResponse(responseCode = "200", description = "Odontólogo eliminado con exito")
    @ApiResponse(responseCode = "404", description = "Odontólogo no encontrado")
    public ResponseEntity <String> eliminarOdontologo(@PathVariable Long id) throws ResourceNotFoundException {
        if (odontologoService.buscarPorID(id).isPresent()) {
            odontologoService.eliminarOdontologo(id);
            return ResponseEntity.ok().body("Odontólogo con ID: " + id + " eliminado con éxito");
        } else {
            logger.error("Odontólogo no existe");
            throw new ResourceNotFoundException("Odontólogo no encontrado para eliminar");
        }
    }
}
