package org.helixos.wallpapers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * On boot, if initial setup has not yet completed, starts a service that
 * watches for its completion so the theme-based wallpaper can be applied.
 * If setup is already complete (e.g. a later reboot), does nothing.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int setupComplete = Settings.Secure.getInt(
                context.getContentResolver(),
                Settings.Secure.USER_SETUP_COMPLETE, 0);
        if (setupComplete == 0) {
            context.startService(new Intent(context, ThemeWallpaperService.class));
        }
    }
}
