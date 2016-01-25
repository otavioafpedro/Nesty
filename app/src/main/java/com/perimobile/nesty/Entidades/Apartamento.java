package com.perimobile.nesty.Entidades;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Apartamento extends Residencial {
    private String edificio;
    private int numApto;

    public Apartamento(long id, long idImob, String bairro, String observacao, String endereco,
                       int numero, int tipoNegociacao, int destaque, float area1, float area2,
                       float preco, Tipo tipo, int quartos, int bwc, String imgPrincipal, String video, String logoImob,
                       int garagem, String edificio, int numApto) {

        super(id, idImob, bairro, observacao, endereco, numero, tipoNegociacao, destaque, area1, area2,
                preco, tipo, quartos, bwc, imgPrincipal, video, logoImob, garagem);
        this.edificio = edificio;
        this.numApto = numApto;
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
