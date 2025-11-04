package App;

import Logica.Dominio.Entidades.Direccion;
import Logica.Dominio.Entidades.Huesped;
import Logica.Excepciones.CamposObligatoriosException;
import Logica.Excepciones.CredencialesInvalidasException;
import Logica.Excepciones.DocumentoDuplicadoException;
import Logica.Servicio.GestorHuesped;
import Logica.Servicio.GestorUsuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"Logica.Servicio", "App"})
@EnableJpaRepositories(basePackages = {"Persistencia.Repositorios"})
@EntityScan(basePackages = {"Logica.Dominio.Entidades" , "Logica.Dominio.State" })
@Order(2)
public class Main implements CommandLineRunner {

    @Autowired
    private GestorUsuario gestorUsuario;

    // <-- ¡YA NO ESTÁ COMENTADO! ---
    @Autowired
    private GestorHuesped gestorHuesped;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}