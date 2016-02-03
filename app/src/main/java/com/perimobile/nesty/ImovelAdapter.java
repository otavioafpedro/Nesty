package com.perimobile.nesty;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.perimobile.nesty.Entidades.Imovel;

import java.util.List;
/**
 * Created by Otto on 16/12/2015.
 */


import android.widget.RelativeLayout;

/**
 * Created by Otto on 16/12/2015.
 */
public class ImovelAdapter extends BaseAdapter {

    private ImageLoader mLoader;
    Context ctx;
    List<Imovel> imoveis;
    String URLBase = "http://www.perimobile.com/img";
    public ImovelAdapter(Context ctx, List<Imovel> imoveis) {
        mLoader = PatternVolley.getInstance(ctx).getImageLoader();
        this.ctx = ctx;
        this.imoveis = imoveis;
    }

    @Override
    public int getCount() {
        return imoveis.size();
    }

    @Override
    public Object getItem(int position){return imoveis.get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Imovel imovel = imoveis.get(position);
        Resources res = ctx.getResources();
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_imovel, null);

            RelativeLayout item = (RelativeLayout) convertView.findViewById(R.id.item_imovel);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(ctx, ImovelDetalhe.class);
                    it.putExtra(Principal.IDIMOV, imovel.getId());
                    ctx.startActivity(it);

                }
            });
            holder = new ViewHolder();

            holder.txtPreco = (TextView) convertView.findViewById(R.id.txtPreco);
            holder.txtTipo = (TextView) convertView.findViewById(R.id.txtTipo);
            holder.txtEndereco = (TextView) convertView.findViewById(R.id.txtEndereco);
            holder.imgImob = (NetworkImageView) convertView.findViewById(R.id.imgImob);
            holder.imgPrincipal = (NetworkImageView) convertView.findViewById(R.id.imgImovel);

            holder.imgImob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(ctx, ImobiliariaDetalhe.class);
                    it.putExtra(Principal.IDIMOB, imovel.getImob().getId());
                    ctx.startActivity(it);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.imgImob.setImageUrl(URLBase + imovel.getImob().getLogo(), mLoader);
        holder.imgPrincipal.setImageUrl(URLBase + imovel.getImgPrincipal(), mLoader);
        holder.txtEndereco.setText(imovel.getEndereco());
        holder.txtTipo.setText(imovel.getTipo());
        holder.txtPreco.setText(String.format(res.getString(R.string.preco),imovel.getPreco()));



        return convertView;
    }

    static class ViewHolder {
        NetworkImageView imgImob;
        NetworkImageView imgPrincipal;
        TextView txtPreco;
        TextView txtTipo;
        TextView txtEndereco;

    }

}