package Gomez_Alonso.ClinicaOdontologica.dao;


import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.model.Paciente;
import Gomez_Alonso.ClinicaOdontologica.model.Turno;
import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TurnoDAOLISTA implements iDao<Turno>{
    private Logger logger= Logger.getLogger(TurnoDAOLISTA.class);
    private List<Turno> turnos= new ArrayList<>();
    Integer identification=1;
    @Override
    public Turno guardar(Turno turno) {
        logger.info("Iniciando las operaciones de guardado de un turno");
        PacienteDAOH2 pacienteDAOH2= new PacienteDAOH2();
        OdontologoDAOH2 odontologoDAOH2= new OdontologoDAOH2();
        turno.setPaciente(pacienteDAOH2.buscarPorId(turno.getPaciente().getId()));
        turno.setOdontologo(odontologoDAOH2.buscarPorId(turno.getOdontologo().getId()));
        turno.setId(identification);
        turnos.add(turno);
        identification++;
        logger.info("Turno guardado con exito");
        return turno;
    }

    @Override
    public List<Turno> buscarTodos() {
        logger.info("iniciando las operacion de mostrar todos los turnos");
        return turnos;
    }

    @Override
    public Turno buscarPorId(Integer id) {
        logger.info("Iniciando la operación de busqueda por id");
        for (Turno turno : turnos) {
            if(turno.getId().equals(id)){
                return turno;
            }else{
                System.out.println("turno no encontrado");
            }
        }
        return null;
    }

    @Override
    public void actualizar(Turno turno) {
        logger.info("Iniciando actualización del turno");
        PacienteDAOH2 pacienteDAOH2= new PacienteDAOH2();
        OdontologoDAOH2 odontologoDAOH2= new OdontologoDAOH2();
        Paciente paciente = pacienteDAOH2.buscarPorId(turno.getPaciente().getId());
        Odontologo odontologo = odontologoDAOH2.buscarPorId(turno.getOdontologo().getId());

        for (Turno turnoBuscar : turnos) {
            if (paciente == null || odontologo == null){
                logger.error("No se pudo encontrar el paciente u odontólogo con los IDs proporcionados.");
                throw new IllegalArgumentException("Paciente u odontólogo no encontrado");
            }else {
                turnoBuscar.setId(turno.getId());
                turnoBuscar.setPaciente(paciente);
                turnoBuscar.setOdontologo(odontologo);
                turnoBuscar.setFecha(turno.getFecha());
                turnoBuscar.setHora(turno.getHora());
            }
        }
    }

    @Override
    public void eliminar(Integer id) {
        for (Turno turno : turnos) {
            if (turno.getId().equals(id)) {
                turnos.remove(turno);
                break;
            }else {
                System.out.println("Turno con id " + id + " no encontrado");
            }
        }
    }

    @Override
    public Turno buscarPorString(String campo, String string) {
        return null;
    }
}
