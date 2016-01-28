package com.perimobile.nesty;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.perimobile.nesty.Entidades.Imovel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Otto on 04/12/2015.
 */
public class ImovelListFragment extends Fragment {
    ImoveisTask mTask;
    List<Imovel> mImoveis;
    ListView mListView;
    TextView mTextMessage;
    ProgressBar mProgressBar;
    ImovelAdapter mAdapter;
    HttpJson imovelHTTP;
    public static final String URL_IMOVEL_JSON =
            "http://www.perimobile.com/ws.php?opt=1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
                            Imovel.Tipo.valueOf(imovelJson.getInt("tipo")),
                            imovelJson.getString("endereco"),
                            (float) imovelJson.getDouble("preco"),
                            imovelJson.getString("logo"),
                            imovelJson.getString("foto_imovel"));
                    imoveis.add(imovel);
                }
                return imoveis;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_list_imovel, null);
        mTextMessage = (TextView)layout.findViewById(android.R.id.empty);
        mListView = (ListView)layout.findViewById(R.id.list);
        mListView.setEmptyView(mTextMessage);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mImoveis == null) {
            mImoveis = new ArrayList<>();

        }
        mAdapter = new ImovelAdapter(getActivity(), mImoveis);
        mListView.setAdapter(mAdapter);

        if (mTask == null) {
            if (HttpJson.temConexao(getActivity())){
                startDownload();
            } else {
                mTextMessage.setText(R.string.semconexao);
            }
        } else if (mTask.getStatus() == AsyncTask.Status.RUNNING) {
            //showProgress(true);
        }
    }

    private void startDownload() {

        if (mTask == null || mTask.getStatus() != AsyncTask.Status.RUNNING) {
            RequestQueue requestQueue = PatternVolley.getInstance(getActivity()).getRequestQueue();
            mTask = new ImoveisTask();
            mTask.execute();
        }
    }

    private void showProgress(boolean show) {
        if (show) {
            mTextMessage.setText(R.string.app_name);
        }
        mTextMessage.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    class ImoveisTask extends AsyncTask<Void, Void, List<Imovel>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgress(true);
        }

        @Override
        protected List<Imovel> doInBackground(Void... strings) {
            //Aqui devemos usar para receber os dados do bd
            return (List<Imovel>) imovelHTTP.loadTargetJson(0);
        }

        @Override
        protected void onPostExecute(List<Imovel> imoveis) {
            super.onPostExecute(imoveis);
            //showProgress(false);
            if (imoveis != null) {
                mImoveis.clear();
                mImoveis.addAll(imoveis);
                mAdapter.notifyDataSetChanged();
            } else {
                mTextMessage.setText(R.string.loadfail);
            }
        }
    }
}