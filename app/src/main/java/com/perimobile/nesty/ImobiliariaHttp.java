package com.perimobile.nesty;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.perimobile.nesty.Entidades.Imobiliaria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo on 16/12/2015.
 */
public class ImobiliariaHttp {
    public static final String URL_IMOBILIARIA_JSON =
            "http://www.perimobile.com/ws.php?opt=3";

    private static HttpURLConnection conectar(String urlArquivo)throws IOException {
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

    public static Imobiliaria carregarImobiliariaJson(long id) {
        try{
            HttpURLConnection conexao = conectar(URL_IMOBILIARIA_JSON + "&id=" + id);
            int resposta = conexao.getResponseCode();
            if(resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonImobiliaria(json);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesParaString(InputStream is) throws IOException{
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();
        int byteLidos;
        while((byteLidos = is.read(buffer))!= -1) {
            bufferzao.write(buffer, 0 , byteLidos);

        }
        System.out.println(new String(bufferzao.toByteArray(), "UTF-8"));
        return new String(bufferzao.toByteArray(), "UTF-8");



    }

    public static Imobiliaria lerJsonImobiliaria(JSONObject json) throws JSONException{
        JSONArray imobiliariasJson= json.getJSONArray("imobiliaria");
        List<Imobiliaria> imobiliarias = new ArrayList<>();

        for (int i = 0; i < imobiliariasJson.length(); i++) {
            JSONObject imobiliariaJson = imobiliariasJson.getJSONObject(i);
            Imobiliaria imobiliaria = new Imobiliaria(
                    imobiliariaJson.getLong("id"),
                    imobiliariaJson.getString("nome"),
                    imobiliariaJson.getString("logo"),
                    imobiliariaJson.getString("descricao"),
                    imobiliariaJson.getString("msgids"),
                    imobiliariaJson.getInt("telefone"),
                    imobiliariaJson.getString("endereco"),
                    imobiliariaJson.getString("fotoimob"));
            imobiliarias.add(imobiliaria);
        }
        return imobiliarias.size() ==  1 ? imobiliarias.get(0) : null;
    }
}