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

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class StockGraphWallpaperService extends WallpaperService{

    private boolean mVisible;
    Canvas canvas;
    int Drawspeed = 60;
    Context mcontext;

    Random r;


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
        toDraw.getViewport().setMinX(0); toDraw.getViewport().setMaxX(59);
        toDraw.getViewport().setScalable(true);

        toDraw.getGridLabelRenderer().setLabelsSpace(0);


        r = new Random();
        for (int i = 0; i < pricesToDraw.length; i++)
        {
            pricesToDraw[i] = new DataPoint(i, r.nextDouble() * 69);
            currentGraphSeries.appendData(pricesToDraw[i], false, 60);
        }
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
                drawFrame();
            }
        };


        //Called when the surface is created
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            //call the draw method
            // this is where you must call your draw code
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

        public void drawFrame()
        {
            final SurfaceHolder holder = getSurfaceHolder();

            canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {

                    //Time to refresh the data
                    //Look at this graph

                    toDraw.setBackgroundColor(getColor(R.color.colorPrimary));

                    currentGraphSeries.setColor(getColor(R.color.colorAccent));



                    //Random Dataaaaa

                    for (int i = 0; i < pricesToDraw.length; i++)
                    {
                        pricesToDraw[i] = new DataPoint(i, r.nextDouble() * 69);
                    }
                    currentGraphSeries.resetData(pricesToDraw);


                    toDraw.measure(canvas.getWidth(), canvas.getHeight());
                    toDraw.layout(0, 0, canvas.getWidth(), canvas.getHeight());
                    toDraw.addSeries(currentGraphSeries);
                    toDraw.draw(canvas);
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

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            drawFrame();
        }
    }
}
