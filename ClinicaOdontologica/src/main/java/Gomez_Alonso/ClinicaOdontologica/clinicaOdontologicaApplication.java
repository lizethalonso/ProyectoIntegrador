package Gomez_Alonso.ClinicaOdontologica;

import Gomez_Alonso.ClinicaOdontologica.dao.BD;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class clinicaOdontologicaApplication {
    public static void main(String[] args) {
        BD.crearTablas();
        SpringApplication.run(clinicaOdontologicaApplication.class, args);
    }
}
