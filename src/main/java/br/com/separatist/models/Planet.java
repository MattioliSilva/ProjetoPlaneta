package br.com.separatist.models;

import java.io.Serializable;

public class Planet implements Serializable {
   private int id, movieAppearances, var;
   private String name, climate, terrain;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getClimate() {
      return climate;
   }

   public void setClimate(String climate) {
      this.climate = climate;
   }

   public String getTerrain() {
      return terrain;
   }

   public void setTerrain(String terrain) {
      this.terrain = terrain;
   }

   public int getMovieAppearances() {
      return movieAppearances;
   }

   public void setMovieAppearances(int movieAppearances) {
      this.movieAppearances = movieAppearances;
   }
}
