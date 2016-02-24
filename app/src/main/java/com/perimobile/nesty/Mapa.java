package com.perimobile.nesty;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.perimobile.nesty.Entidades.Imovel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class Mapa extends AppCompatActivity implements LocationListener, OnMapReadyCallback, OnInfoWindowClickListener {

    LatLng mOrigem;
    private ImovelTask mTask;
    private List<Imovel> imoveis;
    GoogleMap mGoogleMap;
    HttpJson imovelHTTPmapa;
    private HashMap<String, Integer> mMarkers;
    private Long id;
    private Context ctx;
    //private ImageLoader mLoader;
    private String FullURL = null;
    private String URLImg = "http://www.perimobile.com/img";

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

        mMarkers = new HashMap<String, Integer>();

        this.ctx = this;
        //mLoader = PatternVolley.getInstance(ctx).getImageLoader();

        imovelHTTPmapa = new HttpJson(Principal.URLBASE + "?opt=prev", "");
        imovelHTTPmapa.leitura = new LerImovelResumido();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Se o Google Play não estiver ativo

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Se o Google Play estiver ativo.

            // Pegando referencia do SupportMapFragment para a acitvity_map.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            fm.getMapAsync(this);
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
            imoveis = is;
            Resources res = getResources();
            for (int i = 0; i < imoveis.size(); i++) {

                String preco = String.format(res.getString(R.string.preco), imoveis.get(i).getPreco());
                id = imoveis.get(i).getId();
                Integer obj = (int) (long) id;
                MarkerOptions mo = new MarkerOptions()
                        .position(new LatLng(imoveis.get(i).getLat(), imoveis.get(i).getLng()))
                        .title(String.valueOf(preco))
                        .snippet(imoveis.get(i).getEndereco())
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                Marker mkr = mGoogleMap.addMarker(mo);
                mMarkers.put(mkr.getId(), obj);
            }

            // Possibilitando a minha localização no GoogleMap.
            /*if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mGoogleMap.setMyLocationEnabled(true);*/

            // Set listeners for marker events.  See the bottom of this class for their behavior.

        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Colocar o contexto e fazer a intent do imovel detalhe começar.
        //Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();
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

        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.setOnInfoWindowClickListener(this);
        mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());


        if (mTask == null) {
            mTask = new ImovelTask();
            mTask.execute();
            if (HttpJson.temConexao(this)) {
                //startDownload();
            } else {
                //mTextMessage.setText(R.string.semconexao);
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            //showProgress(true);
        }

    }

    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        private final View mWindow;

        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            /*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null;
            }*/
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            /*if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }*/
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            String casap = "/casap.jpg";
            int azulnesty = getResources().getColor(R.color.azulnesty);
            int vermelhonesty = getResources().getColor(R.color.vescuronesty);

            /*for (int i = 0; i < imoveis.size(); i++) {
                int mkr = mMarkers.get(marker.getId());
                if (imoveis.get(i).getId() == mkr) {
                    FullURL = (URLImg + imoveis.get(i).getImgPrincipal());
                } else {
                    FullURL = (URLImg + casap);
                }
            }

            ImageView badge = (ImageView) findViewById(R.id.badge);
            Picasso.with(ctx).load(Uri.parse(FullURL)).resize(250,250).into(badge);*/

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(azulnesty), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(vermelhonesty), 0, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
    }
}

