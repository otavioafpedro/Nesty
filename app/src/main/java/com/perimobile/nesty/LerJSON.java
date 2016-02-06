package com.perimobile.nesty;

import com.perimobile.nesty.Entidades.Imobiliaria;
import com.perimobile.nesty.Entidades.Imovel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo on 06/02/2016.
 */
public interface LerJSON {
    Object lerJsonTarget(JSONObject json) throws JSONException;
}

class LerImobiliaria implements LerJSON {
    @Override
    public Object lerJsonTarget(JSONObject json) throws JSONException {
        JSONArray imobiliariasJson = json.getJSONArray("imobiliaria");
        List<Imobiliaria> imobiliarias = new ArrayList<>();

        for (int i = 0; i < imobiliariasJson.length(); i++) {
            JSONObject imobiliariaJson = imobiliariasJson.getJSONObject(i);
            Imobiliaria imobiliaria = new Imobiliaria(
                    imobiliariaJson.getLong(Imobiliaria.ID),
                    imobiliariaJson.getString(Imobiliaria.NOME),
                    imobiliariaJson.getString(Imobiliaria.LOGO),
                    imobiliariaJson.getString(Imobiliaria.DESC),
                    imobiliariaJson.getString(Imobiliaria.MSGIDS),
                    imobiliariaJson.getInt(Imobiliaria.TEL),
                    imobiliariaJson.getString(Imobiliaria.ENDER),
                    imobiliariaJson.getString(Imobiliaria.FOTOIMOB));
            imobiliarias.add(imobiliaria);
        }
        return imobiliarias.size() == 1 ? imobiliarias.get(0) : null;
    }
}

class LerImovelCompleto implements LerJSON {
    @Override
    public Object lerJsonTarget(JSONObject json) throws JSONException {
        JSONArray imoveisJson = json.getJSONArray("imovel");
        List<Imovel> imoveis = new ArrayList<>();

        for (int i = 0; i < imoveisJson.length(); i++) {
            JSONObject imovelJson = imoveisJson.getJSONObject(i);

            Imovel imovel = new Imovel(
                    imovelJson.getLong(Imovel.ID), imovelJson.getLong(Imovel.IDIMOB),
                    imovelJson.getString(Imovel.BAIRRO), imovelJson.getString(Imovel.OBS),
                    imovelJson.getString(Imovel.ENDERECO), imovelJson.getInt(Imovel.NUMERO), imovelJson.getInt(Imovel.TIPONEGOCIACAO),
                    imovelJson.getInt(Imovel.DESTAQUE),
                    (float) imovelJson.getDouble(Imovel.AREA1), (float) imovelJson.getDouble(Imovel.AREA2),
                    (float) imovelJson.getDouble(Imovel.PRECO), Imovel.Tipo.valueOf(imovelJson.getInt(Imovel.TIPO)),
                    imovelJson.getInt(Imovel.QUARTOS), imovelJson.getInt(Imovel.BWC),
                    imovelJson.getString(Imovel.FOTOIMOVEL), imovelJson.getString(Imovel.VIDEOIMOVEL), imovelJson.getString(Imovel.LOGO));
            imoveis.add(imovel);
        }
        return imoveis.size() == 1 ? imoveis.get(0) : null;
    }
}
class LerImovelResumido implements LerJSON {
    @Override
    public Object lerJsonTarget(JSONObject json) throws JSONException {
        JSONArray imoveisJson = json.getJSONArray("imoveis");
        List<Imovel> imoveis = new ArrayList<>();

        for (int i = 0; i < imoveisJson.length(); i++) {
            JSONObject imovelJson = imoveisJson.getJSONObject(i);
            Imovel imovel = new Imovel(
                    imovelJson.getLong(Imovel.ID),
                    imovelJson.getLong(Imovel.IDIMOB),
                    Imovel.Tipo.valueOf(imovelJson.getInt(Imovel.TIPO)),
                    imovelJson.getString(Imovel.ENDERECO),
                    (float) imovelJson.getDouble(Imovel.PRECO),
                    imovelJson.getString(Imovel.LOGO),
                    imovelJson.getString(Imovel.FOTOIMOVEL),
                    imovelJson.getDouble(Imovel.LAT),
                    imovelJson.getDouble(Imovel.LNG));
            imoveis.add(imovel);
        }
        return imoveis;
    }
}
