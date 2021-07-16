package br.com.separatist.service;

import br.com.separatist.models.Planet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import java.sql.*;
import java.util.*;

@Service
public class PlanetService {
   @Value("${app.datasource.url}")
   private String url;

   @Value("${app.datasource.user}")
   private String user;

   @Value("${app.datasource.password}")
   private String password;

   @Value("${app.datasource.schema}")
   private String schema;

   @Value("${app.datasource.table}")
   private String table;

   @Value("${app.config.base-url}")
   private String baseUrl;


   public Connection connect() throws SQLException {
      return DriverManager.getConnection(url, user, password);
   }

   public Mono<List<Planet>> getPlanetId(int id) {
      List<Planet> planets = new ArrayList<>();

      try(Connection con = connect();
          PreparedStatement ps = con.prepareStatement("SELECT * FROM " + schema + "." + table + " WHERE id = " + id);
          ResultSet result = ps.executeQuery()) {

         while(result.next()) {
            Planet planet = new Planet();
            planet.setId(result.getInt(1));
            planet.setName(result.getString(2));
            planet.setClimate(result.getString(3));
            planet.setTerrain(result.getString(4));
            planet.setMovieAppearances(result.getInt(5));

            planets.add(planet);
         }

      } catch(SQLException e) {
         e.printStackTrace();
      }
      return Mono.just(planets);
   }

   public Mono<List<Planet>> getPlanetName(String name) {
      List<Planet> planets = new ArrayList<>();

      try(Connection con = connect();
          PreparedStatement ps = con.prepareStatement("SELECT * FROM " + schema + "." + table + " WHERE name like '" + name +"'");
          ResultSet result = ps.executeQuery()) {

         while(result.next()) {
            Planet planet = new Planet();
            planet.setId(result.getInt(1));
            planet.setName(result.getString(2));
            planet.setClimate(result.getString(3));
            planet.setTerrain(result.getString(4));
            planet.setMovieAppearances(result.getInt(5));

            planets.add(planet);
         }

      } catch(SQLException e) {
         e.printStackTrace();
      }
      return Mono.just(planets);
   }

   public Mono<List<Planet>> getAllPlanets() {
      List<Planet> planets = new ArrayList<>();

      try(Connection con = connect();
           PreparedStatement ps = con.prepareStatement("SELECT * FROM " + schema + "." + table + "");
           ResultSet result = ps.executeQuery()) {

         while(result.next()) {
            Planet planet = new Planet();
            planet.setId(result.getInt(1));
            planet.setName(result.getString(2));
            planet.setClimate(result.getString(3));
            planet.setTerrain(result.getString(4));
            planet.setMovieAppearances(result.getInt(5));

            planets.add(planet);
         }

      } catch(SQLException e) {
         e.printStackTrace();
      }
      return Mono.just(planets);
   }

   public Mono<List<Map<String, Object>>> getAllPlanetsApi() {

      final String url = baseUrl + "planets/";

      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(url, String.class);
      JSONObject jsonObject = new JSONObject(result);
      JSONArray jsonPlanets = jsonObject.getJSONArray("results");

      String planets = jsonPlanets.toString();
      List<Map<String, Object>> response = null;

      try {
         response = new ObjectMapper().readValue(planets, new TypeReference<List<Map<String, Object>>>(){});
      } catch (JsonProcessingException e) {
         e.printStackTrace();
      }

      return Mono.just(response);
   }

   public Mono<Integer> getMovieAppearances(String name) {

      final String url = baseUrl + "planets/?search=" + name;
      int movieAppearances = 0;

      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(url, String.class);
      JSONObject jsonObject = new JSONObject(result);
      JSONArray planets = jsonObject.getJSONArray("results");
      JSONArray films = null;
      for(int i=0; i<planets.length(); i++) {
         JSONObject planet = planets.getJSONObject(i);
         if(planet.getString("name").toLowerCase().equals(name)){
            films = planet.getJSONArray("films");
         }
      }
      if(films != null){
         movieAppearances = films.length();
      }

      return Mono.just(movieAppearances);
   }

   public Mono<Planet> savePlanet(Planet planet) {
      Mono<Integer> movieAppearances = getMovieAppearances(planet.getName());
      movieAppearances.subscribe(planet::setMovieAppearances);

      String query = "INSERT INTO " + schema + "." + table + "(name, climate, terrain, movie_appearances) VALUES(?, ?, ?, ?) RETURNING id";

      try (Connection con = connect();
           PreparedStatement ps = con.prepareStatement(query)) {

         ps.setString(1, planet.getName());
         ps.setString(2, planet.getClimate());
         ps.setString(3, planet.getTerrain());
         ps.setInt(4, planet.getMovieAppearances());
         ResultSet result = ps.executeQuery();

         while(result.next()) {
            planet.setId(result.getInt(1));
         }

      } catch(SQLException e) {
         e.printStackTrace();
      }
      return Mono.just(planet);
   }

   public Mono<Integer> deletePlanet(int id) {

      int rowsAffected = 0;
      String query = "DELETE FROM " + schema + "." + table + " WHERE id = ?";

      try (Connection con = connect();
           PreparedStatement ps = con.prepareStatement(query)) {
         ps.setInt(1, id);
         ps.executeUpdate();

      } catch(SQLException e) {
         e.printStackTrace();
      }

      return Mono.just(rowsAffected);
   }
}
