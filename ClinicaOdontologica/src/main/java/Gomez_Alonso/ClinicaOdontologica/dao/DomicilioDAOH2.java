package Gomez_Alonso.ClinicaOdontologica.dao;


import Gomez_Alonso.ClinicaOdontologica.model.Domicilio;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DomicilioDAOH2 implements iDao<Domicilio>{
    private static final Logger logger= Logger.getLogger(DomicilioDAOH2.class);
    private static final String SQL_SELECT_ONE="SELECT * FROM DOMICILIOS WHERE ID=?";
    private static final String SQL_INSERT="INSERT INTO DOMICILIOS (CALLE, NUMERO,LOCALIDAD, PROVINCIA) VALUES(?,?,?,?)";
    private static final String SQL_UPDATE="UPDATE  DOMICILIOS SET CALLE=?,NUMERO=?, LOCALIDAD=?, PROVINCIA =? WHERE ID=?";
    private static final String SQL_DELETE = "DELETE FROM DOMICILIOS WHERE ID=?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM DOMICILIOS";

    @Override
    public Domicilio guardar(Domicilio domicilio) {
        logger.info("iniciando las operaciones de guardado de un domicilio con id: "+domicilio.getId());
        Connection connection=null;
        try{
            connection=BD.getConnection();
            PreparedStatement psInsert= connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            psInsert.setString(1, domicilio.getCalle());
            psInsert.setInt(2,domicilio.getNumero());
            psInsert.setString(3, domicilio.getLocalidad());
            psInsert.setString(4, domicilio.getProvincia());
            psInsert.execute();
            ResultSet clave= psInsert.getGeneratedKeys();
            while(clave.next()){
                domicilio.setId(clave.getInt(1));
            }
            logger.info("Domicilio guardado con éxito");

        }catch (Exception e){
            logger.error(e.getMessage());
        }

        return domicilio;
    }

    @Override
    public Domicilio buscarPorId(Integer id) {
        logger.info("Iniciando la busqueda de un domicilio con id: "+id);
        Connection connection= null;
        Domicilio domicilio= null;
        try{
            connection= BD.getConnection();
            PreparedStatement psSelectOne= connection.prepareStatement(SQL_SELECT_ONE);
            psSelectOne.setInt(1,id);
            ResultSet rs= psSelectOne.executeQuery();
            while (rs.next()){
                domicilio= new Domicilio(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return domicilio;
    }

    @Override
    public void eliminar(Integer id) {
        logger.warn("Iniciando operación de eliminación de domicilio con id: " + id);
        try (Connection connection = BD.getConnection();
             PreparedStatement psDelete = connection.prepareStatement(SQL_DELETE)) {
            psDelete.setInt(1, id);
            int filasAfectadas = psDelete.executeUpdate();
            if (filasAfectadas == 0) {
                logger.error("No se encontró ningún domicilio con id: " + id);
            } else {
                logger.info("Domicilio con id " + id + " eliminado correctamente.");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void actualizar(Domicilio domicilio) {
        logger.info("iniciando la actualizacion del domicilio con id: "+domicilio.getId());
        Connection connection= null;
        try{
            connection= BD.getConnection();
            PreparedStatement psUpdate= connection.prepareStatement(SQL_UPDATE);
            psUpdate.setString(1, domicilio.getCalle());
            psUpdate.setInt(2,domicilio.getNumero());
            psUpdate.setString(3, domicilio.getLocalidad());
            psUpdate.setString(4, domicilio.getProvincia());
            psUpdate.setInt(5,domicilio.getId());
            psUpdate.execute();

        }catch (Exception e){
            logger.error("Error al actualizar el domicilio con ID " + domicilio.getId() + ": " + e.getMessage());
        }

    }

    @Override
    public List<Domicilio> buscarTodos() {
        logger.info("Iniciando las operaciones de busqueda de domicilios");
        List<Domicilio> domicilios = new ArrayList<>();
        try (Connection connection = BD.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                Domicilio domicilio = new Domicilio(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                domicilios.add(domicilio);
            }
        } catch (Exception e) {
            logger.error("Error al listar los domicilios: " + e.getMessage());
        }
        return domicilios;
    }

    @Override
    public Domicilio buscarPorString(String campo,String valor) {
        return null;
    }


}
