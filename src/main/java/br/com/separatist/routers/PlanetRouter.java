package br.com.separatist.routers;

import br.com.separatist.handlers.PlanetHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class PlanetRouter {
   @Bean
   public RouterFunction<ServerResponse> planetsRoute(PlanetHandler planetHandler){
      return RouterFunctions
            .route(GET("/planet/id/{id}").and(accept(MediaType.APPLICATION_JSON))
                  ,planetHandler::getPlanetId)
            .andRoute(GET("/planet/name/{name}").and(accept(MediaType.APPLICATION_JSON))
                  ,planetHandler::getPlanetName)
            .andRoute(GET("/planets").and(accept(MediaType.APPLICATION_JSON))
                  ,planetHandler::getAllPlanets)
            .andRoute(GET("/planets/api/").and(accept(MediaType.APPLICATION_JSON))
                  ,planetHandler::getAllPlanetsApi)
            .andRoute(POST("/planet").and(accept(MediaType.APPLICATION_JSON))
                  ,planetHandler::createPlanet)
            .andRoute(DELETE("/planet/{id}").and(accept(MediaType.APPLICATION_JSON))
                  ,planetHandler::deletePlanet);
   }
}
