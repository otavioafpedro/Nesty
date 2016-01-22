package com.perimobile.nesty.Entidades;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Residencial extends Imovel {
    private int quartos;
    private int bwc;
    private int garagem;

    public Residencial(long id, Tipo tipo, String endereco, float preco) {
        super(id, tipo, endereco, preco);
    }

    public int getQuartos() {
        return quartos;
    }

    public void setQuartos(int quartos) {
        this.quartos = quartos;
    }

    public int getBwc() {
        return bwc;
    }

    public void setBwc(int bwc) {
        this.bwc = bwc;
    }

    public int getGaragem() {
        return garagem;
    }

    public void setGaragem(int garagem) {
        this.garagem = garagem;
    }
}
