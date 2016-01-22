package com.perimobile.nesty.Entidades;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Apartamento extends Residencial {
    private String edificio;
    private int numApto;

    public Apartamento(long id, Tipo tipo, String endereco, float preco) {
        super(id, tipo, endereco, preco);
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public int getNumApto() {
        return numApto;
    }

    public void setNumApto(int numApto) {
        this.numApto = numApto;
    }
}
