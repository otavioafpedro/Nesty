package com.perimobile.nesty;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

public class Mapa extends AppCompatActivity implements LocationListener {

    final static String IDIMOV = "id";
    final static String IDIMOB = "id_imob";

    LatLng mOrigem;

    GoogleMap mGoogleMap;

    HttpJson imovelHTTP;

    public static final String URL_IMOVEL_JSON =
            "http://www.perimobile.com/ws.php?opt=4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        /*Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar2);

        final AppCompatActivity activity = Mapa.this;
        activity.setSupportActionBar(toolbar2);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(activity, Principal.class);
                startActivity(it);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        imovelHTTP = new HttpJson(URL_IMOVEL_JSON,"") {
            @Override
            List<Imovel> lerJsonTarget(JSONObject json) throws JSONException {
                JSONArray imoveisJson = json.getJSONArray("imoveis");
                List<Imovel> imoveis = new ArrayList<>();
                for (int i = 0; i < imoveisJson.length(); i++) {
                    JSONObject imovelJson = imoveisJson.getJSONObject(i);
                    Imovel imovel = new Imovel(
                            imovelJson.getLong("id"),
                            imovelJson.getLong("id_imob"),
                            (float) imovelJson.getDouble("preco"),
                            (float) imovelJson.getDouble("lat"),
                            (float) imovelJson.getDouble("lng"));
                    mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(imovel.getLat(),imovel.getLng()))
                            .title(String.valueOf(imovel.getPreco())));
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
            mGoogleMap = fm.getMap();

            // Possibilitando a minha localização no GoogleMap.
            mGoogleMap.setMyLocationEnabled(true);

            // Deixando o mapa do tipo satelite.
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

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
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
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

        // Mostrando a localização atual no Googlemaps.
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mOrigem));

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

}
