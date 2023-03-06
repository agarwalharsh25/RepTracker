package com.reptracker.android.utilities;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reptracker.android.R;
import com.reptracker.android.activities.HomeActivity;
import com.reptracker.android.activities.SplashActivity;

import java.util.ArrayList;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();
    private static int id = 1;

    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            for (Geofence geofence : triggeringGeofences) {
                sendNotification(geofence.getRequestId(), context);
                Log.i(TAG, "receivedNotification: " + geofence.getRequestId());

                GeofencingClient geofencingClient = LocationServices.getGeofencingClient(context);
                List<String> geofenceList = new ArrayList<>();
                geofenceList.add(geofence.getRequestId());

                geofencingClient.removeGeofences(geofenceList)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "Successfully removed geofence");
                                // Geofences removed
                                // ...
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Failed to remove geofences", e);
                                // Failed to remove geofences
                                // ...
                            }
                        });
            }
        }

    }

    private void sendNotification(String resourceId, Context context) {
        Log.i(TAG, "sendNotification");

        Intent resultIntent = new Intent(context, HomeActivity.class);
        resultIntent.putExtra("pendingVisitId", resourceId);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, SplashActivity.CHANNEL_ID)
                .setSmallIcon(R.drawable.background_splash)
                .setContentTitle("Reached the destination")
                .setContentText("Click to go back to the app and start presentation")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id, builder.build());
        id++;
    }
}