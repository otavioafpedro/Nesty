package com.perimobile.nesty;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.perimobile.nesty.Entidades.Imovel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImovelDetalhe extends AppCompatActivity implements View.OnClickListener {

    NetworkImageView fotoImovel, imgImob;
    TextView tvPreco, tvTipo, tvEndereco, tvObs, tvArea1, tvArea2, tvQuartos, tvBWC, tvGaragem, tvEdificio, tvNapto;
    private TextView mTextMessage;

    private long idImov;
    Imovel mImovel;
    private ImovelTask mTask;
    private ImageLoader mLoader;

    String URLBase = "http://www.perimobile.com/img";

    HttpJson imovelHTTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imovel_detalhe);
        mLoader = PatternVolley.getInstance(this).getImageLoader();

        fotoImovel = (NetworkImageView) findViewById(R.id.fotoImovel);
        tvPreco = (TextView) findViewById(R.id.txtPreco);
        tvTipo = (TextView) findViewById(R.id.txtTipo);

        imgImob = (NetworkImageView) findViewById(R.id.imgImob);
        imgImob.setOnClickListener(this);
        tvEndereco = (TextView) findViewById(R.id.txtEndereco);
        tvObs = (TextView) findViewById(R.id.txtObs);
        tvArea1= (TextView) findViewById(R.id.txtarea1);
        tvArea2 = (TextView) findViewById(R.id.txtarea2);
        tvQuartos = (TextView) findViewById(R.id.txtquartos);
        tvBWC = (TextView) findViewById(R.id.txtbwc);
        tvGaragem = (TextView) findViewById(R.id.txtgaragem);
        tvEdificio = (TextView) findViewById(R.id.txtEdificio);
        tvNapto = (TextView) findViewById(R.id.txtNApart);
        Button lgruteis = (Button) findViewById(R.id.lgruteis);
        lgruteis.setOnClickListener(this);

        Intent it = getIntent();
        idImov = it.getLongExtra(Principal.IDIMOV, -1);

        imovelHTTP = new HttpJson("http://www.perimobile.com/ws.php?opt=2","&id=" + idImov){
            @Override
            Object lerJsonTarget(JSONObject json) throws JSONException {
                JSONArray imoveisJson = json.getJSONArray("imovel");
                List<Imovel> imoveis = new ArrayList<>();

                for (int i = 0; i < imoveisJson.length(); i++) {
                    JSONObject imovelJson = imoveisJson.getJSONObject(i);

                    Imovel imovel = new Imovel(
                            imovelJson.getLong(Imovel.ID),
                            imovelJson.getLong(Imovel.IDIMOB), imovelJson.getString(Imovel.BAIRRO), imovelJson.getString(Imovel.OBS),
                            imovelJson.getString(Imovel.ENDERECO), imovelJson.getInt(Imovel.NUMERO), imovelJson.getInt(Imovel.TIPONEGOCIACAO),
                            imovelJson.getInt(Imovel.DESTAQUE), (float) imovelJson.getDouble(Imovel.AREA1),
                            (float) imovelJson.getDouble(Imovel.AREA2), (float) imovelJson.getDouble(Imovel.PRECO),

                            Imovel.Tipo.valueOf(imovelJson.getInt(Imovel.TIPO)),
                            imovelJson.getString(Imovel.FOTOIMOVEL), imovelJson.getString(Imovel.VIDEOIMOVEL), imovelJson.getString(Imovel.LOGO));
                    imoveis.add(imovel);

                }
                return imoveis.size() == 1 ? (Imovel) imoveis.get(0) : null;
            }
        };
        if (mTask == null) {
            mTask = new ImovelTask();
            mTask.execute();
            if (HttpJson.temConexao(this)){
                //startDownload();
            } else {
                mTextMessage.setText("Sem conexão");
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            //showProgress(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgImob:
                Intent it = new Intent(this, ImobiliariaDetalhe.class);
                it.putExtra(Principal.IDIMOB, mImovel.getImob().getId());
                startActivity(it);
                break;
            case R.id.lgruteis:
                Intent iti = new Intent(this, LugaresUteis.class);
                iti.putExtra(Principal.IDIMOV, mImovel.getId());
                startActivity(iti);
                break;
        }
    }

    class ImovelTask extends AsyncTask<Void, Void, Imovel> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress(true);
        }

        @Override
        protected Imovel doInBackground(Void... strings) {
            //Aqui devemos usar para receber os dados do bd

            return (Imovel) imovelHTTP.loadTargetJson(idImov);
        }

        @Override
        protected void onPostExecute(Imovel imovel) {
            super.onPostExecute(imovel);

            //showProgress(false);
            if (imovel != null) {
                mImovel = imovel;

                tvEndereco.setText(mImovel.getEndereco());
                tvPreco.setText(String.valueOf(mImovel.getPreco()));
                fotoImovel.setImageUrl(URLBase + imovel.getImgPrincipal(), mLoader);
                imgImob.setImageUrl(URLBase + imovel.getImob().getLogo(), mLoader);
                tvTipo.setText(mImovel.getTipo());
                tvObs.setText(mImovel.getObservacao());
                tvArea1.setText(String.valueOf(mImovel.getArea1()));
                tvArea2.setText(String.valueOf(mImovel.getArea2()));
            } else {
                mTextMessage.setText("Falha ao carregar Imovel");
            }
        }
    }
    public void playVideo(View v) throws IOException {

    }
}