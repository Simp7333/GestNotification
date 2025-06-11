package fr.gestnotification.model;

public class Service {
    private String id;
    private String nom;
    private String description;
    private boolean actif;

    public Service(String id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.actif = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActif() {
        return actif;
    }

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", id, nom, actif ? "Actif" : "Inactif");
    }
} 