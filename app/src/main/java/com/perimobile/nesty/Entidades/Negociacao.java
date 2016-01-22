package com.perimobile.nesty.Entidades;

import java.util.ArrayList;

/**
 * Created by Rodrigo on 04/11/2015.
 */
public class Negociacao {
    private int qtdClicks;
    private boolean favoritou;
    private ArrayList<String> chat;

    public int getQtdClicks() {
        return qtdClicks;
    }

    public void setQtdClicks(int qtdClicks) {
        this.qtdClicks = qtdClicks;
    }

    public boolean isFavoritou() {
        return favoritou;
    }

    public void setFavoritou(boolean favoritou) {
        this.favoritou = favoritou;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public void setChat(ArrayList<String> chat) {
        this.chat = chat;
    }
}
