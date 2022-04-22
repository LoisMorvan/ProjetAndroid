package com.example.projetandroid.Modele;

public class Resto {

    private String nomR;
    private String villeR;

    public Resto(String nomR, String villeR) {
        this.nomR = nomR;
        this.villeR = villeR;
    }

    public String getNomR() {
        return nomR;
    }

    public void setNomR(String nomR) {
        this.nomR = nomR;
    }

    public String getVilleR() {
        return villeR;
    }

    public void setVilleR(String villeR) {
        this.villeR = villeR;
    }
}
