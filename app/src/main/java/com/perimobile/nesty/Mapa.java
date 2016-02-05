package com.perimobile.nesty;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.perimobile.nesty.Entidades.Imovel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mapa extends AppCompatActivity implements LocationListener, OnMapReadyCallback {

    final static String IDIMOV = "id";
    final static String IDIMOB = "id_imob";
    LatLng mOrigem;
    private ImovelTask mTask;
    GoogleMap mGoogleMap;
    private List<Imovel> imoveis;
    HttpJson imovelHTTPmapa;

    public static final String URL_IMOVEL_JSON =
            "http://www.perimobile.com/ws.php?opt=4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Mapa.this, Principal.class));
            }
        });

        imovelHTTPmapa = new HttpJson(URL_IMOVEL_JSON,"") {
            @Override
            List<Imovel> lerJsonTarget(JSONObject json) throws JSONException {
                JSONArray imoveisJson = json.getJSONArray("imoveis");
                imoveis = new ArrayList<>();
                for (int i = 0; i < imoveisJson.length(); i++) {
                    JSONObject imovelJson = imoveisJson.getJSONObject(i);
                    Imovel imovel = new Imovel(
                            imovelJson.getLong("id"),
                            imovelJson.getLong("id_imob"),
                            (float) imovelJson.getDouble("preco"),
                            (float) imovelJson.getDouble("lat"),
                            (float) imovelJson.getDouble("lng"));
                    imoveis.add(imovel);
                }
                return imoveis;
            }
        };

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Se o Google Play não estiver ativo

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Se o Google Play estiver ativo.

            // Pegando referencia do SupportMapFragment para a acitvity_map.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            // Pegando o Objeto GoogleMaps do fragment.
            //mGoogleMap = fm.getMap();
            fm.getMapAsync(this);


            // Deixando o mapa do tipo satelite.
            /*mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // Obtendo o Objeto LocationManager do LOCATION_SERVICE.
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Criando um objetico de criterio para voltar para o provedor.
            Criteria criteria = new Criteria();

            // Conseguindo o nome do melhor provedor.
            String provider = locationManager.getBestProvider(criteria, true);

            // Obtendo a localização atual.
            Location location = locationManager.getLastKnownLocation(provider);

            // Permissões.
            if (location != null) {
                onLocationChanged(location);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);*/
        }
    }

    class ImovelTask extends AsyncTask<Void, Void, List<Imovel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress(true);
        }

        @Override
        protected List<Imovel> doInBackground(Void... strings) {
            //Aqui devemos usar para receber os dados do bd
            return (List<Imovel>) imovelHTTPmapa.loadTargetJson(0);
        }

        @Override
        protected void onPostExecute(List<Imovel> is) {
            super.onPostExecute(is);
            Resources res = getResources();
            for (int i = 0; i < imoveis.size(); i++) {

                String preco = String.format(res.getString(R.string.preco), imoveis.get(i).getPreco());
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(imoveis.get(i).getLat(), imoveis.get(i).getLng()))
                        .title(String.valueOf(preco)));
            }

            // Possibilitando a minha localização no GoogleMap.
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Obtendo a latitude da localização atual.
        double latitude = location.getLatitude();

        // Obtendo a longitude da localização atual.
        double longitude = location.getLongitude();

        // Criando o objeto LatLng da localização atual.
        mOrigem = new LatLng(latitude, longitude);


        // Zoom
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mTask == null) {
            mTask = new ImovelTask();
            mTask.execute();
            if (HttpJson.temConexao(this)){
                //startDownload();
            } else {
                //mTextMessage.setText(R.string.semconexao);
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            //showProgress(true);
        }

    }
}
