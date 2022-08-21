package com.usa;

import com.usa.controlador.Controlador;
import com.usa.modelo.RepositorioProducto;
import com.usa.vista.VistaAPP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan("com.usa.modelo")
@EnableJdbcRepositories("com.usa.modelo")
public class MisionTicReto5Application {
        
        @Autowired
        RepositorioProducto repositorioProducto;
        
	public static void main(String[] args) {
		//SpringApplication.run(MisionTicReto5Application.class, args);
                SpringApplicationBuilder builder = new SpringApplicationBuilder(MisionTicReto5Application.class);
                builder.headless(false);
                ConfigurableApplicationContext context = builder.run(args);
	}
        
        @Bean
        ApplicationRunner applicationRunner(){
        
            return args -> {
                VistaAPP vista = new VistaAPP();
                Controlador controlador = new Controlador(repositorioProducto,vista);
                // este es el constructor que se hace en el controlador y se le pasa los objetos repositorio y vista
            };
            
        }
}
