package Gomez_Alonso.ClinicaOdontologica.service;

import Gomez_Alonso.ClinicaOdontologica.dao.TurnoDAOLISTA;
import Gomez_Alonso.ClinicaOdontologica.dao.iDao;
import Gomez_Alonso.ClinicaOdontologica.model.Turno;

import java.util.List;

public class TurnoService {
    private iDao<Turno> turnoiDao;

    public TurnoService() {
        turnoiDao= new TurnoDAOLISTA();
    }
    public Turno guardarTurno(Turno turno){
        return turnoiDao.guardar(turno);
    }
    public List<Turno> buscarTodos(){
        return turnoiDao.buscarTodos();
    }
    public Turno buscarPorId(Integer id){
        return turnoiDao.buscarPorId(id);
    }
    public void actualizarTurno(Turno turno) { turnoiDao.actualizar(turno);}
    public void eliminarTurno (Integer id){ turnoiDao.eliminar(id);}

}
