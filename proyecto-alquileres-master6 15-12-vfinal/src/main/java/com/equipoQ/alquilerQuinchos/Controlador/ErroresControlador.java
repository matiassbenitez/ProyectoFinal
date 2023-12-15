package com.equipoQ.alquilerQuinchos.Controlador;
import javax.servlet.http.HttpServletRequest;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErroresControlador {

    @Bean
    public ErrorPageRegistrar errorPageRegistrar() {
        return registry -> {
            registry.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/error"));
            registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error"));
            registry.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error"));
            registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error"));
            registry.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error"));
        };
    }
    private int getErrorCode(HttpServletRequest httpRequest) {
    Integer statusCode = (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
    return statusCode != null ? statusCode : 500; // Default to 500 if status code is null
}
}