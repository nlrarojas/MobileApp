package com.melodicmusic.mobileapp.controller;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melodicmusic.mobileapp.model.Product;
import com.melodicmusic.pruebas.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Nelson on 4/9/2017.
 */

public class AdapterListView extends BaseAdapter {
    private List<Product> products;
    private Context context;
    private double dolarExchange;
    private boolean shoppingCartDisplayed;
    private boolean setButton;
    private List<Product> shoppingCart;
    private Fragment fragment;
    private String userId;

    public AdapterListView(List<Product> products, Context context, double dolarExchange) {
        this.products = products;
        this.context = context;
        this.dolarExchange = dolarExchange;
        this.shoppingCartDisplayed = false;
        this.setButton = false;
    }

    public AdapterListView(List<Product> products, Context context, double dolarExchange, Fragment fragment) {
        this.products = products;
        this.context = context;
        this.fragment = fragment;
        this.dolarExchange = dolarExchange;
        this.shoppingCartDisplayed = false;
        this.setButton = false;
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
        TextView productDollarPrice = (TextView) convertView.findViewById(R.id.textViewDolarPrice);
        TextView productColonPrice = (TextView) convertView.findViewById(R.id.textViewColonPrice);
        ImageView productImage = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        TextView productDescription = (TextView) convertView.findViewById(R.id.textViewDescription);
        TextView productBrand = (TextView) convertView.findViewById(R.id.textViewBrand);

        productName.setText(products.get(position).getName());
        productDollarPrice.setText(String.format( "$ %.2f",(products.get(position).getPrice() / dolarExchange)));
        productColonPrice.setText(String.format( "¢ %.2f",(products.get(position).getPrice())));
        Picasso.with(context).load(products.get(position).getImageUrl()).into(productImage);
        productDescription.setText(products.get(position).getDescription());
        productBrand.setText("Marca: " + products.get(position).getBrand());

        if(isShoppingCartDisplayed() && !setButton){
            Button finishSaleBtn = (Button)fragment.getView().findViewById(R.id.btnProcessSale);
            finishSaleBtn.setAllCaps(false);
            finishSaleBtn.setVisibility(View.VISIBLE);
            finishSaleBtn.setBackgroundColor(Color.parseColor("#FBA919"));
            finishSaleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Llamar al método para consumir el web services de venta
                }
            });

            setButton = true;
        }
        return convertView;
    }

    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "," + calendar.get(Calendar.MONTH) + "," + calendar.get(Calendar.DAY_OF_MONTH) + "," +
                calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
    }

    public boolean isShoppingCartDisplayed() {
        return shoppingCartDisplayed;
    }

    public void setShoppingCartDisplayed(boolean shoppingCartDisplayed) {
        this.shoppingCartDisplayed = shoppingCartDisplayed;
    }

    public List<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(List<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
