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
    private float price;


    //Symbol doesn't change, doesn't need a setter method
    public String getSymbol() {
        return symbol;
    }

    //Returns the current price
    public float getPrice() {

        try {
            NetworkQueryHandler n = new NetworkQueryHandler();
            price = n.execute("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=1min&apikey=2WYOTXHOURLLD9BD").get();
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


    class NetworkQueryHandler extends AsyncTask<String, Void, Float>
    {
        public Float doInBackground(String... url)
        {
            try {
                InputStream is = new URL(url[0]).openStream();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);

                    JSONObject _prices = json.getJSONObject("Time Series (1min)").getJSONObject("");

                    Float _price = Float.parseFloat(_prices.getJSONObject(0).get("1. open").toString());

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

            return (float)-420.69;
        }
    }

}
