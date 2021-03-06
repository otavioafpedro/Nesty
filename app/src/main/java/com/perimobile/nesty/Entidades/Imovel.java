package com.perimobile.nesty.Entidades;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rodrigo on 04/11/2015.
 */

public class Imovel implements Serializable{
    public static final String ID = "id";
    public static final String IDIMOB = "id_imob";
    public static final String BAIRRO = "bairro";
    public static final String OBS = "observacao";
    public static final String ENDERECO = "endereco";
    public static final String NUMERO = "numero";
    public static final String TIPONEGOCIACAO = "tipo_negociacao";
    public static final String DESTAQUE = "destaque";
    public static final String AREA1 = "area1";
    public static final String AREA2 = "area2";
    public static final String PRECO = "preco";
    public static final String TIPO = "tipo";
    public static final String BWC = "bwc";
    public static final String QUARTOS = "quartos";
    public static final String FOTOIMOVEL = "foto_imovel";
    public static final String VIDEOIMOVEL = "video_imovel";
    public static final String LOGO = "logo";
    public static final String LAT = "lat";
    public static final String LNG = "lng";


    protected String bairro;
    protected String observacao;
    protected Tipo tipo;
    protected String endereco;
    protected int numero;
    protected int quartos;
    protected int bwc;
    protected int tipoNegociacao;         // venda/aluguel
    protected int destaque;
    protected float area1;
    protected float area2;
    protected float preco;
    protected double lat;
    protected double lng;
    protected long id;
    protected Imobiliaria imob;
    protected String imgPrincipal;
    protected Status status;
    protected String video;
    protected String logoImob;


    public Imovel(long id, long idimob, Tipo tipo, String endereco, float preco,
                  String imob, String imgPrincipal, double lat, double lng) {
        this.id = id;
        this.imob = new Imobiliaria(idimob);
        this.tipo = tipo;
        this.endereco = endereco;
        this.preco = preco;
        this.imob.setLogo(imob);
        this.imgPrincipal = imgPrincipal;
        this.lat = lat;
        this.lng = lng;
    }

    public Imovel(long id, long idimob, float preco, double lat, double lng) {
        this.id = id;
        this.imob = new Imobiliaria(idimob);
        this.preco = preco;
        this.lat = lat;
        this.lng = lng;
    }

    public Imovel(long id, long idImob, String bairro, String observacao, String endereco, int numero,
                  int tipoNegociacao, int destaque, float area1, float area2,
                  float preco, Tipo tipo, int quartos, int bwc,  String imgPrincipal, String video, String logoImob) {
        this.imob = new Imobiliaria(idImob);
        this.id = id;
        this.bairro = bairro;
        this.observacao = observacao;
        this.endereco = endereco;
        this.numero = numero;
        this.tipoNegociacao = tipoNegociacao;
        this.destaque = destaque;
        this.area1 = area1;
        this.area2 = area2;
        this.preco = preco;
        this.tipo = tipo;
        this.quartos = quartos;
        this.bwc = bwc;
        this.imgPrincipal = imgPrincipal;
        this.video = video;
        this.imob.setLogo(logoImob);

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

    public enum Status {
        OK, INSERIR, ATUALIZAR, EXCLUIR;
    }
    public enum Tipo {
        Casa(1), Sobrado(2), Kitnet(3), Apartamento(4), Sala(5), Terreno(6), TerrenoC(7), Galp(8), Rural(9);

        private static Map<Integer, Tipo> map = new HashMap<Integer, Tipo>();

        static {
            for(Tipo t : Tipo.values()){
                map.put(t.valor, t);
            }
        }
        private int valor;
        Tipo(int valor) {
            this.valor = valor;
        }

        public static Tipo valueOf(int legNo) {
            return map.get(legNo);
        }
    }

    public Imobiliaria getImob() {return imob;}

    public void setImob(Imobiliaria imob) {this.imob = imob;}

    public String getImgPrincipal() {
        return imgPrincipal;
    }

    public void setImgPrincipal(String imgPrincipal) {
        this.imgPrincipal = imgPrincipal;
    }

    public String getTipo() {return tipo.name();}

    public void setTipo(Tipo tipo) {this.tipo = tipo;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public Status getStatus() {return status;}

    public void setStatus(Status status) {this.status = status;}

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getTipoNegociacao() {
        return tipoNegociacao;
    }

    public void setTipoNegociacao(int tipoNegociacao) {
        this.tipoNegociacao = tipoNegociacao;
    }

    public int getDestaque() {
        return destaque;
    }

    public void setDestaque(int destaque) {
        this.destaque = destaque;
    }

    public float getArea1() {
        return area1;
    }

    public void setArea1(float area1) {
        this.area1 = area1;
    }

    public float getArea2() {
        return area2;
    }

    public void setArea2(float area2) {
        this.area2 = area2;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}