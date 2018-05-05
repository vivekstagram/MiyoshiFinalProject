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
    String symbols[] = {"MSFT", "TSLA", "AAPL", "GOOG", "HPQ", "AMZN"};

    Double prices[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_info_card);

        stockInfo = new StockInfo(symbols);
        setupCards();
    }

    void setupCards()
    {
        LinearLayout rootLinearLayout = findViewById(R.id.stock_select_linear_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        stockInfo.getPrice(new OnPriceUpdated() {
            @Override
            public void onPriceUpdated(Double[] queryPrices) {
                prices = queryPrices;

                LinearLayout rootLinearLayout = findViewById(R.id.stock_select_linear_layout);

                for (int i = 0; i < prices.length; i++) {
                    CardView currentStockInfoCard = (CardView)rootLinearLayout.getChildAt(i);

                    LinearLayout holder = (LinearLayout) currentStockInfoCard.getChildAt(0);

                    AppCompatTextView price = (AppCompatTextView) holder.getChildAt(2);

                    price.setText(prices[i].toString());
                }
            }
        });


        for (int i = 0; i < symbols.length; i++) {
            CardView currentStockInfoCard = (CardView) layoutInflater.inflate(R.layout.card_template, (RelativeLayout)findViewById(R.id.testing), false);
            //CardView currentStockInfoCard = (CardView) layoutInflater.

            LinearLayout holder = (LinearLayout) currentStockInfoCard.getChildAt(0);

            AppCompatTextView symbol =  (AppCompatTextView) holder.getChildAt(0);
            AppCompatTextView price =  (AppCompatTextView) holder.getChildAt(2);

            symbol.setText(symbols[i]);
            price.setText("Loading...");

            rootLinearLayout.addView(currentStockInfoCard);
        }
    }

    /*
    @Override
    public void onPriceUpdated(Double[] queryPrices)
    {
        prices = queryPrices;

        LinearLayout rootLinearLayout = findViewById(R.id.stock_select_linear_layout);

        for (int i = 0; i < symbols.length; i++) {
            CardView currentStockInfoCard = (CardView)rootLinearLayout.getChildAt(i);

            LinearLayout holder = (LinearLayout) currentStockInfoCard.getChildAt(0);

            AppCompatTextView price = (AppCompatTextView) holder.getChildAt(2);

            price.setText(prices[i].toString());
        }
    }
    */
}