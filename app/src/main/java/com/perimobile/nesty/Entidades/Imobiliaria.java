package com.perimobile.nesty.Entidades;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Imobiliaria {
    public static final String ID = "id";
    public static final String NOME = "nome";
    public static final String LOGO = "logo";
    public static final String DESC = "descricao";
    public static final String MSGIDS = "msgids";
    public static final String TEL = "telefone";
    public static final String ENDER = "endereco";
    public static final String FOTOIMOB = "fotoimob";
    private long id;

    public long getId() {
        return id;
    }
    private String nome;
    private String logo;
    private String descricao;
    private String endereco;
    private String fotoImob;

    public void setId(long id) {
        this.id = id;
    }

    public Imobiliaria(long id) {
        this.id = id;
    }

    public Imobiliaria(long id, String nome, String logo, String descricao, String msgIndisponivel, int telefone, String endereco, String fotoImob) {
        this.id = id;
        this.nome = nome;
        this.logo = logo;
        this.descricao = descricao;
        this.msgIndisponivel = msgIndisponivel;
        this.telefone = telefone;
        this.endereco = endereco;
        this.fotoImob = fotoImob;
    }


    public String getFotoImob() {
        return fotoImob;
    }

    public void setFotoImob(String fotoImob) {
        this.fotoImob = fotoImob;
    }

    public String getEndereco() {return endereco;}
    public void setEndereco(String endereco) {this.endereco = endereco;}

    private String msgIndisponivel;
    private int telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMsgIndisponivel() {
        return msgIndisponivel;
    }

    public void setMsgIndisponivel(String msgIndisponivel) {
        this.msgIndisponivel = msgIndisponivel;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }
}