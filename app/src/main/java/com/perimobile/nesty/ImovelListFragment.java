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
import com.perimobile.nesty.Entidades.Imobiliaria;
import com.perimobile.nesty.Entidades.Imovel;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
            if (ImovelHttp.temConexao(getActivity())){
                startDownload();
            } else {
                mTextMessage.setText("Sem conexão");
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
            return ImovelHttp.carregarImoveisJson();
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
                mTextMessage.setText("Falha ao carregar imoveis");
            }
        }
    }
}