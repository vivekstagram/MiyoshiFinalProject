package com.horizon.vpatel.papershares;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.os.Handler;

import com.jjoe64.graphview.GraphView;

public class StockGraphWallpaperService extends WallpaperService{

    private boolean mVisible;
    Canvas canvas;
    int Drawspeed = 60;
    Context mcontext;

    private Double pricesToDraw[] = new Double[60];


    @Override
    public Engine onCreateEngine() {
        return new StockWallpaperEngine();
    }


    public class StockWallpaperEngine extends Engine
    {
        private final Handler mHandler = new Handler(); // this is to handle the thread

        //the tread responsibe for drawing this thread get calls every time
        // drawspeed vars set the execution speed
        private final Runnable mDrawFrame = new Runnable() {
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

        // remove thread
        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawFrame);
        }


        //called when varaible changed
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
                    // look at this graph!
                    // my draw code

                    Paint numberP = new Paint();
                    numberP.setStyle(Paint.Style.FILL);
                    numberP.setColor(Color.WHITE);
                    canvas.drawPaint(numberP);

                    numberP.setColor(Color.parseColor("#16406f"));
                    canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), numberP);


                    //GraphView v = new GraphView();


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
        }
    }
}
