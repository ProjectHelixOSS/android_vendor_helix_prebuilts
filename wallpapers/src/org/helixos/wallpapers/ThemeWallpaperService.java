package org.helixos.wallpapers;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;

import java.io.IOException;

/**
 * Watches Settings.Secure "user_setup_complete" for the moment setup
 * finishes, then applies white wallpaper if the user chose Light theme
 * (yellow is already the boot default, so Dark requires no action). Stops
 * itself once done; never runs again after that.
 */
public class ThemeWallpaperService extends Service {

    private static final String USER_SETUP_COMPLETE = "user_setup_complete";

    private ContentObserver mObserver;

    @Override
    public void onCreate() {
        super.onCreate();
        Handler handler = new Handler(getMainLooper());
        mObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                int setupComplete = Settings.Secure.getInt(
                        getContentResolver(),
                        USER_SETUP_COMPLETE, 0);
                if (setupComplete == 1) {
                    applyThemeWallpaper();
                    getContentResolver().unregisterContentObserver(mObserver);
                    stopSelf();
                }
            }
        };
        getContentResolver().registerContentObserver(
                Settings.Secure.getUriFor(USER_SETUP_COMPLETE),
                false, mObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void applyThemeWallpaper() {
        boolean isNight = (getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        if (!isNight) {
            try {
                WallpaperManager.getInstance(this)
                        .setResource(R.drawable.helix_wallpaper_white);
            } catch (IOException e) {
                // Leave the yellow boot default in place on failure.
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mObserver != null) {
            getContentResolver().unregisterContentObserver(mObserver);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
