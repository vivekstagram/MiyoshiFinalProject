package com.horizon.vpatel.papershares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.Rect;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.os.Handler;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class StockGraphWallpaperService extends WallpaperService {

    private boolean mVisible;
    Canvas canvas;
    int Drawspeed = 60;
    Context mcontext;
    String theChosenOne;

    Double[] theRawData = new Double[60];

    //The information for the graph
    private GraphView toDraw;

    private DataPoint pricesToDraw[] = new DataPoint[60];

    private LineGraphSeries<DataPoint> currentGraphSeries = new LineGraphSeries<DataPoint>();


    public void onCreate()
    {
        super.onCreate();

        //Set up some properties of the graph
        currentGraphSeries.setThickness(8);
        currentGraphSeries.setDataPointsRadius(2.0f);
        currentGraphSeries.setDrawDataPoints(true);
        currentGraphSeries.setAnimated(true);

        toDraw = new GraphView(getApplicationContext()); //Instantiate
        toDraw.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        toDraw.getGridLabelRenderer().setVerticalLabelsVisible(false);
        toDraw.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        toDraw.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " $";
                }
            }
        });
        toDraw.getViewport().setMinX(0); toDraw.getViewport().setMaxX(59);
        toDraw.getViewport().setScalable(true);
        toDraw.getGridLabelRenderer().setLabelsSpace(100);

        StockInfo.getTimeSeries(new OnPriceUpdated() {
            @Override
            public void onPriceUpdated(Double[] queryPrices) {
                theRawData = queryPrices;
            }
        });

        toDraw.addSeries(currentGraphSeries);
    }

    public void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    public Engine onCreateEngine() {
        return new StockWallpaperEngine();
    }

    public class StockWallpaperEngine extends Engine
    {
        private final Handler mHandler = new Handler();

        //the tread responsible for drawing this thread get calls every time
        private final Runnable mDrawFrame = new Runnable() {
            @Override
            public void run() {
                StockInfo.getTimeSeries(new OnPriceUpdated() {
                    @Override
                    public void onPriceUpdated(Double[] queryPrices) {
                        theRawData = queryPrices;
                        drawFrame();
                    }
                });
            }
        };

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            drawFrame();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mVisible = false;
            mHandler.removeCallbacks(mDrawFrame);
        }


        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawFrame);
            }
        }

        //called when surface destroyed
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawFrame);
        }

        private void drawFrame()
        {
            final SurfaceHolder holder = getSurfaceHolder();

            canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    drawGraph();
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }

            mHandler.removeCallbacks(mDrawFrame);
            if (mVisible) {
                // set the execution delay
                mHandler.postDelayed(mDrawFrame, Drawspeed * 1000);
            }
        }

        private void drawGraph()
        {
            for (int i = 0; i < theRawData.length; i++)
            {
                pricesToDraw[i] = new DataPoint(i, theRawData[i]);
            }

            toDraw.setBackgroundColor(getColor(R.color.colorPrimary));

            currentGraphSeries.setColor(getColor(R.color.colorAccent));

            currentGraphSeries.resetData(pricesToDraw);

            toDraw.measure(canvas.getWidth(), canvas.getHeight());
            toDraw.layout(0, 0, canvas.getWidth(), canvas.getHeight());
            toDraw.addSeries(currentGraphSeries);

            toDraw.draw(canvas);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            mHandler.removeCallbacks(mDrawFrame);
            if (mVisible) {
                // set the execution delay
                mHandler.postDelayed(mDrawFrame, Drawspeed);
            }
        }
    }
}
