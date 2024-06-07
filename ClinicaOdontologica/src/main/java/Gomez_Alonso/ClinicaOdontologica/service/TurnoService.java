package Gomez_Alonso.ClinicaOdontologica.service;

import Gomez_Alonso.ClinicaOdontologica.entity.Turno;
import Gomez_Alonso.ClinicaOdontologica.repository.TurnoRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    public Turno guardarTurno(Turno turno){
        return turnoRepository.save(turno);
    }
    public List<Turno> buscarTodos(){ return turnoRepository.findAll(); }
    public Optional<Turno> buscarPorId(Long id){
        return turnoRepository.findById(id);
    }
    public void actualizarTurno(Turno turno) { turnoRepository.save(turno);}
    public void eliminarTurno (Long id){ turnoRepository.deleteById(id);}

}
