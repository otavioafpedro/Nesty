package com.perimobile.nesty;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayerView;
import com.perimobile.nesty.Entidades.Imovel;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImovelDetalhe extends YouTubeBaseActivity implements View.OnClickListener, YouTubePlayer.OnInitializedListener {

    NetworkImageView fotoImovel, imgImob;
    TextView tvPreco, tvTipo, tvEndereco, tvObs, tvArea1, tvArea2, tvQuartos, tvBWC, tvGaragem, tvEdificio, tvNapto;
    private TextView mTextMessage;

    private long idImov;
    Imovel mImovel;
    private ImovelTask mTask;
    private ImageLoader mLoader;

    String URLBase = "http://www.perimobile.com/img";

    HttpJson imovelHTTP;

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImovel = null;
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

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);

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
                            imovelJson.getInt(Imovel.QUARTOS), imovelJson.getInt(Imovel.BWC),
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
                mTextMessage.setText(R.string.semconexao);
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

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(mImovel.getVideo()); // Plays https://www.youtube.com/watch?v=pAgnJDJN4VA
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
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
            youTubeView.initialize(Config.YOUTUBE_API_KEY, ImovelDetalhe.this);
            //showProgress(false);
            if (imovel != null) {
                mImovel = imovel;
                Resources res = getResources();
                tvEndereco.setText(mImovel.getEndereco());

                String preco = String.format(res.getString(R.string.preco), String.format("%.02f", mImovel.getPreco()));
                tvPreco.setText(preco);

                fotoImovel.setImageUrl(URLBase + imovel.getImgPrincipal(), mLoader);
                imgImob.setImageUrl(URLBase + imovel.getImob().getLogo(), mLoader);
                tvTipo.setText(mImovel.getTipo());
                tvObs.setText(mImovel.getObservacao());


                String area1 = String.format(res.getString(R.string.area1), mImovel.getArea1());
                tvArea1.setText(area1);

                String area2 = String.format(res.getString(R.string.area2), mImovel.getArea2());
                tvArea2.setText(area2);

                String quartos = String.format(res.getString(R.string.quartos), mImovel.getQuartos());
                tvQuartos.setText(quartos);

                String bwc = String.format(res.getString(R.string.bwc), mImovel.getBwc());
                tvBWC.setText(bwc);

            } else {
                mTextMessage.setText(R.string.loadfail1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}