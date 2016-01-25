package com.perimobile.nesty.Entidades;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Residencial extends Imovel {

    protected int garagem;

    public Residencial(long id, long idImob, String bairro, String observacao, String endereco, int numero, int tipoNegociacao, int destaque, float area1, float area2, float preco, Tipo tipo, int quartos, int bwc, String imgPrincipal, String video, String logoImob, int garagem) {
        super(id, idImob, bairro, observacao, endereco, numero, tipoNegociacao, destaque, area1, area2, preco, tipo, quartos, bwc, imgPrincipal, video, logoImob);
        this.garagem = garagem;
    }

    public int getGaragem() {
        return garagem;
    }

    public void setGaragem(int garagem) {
        this.garagem = garagem;
    }
}
