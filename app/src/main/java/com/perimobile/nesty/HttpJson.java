package com.perimobile.nesty;

/**
 * Created by Rodrigo on 15/01/2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

abstract class HttpJson {
    static protected String URL_TARGET_JSON;
    static protected String parametros;

    public HttpJson(String URL, String parms) {
        URL_TARGET_JSON = URL;
        parametros = parms;
    }

    private static HttpURLConnection conectar(String urlArquivo) throws IOException {
        final int SEGUNDOS = 1000;
        URL url = new URL(urlArquivo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

        conexao.setRequestMethod("GET");
        conexao.setConnectTimeout(15 * SEGUNDOS);
        conexao.setReadTimeout(10 * SEGUNDOS);
        conexao.setDoInput(true);
        conexao.setDoOutput(false);
        conexao.connect();
        return conexao;
    }

    public static boolean temConexao(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    abstract Object lerJsonTarget(JSONObject json) throws JSONException;


    public Object loadTargetJson(long id) {
        try{
            HttpURLConnection conexao = conectar(URL_TARGET_JSON + parametros);
            int resposta = conexao.getResponseCode();
            if(resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonTarget(json);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String bytesParaString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int byteLidos;
        while((byteLidos = is.read(buffer))!= -1) {
            bufferzao.write(buffer, 0 , byteLidos);

        }
        System.out.println(new String(bufferzao.toByteArray(), "UTF-8"));
        return new String(bufferzao.toByteArray(), "UTF-8");

    }
}

