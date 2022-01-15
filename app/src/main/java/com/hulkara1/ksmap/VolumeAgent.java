package com.hulkara1.ksmap;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class VolumeAgent extends Service {

    DatabaseHelper databaseHelper;
    VolumeProfile activeVolumeProfile;
    AudioManager audioManager;

    static final int VOLUME_AGENT_SERVICE_ID = 175;
    static final String ACTION_START_VOLUME_AGENT_SERVICE = "startVolumeAgentService";
    static final String ACTION_STOP_VOLUME_AGENT_SERVICE = "stopVolumeAgentService";

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                Log.e("Location", "Location: " + latitude + ", " + longitude);
                //Log.e("Location2", databaseHelper.getVolumeProfileById(1).toString());

                VolumeProfile currentLocationAndTimeVolumeProfile = databaseHelper.getVolumeProfileForLocationAndCurrentTime(latitude, longitude);

                activeVolumeProfile = databaseHelper.getActiveVolumeProfile();

                if (audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == activeVolumeProfile.getMedia() &&
                        audioManager.getStreamVolume(AudioManager.STREAM_ALARM) == activeVolumeProfile.getAlarm() &&
                        audioManager.getStreamVolume(AudioManager.STREAM_RING) == activeVolumeProfile.getRinging() &&
                        audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL) == activeVolumeProfile.getCall() &&
                        audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM) == activeVolumeProfile.getSystem() &&
                        audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) == activeVolumeProfile.getNotification()) {

                    if (currentLocationAndTimeVolumeProfile != null) {

                        activeVolumeProfile = currentLocationAndTimeVolumeProfile;
                        activeVolumeProfile.setInUse(true);
                        databaseHelper.saveActiveVolumeProfile(activeVolumeProfile);
                        setupVolume(activeVolumeProfile, audioManager);

                    } else {
                        activeVolumeProfile = databaseHelper.getDefaultVolumeProfile();
                        activeVolumeProfile.setInUse(true);
                        databaseHelper.saveActiveVolumeProfile(activeVolumeProfile);
                        setupVolume(activeVolumeProfile, audioManager);
                    }

                } else {
                    if (currentLocationAndTimeVolumeProfile == null) {
                        activeVolumeProfile.setMedia(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
                        activeVolumeProfile.setAlarm(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
                        activeVolumeProfile.setSystem(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));
                        activeVolumeProfile.setNotification(audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION));
                        activeVolumeProfile.setCall(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL));
                        activeVolumeProfile.setRinging(audioManager.getStreamVolume(AudioManager.STREAM_RING));
                        activeVolumeProfile.setInUse(false);
                        activeVolumeProfile.setName(null);
                        activeVolumeProfile.setLatitude(latitude);
                        activeVolumeProfile.setLongitude(longitude);
                        activeVolumeProfile.setRadius(0);
                        activeVolumeProfile.setFromMin(0);
                        activeVolumeProfile.setToMin(1440);
                        activeVolumeProfile.setMonday(true);
                        activeVolumeProfile.setTuesday(true);
                        activeVolumeProfile.setThursday(true);
                        activeVolumeProfile.setWednesday(true);
                        activeVolumeProfile.setFriday(true);
                        activeVolumeProfile.setSaturday(true);
                        activeVolumeProfile.setSunday(true);
                        databaseHelper.addRow(activeVolumeProfile);
                        activeVolumeProfile.setInUse(true);
                        databaseHelper.saveActiveVolumeProfile(activeVolumeProfile);
                        activeVolumeProfile.setId(databaseHelper.getVolumeProfileId(activeVolumeProfile));

                    } else {
                        activeVolumeProfile.setRinging((activeVolumeProfile.getRinging() + currentLocationAndTimeVolumeProfile.getRinging())/2);
                        activeVolumeProfile.setMedia((activeVolumeProfile.getMedia() + currentLocationAndTimeVolumeProfile.getMedia())/2);
                        activeVolumeProfile.setAlarm((activeVolumeProfile.getAlarm() + currentLocationAndTimeVolumeProfile.getAlarm())/2);
                        activeVolumeProfile.setSystem((activeVolumeProfile.getSystem() + currentLocationAndTimeVolumeProfile.getSystem())/2);
                        activeVolumeProfile.setCall((activeVolumeProfile.getCall() + currentLocationAndTimeVolumeProfile.getCall())/2);
                        activeVolumeProfile.setNotification((activeVolumeProfile.getNotification() + currentLocationAndTimeVolumeProfile.getNotification())/2);
                        setupVolume(activeVolumeProfile, audioManager);
                        activeVolumeProfile.setInUse(false);
                        databaseHelper.updateRow(activeVolumeProfile);
                        activeVolumeProfile.setInUse(true);
                        databaseHelper.saveActiveVolumeProfile(activeVolumeProfile);
                    }
                }
            }
        }
    };

    private static void setupVolume(VolumeProfile volumeProfile, AudioManager audioManager) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeProfile.getMedia(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volumeProfile.getNotification(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volumeProfile.getAlarm(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volumeProfile.getCall(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volumeProfile.getSystem(), 0);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, volumeProfile.getRinging(), 0);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startLocationService() {
        String channelId = "volume_agent_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Volume agent service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("běží...");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId, "Volume agent service", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription("Channel for volume agent service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            LocationServices.getFusedLocationProviderClient(VolumeAgent.this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        startForeground(VOLUME_AGENT_SERVICE_ID, builder.build());
    }

    private void stop() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(ACTION_START_VOLUME_AGENT_SERVICE)) {
                    databaseHelper = new DatabaseHelper(VolumeAgent.this);
                    activeVolumeProfile = databaseHelper.getActiveVolumeProfile();
                    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    startLocationService();
                    //Log.e("Location2", "Location: " + LocationServices.getFusedLocationProviderClient(this).getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).getResult().getLongitude());
                } else if (action.equals(ACTION_STOP_VOLUME_AGENT_SERVICE)) {
                    stop();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
