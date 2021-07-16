package br.com.separatist.handlers;

import br.com.separatist.models.Planet;
import br.com.separatist.service.PlanetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class PlanetHandler {
   @Autowired
   private PlanetService planetService;
   static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

   public Mono<ServerResponse> getPlanetId(ServerRequest serverRequest) {
      int id = Integer.parseInt(serverRequest.pathVariable("id"));
      return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(planetService.getPlanetId(id), Planet.class);
   }

   public Mono<ServerResponse> getPlanetName(ServerRequest serverRequest) {
      String name = serverRequest.pathVariable("name");
      return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(planetService.getPlanetName(name), Planet.class);
   }

   public Mono<ServerResponse> getAllPlanets(ServerRequest serverRequest) {
      return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(planetService.getAllPlanets(), List.class);
   }

   public Mono<ServerResponse> getAllPlanetsApi(ServerRequest serverRequest) {
      return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(planetService.getAllPlanetsApi(), List.class);
   }

   public Mono<ServerResponse> createPlanet(ServerRequest serverRequest) {
      Mono<Planet> planetToToSave = serverRequest.bodyToMono(Planet.class);
      return planetToToSave.flatMap(planet ->
            ServerResponse.ok()
                  .contentType(MediaType.APPLICATION_JSON)
                  .body(planetService.savePlanet(planet), Planet.class));
   }

   public Mono<ServerResponse> deletePlanet(ServerRequest serverRequest) {
      String id = serverRequest.pathVariable("id");
      Mono<Integer> deleteItem = planetService.deletePlanet(Integer.parseInt(id));
      return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(deleteItem, Void.class);
   }
}
