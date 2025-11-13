package App;

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


@SpringBootApplication
@ComponentScan(basePackages = {"Logica.Servicio", "App"})
@EnableJpaRepositories(basePackages = {"Persistencia.Repositorios"})
@EntityScan(basePackages = {"Logica.Dominio.Entidades" , "Logica.Dominio.State" })
@Order(2)
public class Main implements CommandLineRunner {

    @Autowired
    private GestorUsuario gestorUsuario;

    @Autowired
    private GestorHuesped gestorHuesped;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}