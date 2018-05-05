package com.horizon.vpatel.papershares;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class cardViewActivityTest extends AppCompatActivity {

    StockInfo stockInfo;

    //GET RID OF THIS LATER
    String symbols[] = {"MSFT", "TSLA", "AAPL", "GOOG"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_info_card);

        stockInfo = new StockInfo(new String[] {"MSFT", "TSLA", "AAPL", "GOOG"});
        setupCards();

        try
        {
            wait((long)500);
        }
        catch (Exception e)
        {
            Log.d("Interrupted", "End yourself");
        }
    }

    void setupCards()
    {
        LinearLayout rootLinearLayout = findViewById(R.id.stock_select_linear_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        Double prices[] = {93.12, 1006.7, 128.45, 89.32};//stockInfo.getPrice();


        for (int i = 0; i < 4; i++) {
            CardView currentStockInfoCard = (CardView) layoutInflater.inflate(R.layout.card_template, (RelativeLayout)findViewById(R.id.testing), false);
            //CardView currentStockInfoCard = (CardView) layoutInflater.

            LinearLayout holder = (LinearLayout) currentStockInfoCard.getChildAt(0);

            AppCompatTextView symbol =  (AppCompatTextView) holder.getChildAt(0);
            AppCompatTextView price =  (AppCompatTextView) holder.getChildAt(2);

            symbol.setText(symbols[i]);
            price.setText(String.format("%f", prices[i]));

            rootLinearLayout.addView(currentStockInfoCard);
        }
    }
}
