package org.helixos.wallpapers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * On boot, if initial setup has not yet completed, starts a service that
 * watches for its completion so the theme-based wallpaper can be applied.
 * If setup is already complete (e.g. a later reboot), does nothing.
 *
 * Note: "user_setup_complete" is the literal Settings.Secure key backing the
 * hidden Settings.Secure.USER_SETUP_COMPLETE constant, which isn't visible
 * under sdk_version=current. Same key, public API access path.
 */
public class BootReceiver extends BroadcastReceiver {
    private static final String USER_SETUP_COMPLETE = "user_setup_complete";

    @Override
    public void onReceive(Context context, Intent intent) {
        int setupComplete = Settings.Secure.getInt(
                context.getContentResolver(),
                USER_SETUP_COMPLETE, 0);
        if (setupComplete == 0) {
            context.startService(new Intent(context, ThemeWallpaperService.class));
        }
    }
}
