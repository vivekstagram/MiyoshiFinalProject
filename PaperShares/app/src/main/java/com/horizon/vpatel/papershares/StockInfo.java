package com.horizon.vpatel.papershares;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class StockInfo {

    //The symbol that this stock trades under
    private String symbol;

    //The current price of this stock
    private double price;


    //Symbol doesn't change, doesn't need a setter method
    public String getSymbol() {
        return symbol;
    }

    //Returns the current price
    public double getPrice() {

        try {
            NetworkQueryHandler n = new NetworkQueryHandler();
            price = n.execute("https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=MSFT,FB,AAPL,AMZN,TSLA,GOOG,HPQ&apikey=2WYOTXHOURLLD9BD").get();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return price;
    }

    //Sets the price via a float parameter.
    //  That float will be determined by a query handler class.
    //  But for right now it will handle the querying by itself.
    public void setPrice(String url) throws IOException, JSONException {
        //this.price = price;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }


    class NetworkQueryHandler extends AsyncTask<String, Void, Double>
    {
        public Double doInBackground(String... url)
        {
            try {
                InputStream is = new URL(url[0]).openStream();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);

                    JSONArray _prices = json.getJSONArray("Stock Quotes");

                    Double _price = Double.parseDouble(_prices.getJSONObject(0).getString("2. price"));

                    Log.d("PriceDebug", "" + _price);
                    is.close();

                    return _price;
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
            }
            catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            return -420.69;
        }
    }

}
