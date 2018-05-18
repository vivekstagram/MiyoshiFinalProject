package com.horizon.vpatel.papershares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.os.Handler;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StockGraphWallpaperService extends WallpaperService{

    private boolean mVisible;
    Canvas canvas;
    int Drawspeed = 60;
    Context mcontext;


    //The information for the graph
    private GraphView toDraw = new GraphView(getBaseContext());

    private DataPoint pricesToDraw[] = new DataPoint[60];

    private LineGraphSeries<DataPoint> currentGraphSeries = new LineGraphSeries<DataPoint>();


    public void onCreate()
    {
        super.onCreate();
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
                    toDraw.addSeries(currentGraphSeries);
                    toDraw.measure(1440, 2560);
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
