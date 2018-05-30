package com.horizon.vpatel.papershares;

import android.os.AsyncTask;
import android.os.Debug;
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

    static String symbols[];

    //The current price of this stock
    private Double prices[];

    public StockInfo(String _symbols[]) {
        symbols = _symbols;
    }

    //Symbol doesn't change, doesn't need a setter method
    public String[] getSymbols() {
        return symbols;
    }

    //Returns the current price
    public Double[] getPrices(OnPriceUpdated o) {

        try {
            BatchQueryHandler n = new BatchQueryHandler(o);
            prices = n.execute("https://www.alphavantage.co/query?function=BATCH_STOCK_QUOTES&symbols=MSFT,TSLA,AAPL,GOOG,HPQ,AMZN,XOM,NVDA,AMD,ADBE,NFLX,TMUS,INTC&apikey=2WYOTXHOURLLD9BD").get();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return prices;
    }

    public Double[] getTimeSeries(OnPriceUpdated o, String symbol) {

        try {
            TimeSeriesQueryHandler n = new TimeSeriesQueryHandler(o);
            prices = n.execute(new String [] {"https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&interval=1min&apikey=2WYOTXHOURLLD9BD", "MSFT"}).get();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return prices;
    }

    //Sets the price via a float parameter.
    //  That float will be determined by a query handler class.
    //  But for right now it will handle the querying by itself.
    public void setPrice(String url) throws IOException, JSONException {
        //this.price = price;
    }

    private static String readAll(Reader rd) throws IOException {
        String sb = "";
        int cp;
        while ((cp = rd.read()) != -1) {
            sb += (char)cp;
        }
        return sb;
    }

    static class BatchQueryHandler extends AsyncTask<String, Void, Double[]> {
        public OnPriceUpdated listener;

        public BatchQueryHandler(OnPriceUpdated listener)
        {
            this.listener = listener;
        }

        public Double[] doInBackground(String... url)
        {
            try {
                InputStream is = new URL(url[0]).openStream();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);


                    JSONArray _prices = json.getJSONArray("Stock Quotes");

                    Double _priceVals[] = new Double[symbols.length];

                    for (int i = 0; i < symbols.length; i++)
                    {
                        _priceVals[i] = Double.parseDouble(_prices.getJSONObject(i).getString("2. price"));
                    }

                    Log.d("PriceDebug", "" + _priceVals.toString());
                    is.close();

                    return _priceVals;
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
            }
            catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            return new Double[]{6.66};
        }

        protected void onPostExecute(Double[] result)
        {
            listener.onPriceUpdated(result);
        }
    }

    static class TimeSeriesQueryHandler extends AsyncTask<String, Void, Double[]> {
        public OnPriceUpdated listener;

        public TimeSeriesQueryHandler(OnPriceUpdated listener)
        {
            this.listener = listener;
        }

        public Double[] doInBackground(String... args)
        {
            try {
                InputStream is = new URL(args[0]).openStream();
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                    String jsonText = readAll(rd);
                    JSONObject json = new JSONObject(jsonText);


                    JSONObject _prices = json.getJSONObject("Time Series (1min)");



                    /*

                    //JSONArray theWorkableValues = new JSONArray(_prices.toString());

                    //Log.e("Parsed stuff lololol", theWorkableValues.toString());

                    Double _priceVals[] = new Double[60];



                    for (int i = 0; i < 60; i++)
                    {
                        _priceVals[i] = Double.parseDouble(theWorkableValues.getJSONObject(i).getString("4. close"));
                    }

                    */
                    //Log.d("PriceDebug", "" + _priceVals.toString());
                    is.close();

                    return _priceVals;
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
            }
            catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            return null;
        }

        protected void onPostExecute(Double[] result) {
            listener.onPriceUpdated(result);
        }
    }

}