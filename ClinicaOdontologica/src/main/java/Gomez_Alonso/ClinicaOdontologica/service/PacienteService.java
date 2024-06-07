package Gomez_Alonso.ClinicaOdontologica.service;


import Gomez_Alonso.ClinicaOdontologica.dao.PacienteDAOH2;
import Gomez_Alonso.ClinicaOdontologica.dao.iDao;
import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import Gomez_Alonso.ClinicaOdontologica.model.Paciente;

import java.util.List;

public class PacienteService {
private iDao<Paciente> pacienteiDao;

    public PacienteService() {
        pacienteiDao=new PacienteDAOH2();
    }
    //metodos manuales
    public Paciente guardarPaciente(Paciente paciente){
        return pacienteiDao.guardar(paciente);
    }
    public Paciente buscarPorID(Integer id){
        return pacienteiDao.buscarPorId(id);
    }
    public List<Paciente> buscarTodos(){ return pacienteiDao.buscarTodos();}
    public Paciente buscarPorString(String campo, String string){
        return pacienteiDao.buscarPorString(campo,string);
    }
    public void actualizarPaciente(Paciente paciente){
        pacienteiDao.actualizar(paciente);
    }
    public void eliminarPaciente(Integer id){pacienteiDao.eliminar(id);}
}
