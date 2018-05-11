package com.horizon.vpatel.papershares;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void onClickHandler(View view)
    {
        switch (view.getId())
        {
            case R.id.select_stocks_textview:
                Log.d("Start Activity","Select Stocks");
                Intent intent = new Intent(this, StockSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.set_live_wallpaper_textview:
                Log.d("Start Activity", "Set Live Wallpaper");

                Intent i = new Intent();

                i.setAction(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                String pkg = StockGraphWallpaperService.class.getPackage().getName();
                String cls = StockGraphWallpaperService.class.getCanonicalName();
                i.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT, new ComponentName(pkg, cls));
                startActivity(i);
                break;
            case R.id.app_settings:
                Log.d("Start Activity", "App Settings");
                break;
            default: Log.e("UH-OH", "SOMETHING WENT VERY WRONG");
                break;
        }
    }
}