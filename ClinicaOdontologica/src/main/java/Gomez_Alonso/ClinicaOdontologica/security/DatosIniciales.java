package Gomez_Alonso.ClinicaOdontologica.security;

import Gomez_Alonso.ClinicaOdontologica.entity.*;
import Gomez_Alonso.ClinicaOdontologica.repository.OdontologoRepository;
import Gomez_Alonso.ClinicaOdontologica.repository.PacienteRepository;
import Gomez_Alonso.ClinicaOdontologica.repository.UsuarioRepository;
import Gomez_Alonso.ClinicaOdontologica.service.OdontologoService;
import Gomez_Alonso.ClinicaOdontologica.service.PacienteService;
import Gomez_Alonso.ClinicaOdontologica.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatosIniciales implements ApplicationRunner {
    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private OdontologoRepository odontologoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String passSinCifrar= "admin";
        String passSinCifrar2= "user";
        String passCifrado=  passwordEncoder.encode(passSinCifrar);
        String passCifrado2=  passwordEncoder.encode(passSinCifrar2);
        Usuario usuario= new Usuario("admin","admin","admin@admin.com",passCifrado, UsuarioRole.ROLE_ADMIN);
        Usuario usuario2 = new Usuario("user","user","user@user.com",passCifrado2, UsuarioRole.ROLE_USER);
        Paciente paciente= new Paciente("Julian", "Callejas", "123", LocalDate.of(2024,6,18),new Domicilio("40",12,"Quibdó","Chocó"),"julian@email.com");
        Paciente paciente1= new Paciente("Camila", "Mesa", "456", LocalDate.of(2024,6,19),new Domicilio("30",21,"Acacías","Meta"),"camila@email.com");
        Odontologo odontologo = new Odontologo("ABC123","Lina","Gómez");
        Odontologo odontologo1 = new Odontologo("DEF234","Beatriz","Giraldo");
        /*PacienteService pacienteService= new PacienteService();
        pacienteService.guardarPaciente(paciente);
        OdontologoService odontologoService = new OdontologoService();
        odontologoService.guardarOdontologo(odontologo);*/

        pacienteRepository.save(paciente);
        odontologoRepository.save(odontologo);
        pacienteRepository.save(paciente1);
        odontologoRepository.save(odontologo1);
        usuarioRepository.save(usuario);
        usuarioRepository.save(usuario2);
    }
}
