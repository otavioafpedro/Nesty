package com.perimobile.nesty.Entidades;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Funcionario {
    private String nome;
    private String email;
    private String msgIndisponivel;
    private int telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMsgIndisponivel() {
        return msgIndisponivel;
    }

    public void setMsgIndisponivel(String msgIndisponivel) {
        this.msgIndisponivel = msgIndisponivel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }
}
