package org.helixos.wallpapers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Exists only so PackageManager can resolve this package as the partner
 * customization provider via queryBroadcastReceivers(). This broadcast is
 * never actually sent by anything, so onReceive is never invoked.
 */
public class PartnerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // No-op.
    }
}
