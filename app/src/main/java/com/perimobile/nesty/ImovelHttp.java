package com.perimobile.nesty;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.perimobile.nesty.Entidades.Imobiliaria;
import com.perimobile.nesty.Entidades.Imovel;

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
public class ImovelHttp {
    public static final String URL_IMOVEL_JSON =
            "http://www.perimobile.com/ws.php?opt=1";

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

    public static List<Imovel> carregarImoveisJson() {
        try{
            HttpURLConnection conexao = conectar(URL_IMOVEL_JSON);
            int resposta = conexao.getResponseCode();
            if(resposta == HttpURLConnection.HTTP_OK) {
                InputStream is = conexao.getInputStream();
                JSONObject json = new JSONObject(bytesParaString(is));
                return lerJsonImoveis(json);
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

    public static List<Imovel> lerJsonImoveis(JSONObject json) throws JSONException{
        JSONArray imoveisJson = json.getJSONArray("imoveis");
        List<Imovel> imoveis = new ArrayList<>();

        for (int i = 0; i < imoveisJson.length(); i++) {
            JSONObject imovelJson = imoveisJson.getJSONObject(i);
            Imovel imovel = new Imovel(
                    imovelJson.getLong("id"),
                    imovelJson.getLong("id_imob"),
                    Imovel.Tipo.valueOf(imovelJson.getInt("tipo")),
                    imovelJson.getString("endereco"),
                    (float) imovelJson.getDouble("preco"),
                    imovelJson.getString("logo"),
                    imovelJson.getString("foto_imovel"));
            imoveis.add(imovel);
        }
        return imoveis;
    }
}
