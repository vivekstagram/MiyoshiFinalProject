package com.horizon.vpatel.papershares;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class StockSelectActivity extends AppCompatActivity {

    //GET RID OF THIS LATER ///// OR MAYBE NOT
    public String symbols[] = {"MSFT", "TSLA", "AAPL", "GOOG", "HPQ", "AMZN", "XOM", "NVDA", "AMD", "ADBE", "NFLX", "TMUS", "INTC"};

    Double prices[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_info_card);

        Toast.makeText(this, "Tap a share to select it as your live wallpaper", Toast.LENGTH_LONG).show();

        setupCards();
    }

    private void populatePrices()
    {
        LinearLayout rootLinearLayout = findViewById(R.id.stock_select_linear_layout);

        if (prices == null)
            return;

        for (int i = 0; i < prices.length; i++) {
            CardView currentStockInfoCard = (CardView)rootLinearLayout.getChildAt(i);

            LinearLayout holder = (LinearLayout) currentStockInfoCard.getChildAt(0);

            AppCompatTextView price = (AppCompatTextView) holder.getChildAt(2);

            //ehhhh thats a bit jerry rigged
            price.setText(prices[i].toString());
        }
    }

    private void setupCards()
    {
        LinearLayout rootLinearLayout = findViewById(R.id.stock_select_linear_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        //dark magic with the layout inflater. Don't ask anything.

        for (int i = 0; i < symbols.length; i++) {
            CardView currentStockInfoCard = (CardView) layoutInflater.inflate(R.layout.card_template,
                    (RelativeLayout)findViewById(R.id.testing), false);

            //Refer to the stock view xml layout for the nesting structure

            LinearLayout holder = (LinearLayout) currentStockInfoCard.getChildAt(0);

            AppCompatTextView symbol =  (AppCompatTextView) holder.getChildAt(0);
            AppCompatTextView price =  (AppCompatTextView) holder.getChildAt(2);

            symbol.setText(symbols[i]);
            price.setText("Loading...");

            rootLinearLayout.addView(currentStockInfoCard);
        }

        //This is what allows getting the prices asynchronously without the entire activity crashing and burning (sometimes)
        StockInfo.getPrices(new OnPriceUpdated() {
            @Override
            public void onPriceUpdated(Double[] queryPrices) {
                prices = queryPrices;
                populatePrices();
            }
        });
    }

    public void onCardClick(View view)
    {
        //Set the tapped share to the intended one for the live wallpaper
        CardView clicked = (CardView)view;
        LinearLayout holder = (LinearLayout) clicked.getChildAt(0);
        AppCompatTextView symbol =  (AppCompatTextView) holder.getChildAt(0);
        StockInfo.chosenSymbol = symbol.getText().toString();
        Toast.makeText(this, "Share of choice set to " + StockInfo.chosenSymbol, Toast.LENGTH_LONG).show();
    }
}