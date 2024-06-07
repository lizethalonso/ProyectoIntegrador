package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {
    private OdontologoService odontologoService;

    public OdontologoController() {
        odontologoService = new OdontologoService();
    }

    /*@GetMapping
    public String getOdontologoById(Model model, @RequestParam("id") Integer id){
        Odontologo odontologo = odontologoService.buscarPorID(id);
        if (odontologo != null) {
            model.addAttribute("numeroMatricula", odontologo.getNumeroMatricula());
        } else {
            model.addAttribute("error", "Odontologo not found");
        }
        return "index";
    }*/

    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    public ResponseEntity<?> guardarOdontologo(@RequestBody Odontologo odontologo){
        //No ponemos numeroMatricula (como el atributo del obj) porque el campo está nombrado en la BD como numero_matricula
        Odontologo odontologoExistente = odontologoService.buscarPorString("numero_matricula",odontologo.getNumeroMatricula());
        if (odontologoExistente != null){
            return ResponseEntity.badRequest().body("Ya existe un odontólogo con matrícula: " + odontologo.getNumeroMatricula());
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
    public ResponseEntity<?> buscarPorOdontologo(@PathVariable Integer id){
        Odontologo odontologoBuscado = odontologoService.buscarPorID(id);
        if (odontologoBuscado != null) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Odontólogo con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorCampo(@RequestParam String campo, @RequestParam String valor){
        Odontologo odontologoBuscado = odontologoService.buscarPorString(campo,valor);
        if (odontologoBuscado != null) {
            return ResponseEntity.ok(odontologoBuscado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Odontólogo con " + campo + " " + valor + " no encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarOdontologo(@RequestBody Odontologo odontologo){
        Odontologo odontologoBuscado= odontologoService.buscarPorID(odontologo.getId());
        if(odontologoBuscado!=null){
            odontologoService.actualizarOdontologo(odontologo);
            return ResponseEntity.ok().body("Odontólogo actualizado con éxito");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Odontólogo con ID " + odontologo.getId() +
                    " no se encuentra registrado para realizar la actualización");
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity <?> eliminarOdontologo(@PathVariable Integer id){
        if (odontologoService.buscarPorID(id) != null) {
            odontologoService.eliminarOdontologo(id);
            return ResponseEntity.ok().body("Odontólogo con ID: " + id + " eliminado con éxito ");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede eliminar un odontólogo que no existe");
        }
    }
}
