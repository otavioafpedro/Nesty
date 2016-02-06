package com.perimobile.nesty;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.perimobile.nesty.Entidades.Imobiliaria;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class ImobiliariaDetalhe extends AppCompatActivity {
    TextView mTextMessage, tvNome,tvDescricao, tvTelefone, tvEndereco;
    NetworkImageView nivFotoImob, nivLogoImob;
    ImobiliariaTask mTask;
    Imobiliaria mImobiliaria;
    long idImob;
    private ImageLoader mLoader;
    HttpJson imobiliariaHTTP;
    String URLBase = "http://www.perimobile.com/img";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imobiliaria_detalhe);
        mTextMessage = (TextView) findViewById(android.R.id.empty);
        mLoader = PatternVolley.getInstance(this).getImageLoader();


        tvNome = (TextView) findViewById(R.id.tvNome);
        tvDescricao = (TextView) findViewById(R.id.tvDescricao);
        tvTelefone = (TextView) findViewById(R.id.tvTelefone);
        tvEndereco = (TextView) findViewById(R.id.tvEndereco);
        nivFotoImob = (NetworkImageView) findViewById(R.id.fotoImob);
        nivLogoImob = (NetworkImageView) findViewById(R.id.logoImob);

        Intent it = getIntent();
        idImob = it.getLongExtra(Principal.IDIMOB, -1);

        imobiliariaHTTP = new HttpJson(Principal.URLBASE+"?opt=imob", "&id=" + idImob);
        imobiliariaHTTP.leitura = new LerImobiliaria();

        if (mTask == null) {
            mTask = new ImobiliariaTask();
            mTask.execute();
            if (HttpJson.temConexao(this)){
                //startDownload();
            } else {
                mTextMessage.setText(R.string.semconexao);
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            //showProgress(true);
        }

    }

    class ImobiliariaTask extends AsyncTask<Void, Void, Imobiliaria> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress(true);
        }

        @Override
        protected Imobiliaria doInBackground(Void... strings) {
            //Aqui devemos usar para receber os dados do bd

            return (Imobiliaria) imobiliariaHTTP.loadTargetJson(idImob);
        }

        @Override
        protected void onPostExecute(Imobiliaria imobiliaria) {
            super.onPostExecute(imobiliaria);

            //showProgress(false);
            if (imobiliaria != null) {
                mImobiliaria = imobiliaria;

                tvNome.setText(mImobiliaria.getNome());
                tvDescricao.setText(mImobiliaria.getDescricao());
                String tmp = String.valueOf(mImobiliaria.getTelefone());
                String telefone = "(" + tmp.substring(0,2) + ") " +
                        tmp.substring(2,6)+"-"+ tmp.substring(6,10);
                tvTelefone.setText(telefone);
                tvEndereco.setText(String.valueOf(mImobiliaria.getEndereco()));
                nivLogoImob.setImageUrl(URLBase + mImobiliaria.getLogo(), mLoader);
                nivFotoImob.setImageUrl(URLBase + mImobiliaria.getFotoImob(), mLoader);
            } else {
                mTextMessage.setText(R.string.loadfail2);
            }
        }
    }
}