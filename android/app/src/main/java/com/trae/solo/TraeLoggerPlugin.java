package com.trae.solo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "TraeLogger")
public class TraeLoggerPlugin extends Plugin {
    private static final String CHANNEL_ID = "trae_solo_channel";

    @Override
    public void load() {
        super.load();
        createNotificationChannel();
    }

    @PluginMethod
    public void log(PluginCall call) {
        String message = call.getString("message", "");
        Log.i("TRAE_SOLO", message);
        call.resolve();
    }

    @PluginMethod
    public void notify(PluginCall call) {
        String title = call.getString("title", "TRAE SOLO");
        String message = call.getString("message", "");
        
        Context context = getContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Fallback icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        // Try to use app icon if possible
        try {
            int iconId = context.getApplicationInfo().icon;
            if (iconId != 0) {
                builder.setSmallIcon(iconId);
            }
        } catch (Exception e) {
            // Ignore
        }

        if (notificationManager != null) {
            notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        }

        call.resolve();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "TRAE SOLO Notifications";
            String description = "Notifications for AI generation completion";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

