package com.melodicmusic.mobileapp.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.melodicmusic.mobileapp.model.Product;
import com.melodicmusic.pruebas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Nelson on 4/9/2017.
 */

public class AdapterListView extends BaseAdapter {
    private List<Product> products;
    private Context context;
    private double dolarExchange;

    public AdapterListView(List<Product> products, Context context, double dolarExchange) {
        this.products = products;
        this.context = context;
        this.dolarExchange = dolarExchange;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.view_product, null);
        }

        TextView productName = (TextView) convertView.findViewById(R.id.textViewName);
        TextView productDolarPrice = (TextView) convertView.findViewById(R.id.textViewDolarPrice);
        TextView productColonPrice = (TextView) convertView.findViewById(R.id.textViewColonPrice);
        ImageView productImage = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        TextView productDescripcion = (TextView) convertView.findViewById(R.id.textViewDescription);
        TextView productBrand = (TextView) convertView.findViewById(R.id.textViewBrand);

        productName.setText(products.get(position).getName());
        productDolarPrice.setText(String.format( "$ %.2f",(products.get(position).getPrice() / dolarExchange)));
        productColonPrice.setText(String.format( "¢ %.2f",(products.get(position).getPrice())));
        Picasso.with(context).load(products.get(position).getImageUrl()).into(productImage);
        productDescripcion.setText(products.get(position).getDescription());
        productBrand.setText("Marca: " + products.get(position).getBrand());
        return convertView;
    }
}
