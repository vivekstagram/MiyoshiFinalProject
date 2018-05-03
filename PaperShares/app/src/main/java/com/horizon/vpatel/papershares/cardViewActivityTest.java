package com.horizon.vpatel.papershares;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;

public class cardViewActivityTest extends AppCompatActivity {

    StockInfo stockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_info_card);


        stockInfo = new StockInfo();
        AppCompatTextView stock_price = (AppCompatTextView)findViewById(R.id.stock_price);
        String price = "";
        price += stockInfo.getPrice();

        stock_price.setText(price.toCharArray(), 0, price.length());
    }
}
