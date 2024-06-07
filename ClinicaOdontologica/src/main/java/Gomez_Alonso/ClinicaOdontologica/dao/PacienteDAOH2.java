package Gomez_Alonso.ClinicaOdontologica.dao;

import Gomez_Alonso.ClinicaOdontologica.model.Domicilio;
import Gomez_Alonso.ClinicaOdontologica.model.Paciente;
import Gomez_Alonso.ClinicaOdontologica.model.Odontologo;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAOH2 implements iDao<Paciente> {
    private static final Logger logger= Logger.getLogger(PacienteDAOH2.class);
    private static final String SQL_SELECT_ONE="SELECT * FROM PACIENTES WHERE ID=?";
    private static final String SQL_INSERT="INSERT INTO PACIENTES(NOMBRE, APELLIDO, CEDULA, FECHA_INGRESO, DOMICILIO_ID, EMAIL, ODONTOLOGO_ID) VALUES(?,?,?,?,?,?,?)";
    private static final String SQL_SELECT_ALL="SELECT * FROM PACIENTES";
    //private static final String SQL_SELECT_BY_EMAIL="SELECT * FROM PACIENTES WHERE EMAIL=?";
    //private static final String SQL_ASSOCIATE="INSERT INTO PACIENTE_ODONTOLOGO (ID_PACIENTE, ID_ODONTOLOGO) VALUES (?,?)";
    private static final String SQL_UPDATE="UPDATE PACIENTES SET NOMBRE=?, APELLIDO=?, CEDULA=?, FECHA_INGRESO=?, DOMICILIO_ID=?, EMAIL=?, ODONTOLOGO_ID=? WHERE ID=?";
    private static final String SQL_DELETE="DELETE FROM PACIENTES WHERE ID=?";

    @Override
    public Paciente guardar(Paciente paciente) {
        logger.warn("Iniciando las operaciones de guardado de paciente: " + paciente.getNombre());
        Connection connection= null;
        Odontologo odontologo=null;
        DomicilioDAOH2 daoAux= new DomicilioDAOH2();
        OdontologoDAOH2 daoAux2= new OdontologoDAOH2();
        Domicilio domicilio=  daoAux.guardar(paciente.getDomicilio());
        try{
            connection= BD.getConnection();
            PreparedStatement psInsert= connection.prepareStatement(SQL_INSERT,Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1, paciente.getNombre());
            psInsert.setString(2, paciente.getApellido());
            psInsert.setString(3, paciente.getCedula());
            psInsert.setDate(4, Date.valueOf((paciente.getFechaIngreso())));
            psInsert.setInt(5,domicilio.getId());
            psInsert.setString(6, paciente.getEmail());
            psInsert.setInt(7, paciente.getOdontologo().getId());
            psInsert.execute();
            odontologo= daoAux2.buscarPorId(paciente.getOdontologo().getId());
            paciente.setOdontologo(odontologo);
            ResultSet clave= psInsert.getGeneratedKeys();
        while (clave.next()){
            paciente.setId(clave.getInt(1));
        }
        logger.info("Paciente guardado con éxito: " + paciente.toString());

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return paciente;
    }

    @Override
    public List<Paciente> buscarTodos() {
        logger.info("Iniciando las operaciones de busqueda:");
        Connection connection=null;
        List<Paciente> pacientes= new ArrayList<>();
        Paciente paciente= null;
        Domicilio domicilio=null;
        Odontologo odontologo=null;
        DomicilioDAOH2 daoAux= new DomicilioDAOH2();
        OdontologoDAOH2 daoAux2= new OdontologoDAOH2();
        try{
            connection=BD.getConnection();
            Statement statement= connection.createStatement();
            ResultSet rs= statement.executeQuery(SQL_SELECT_ALL);
            while(rs.next()){
                domicilio= daoAux.buscarPorId(rs.getInt(6));
                odontologo= daoAux2.buscarPorId(rs.getInt(8));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7), odontologo);
                pacientes.add(paciente);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return pacientes;
    }

    @Override
    public Paciente buscarPorId(Integer id) {
        logger.warn("Iniciando la operacion de buscado de un paciente con id : "+id);
        Connection connection= null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        Odontologo odontologo=null;
        try{
            connection= BD.getConnection();
            PreparedStatement psSElectOne= connection.prepareStatement(SQL_SELECT_ONE);
            psSElectOne.setInt(1,id);
            ResultSet rs= psSElectOne.executeQuery();
            DomicilioDAOH2 daoAux= new DomicilioDAOH2();
            OdontologoDAOH2 daoAux2= new OdontologoDAOH2();
            while(rs.next()){
                domicilio= daoAux.buscarPorId(rs.getInt(6));
                odontologo= daoAux2.buscarPorId(rs.getInt(8));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7), odontologo);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return paciente;
    }

    @Override
    public Paciente buscarPorString(String campo, String valor) {
        logger.info("Iniciando la busqueda por : " + campo);
        Connection connection=null;
        Paciente paciente= null;
        Domicilio domicilio= null;
        Odontologo odontologo=null;
        DomicilioDAOH2 daoAux= new DomicilioDAOH2();
        OdontologoDAOH2 daoAux2 = new OdontologoDAOH2();
        try{
            connection=BD.getConnection();
            String SQL_SELECT_BY_STRING="SELECT * FROM PACIENTES WHERE " + campo  + " = ?";
            PreparedStatement psSelectE= connection.prepareStatement(SQL_SELECT_BY_STRING);
            psSelectE.setString(1,valor);
            ResultSet rs= psSelectE.executeQuery();
            while(rs.next()){
                domicilio= daoAux.buscarPorId(rs.getInt(6));
                odontologo= daoAux2.buscarPorId(rs.getInt(8));
                paciente= new Paciente(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getDate(5).toLocalDate(),domicilio,rs.getString(7), odontologo);
                paciente.setOdontologo(odontologo);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            return null;
        }

        return paciente;
    }

    @Override
    public void actualizar(Paciente paciente) {
        logger.warn("Iniciando las operaciones de actualizacion de paciente con id : "+paciente.getId());
        Connection connection= null;
        DomicilioDAOH2 daoAux= new DomicilioDAOH2();
        try{
            connection= BD.getConnection();
            daoAux.actualizar(paciente.getDomicilio());
            PreparedStatement psUpdate= connection.prepareStatement(SQL_UPDATE);
            psUpdate.setString(1, paciente.getNombre());
            psUpdate.setString(2, paciente.getApellido());
            psUpdate.setString(3, paciente.getCedula());
            psUpdate.setDate(4,Date.valueOf(paciente.getFechaIngreso()));
            psUpdate.setInt(5,paciente.getDomicilio().getId());
            psUpdate.setString(6, paciente.getEmail());
            psUpdate.setInt(7,paciente.getOdontologo().getId());
            psUpdate.setInt(8,paciente.getId());
            psUpdate.execute();

        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    @Override
    public void eliminar(Integer id) {
        logger.warn("Iniciando operación de eliminación de paciente con id: " + id);
        Connection connection = null;
        try {
            connection= BD.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE);
            psDelete.setInt(1,id);
            int filasAfectadas = psDelete.executeUpdate();
            if (filasAfectadas == 0){
                logger.error("No se encontró ningún paciente con id: " + id);
            }else {
                connection.commit();
                logger.info("Paciente con id " + id + " eliminado correctamente.");
            }
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
