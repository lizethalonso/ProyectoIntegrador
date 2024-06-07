package Gomez_Alonso.ClinicaOdontologica.controller;

import Gomez_Alonso.ClinicaOdontologica.model.Paciente;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController //para trabajar sin tecnologia de vista
// @Controller<-- es controller pq vamos a usar una tecnologia de vista

@RequestMapping("/pacientes")
public class PacienteController {
    private PacienteService pacienteService;

    public PacienteController() {
        pacienteService= new PacienteService();
    }
    //ahora vienen todos los metodos que nos permitan actuar como intermediarios.
   /* @GetMapping
    public String buscarPacientePorCorreo(Model model, @RequestParam("email") String email){

        Paciente paciente= pacienteService.buscarPorEmail(email);
        model.addAttribute("nombre",paciente.getNombre());
        model.addAttribute("apellido",paciente.getApellido());
        return "index";

        //return pacienteService.buscarPorEmail(email);
    }*/
    @PostMapping //--> nos permite persistir los datos que vienen desde la vista
    public ResponseEntity<?> guardarPaciente(@RequestBody Paciente paciente){
        Paciente pacienteExistente = pacienteService.buscarPorString("cedula", paciente.getCedula());
        if ( pacienteExistente != null){
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
    public ResponseEntity<?> buscarPorPaciente(@PathVariable Integer id){
        Paciente pacienteBuscado = pacienteService.buscarPorID(id);
        if (pacienteBuscado != null){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con ID " + id + " no encontrado");
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorString(@RequestParam String campo, @RequestParam String valor){
        Paciente pacienteBuscado = pacienteService.buscarPorString(campo,valor);
        if (pacienteBuscado != null){
            return ResponseEntity.ok(pacienteBuscado);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Paciente con " + campo + " " + valor + " no encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<String> actualizarPaciente(@RequestBody Paciente paciente){
        Paciente pacienteBuscado= pacienteService.buscarPorID(paciente.getId());
        if(pacienteBuscado!=null){
            pacienteService.actualizarPaciente(paciente);
            return ResponseEntity.ok("Paciente actualizado con éxito");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El paciente con ID " + paciente.getId() +
                    " no se encuentra registrado para realizar la actualización");
            //return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarPaciente(@PathVariable Integer id){
        if (pacienteService.buscarPorID(id) != null){
            pacienteService.eliminarPaciente(id);
            return ResponseEntity.ok().body("Paciente con ID " + id + " eliminado con éxito");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede eliminar un paciente que no existe");
        }
    }

}
