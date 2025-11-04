package App;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Ajusta "/api/**" según las URLs que encontraste en el Paso 2
        // Si no todas empiezan con /api, puedes poner "/**" para permitir todo
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // El origen de tu app Next.js
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH") // Métodos permitidos
                .allowCredentials(true);
    }
}