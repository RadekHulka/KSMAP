package com.hulkara1.ksmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.service.media.CameraPrewarmService;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // AUDIO MANAGER
    AudioManager audioManager;

    // RECYCLER VIEW
    private RecyclerView volumeProfilesRecyclerView;

    // DATABASE
    DatabaseHelper databaseHelper;

    // VOLUME AGENT
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    private ArrayList permissions;
    private boolean permissionGranted = false;
    final int REQUEST_PERMISSION_CODE = 1000;
    final int MICROPHONE_PERMISSION_CODE = 200;
    final int LOCATION_PERMISSION_CODE = 44;
    ContextWrapper contextWrapper;
    File musicDirectory;
    File tmpFile;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }
        */

        // DATABASE - inicialization
        databaseHelper = new DatabaseHelper(MainActivity.this);

        // AUDIO MANAGER - inicialization
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // RECYCLER VIEW - inicialization & data & adapter
        volumeProfilesRecyclerView = findViewById(R.id.volumeProfilesRecyclerView);
        ArrayList<VolumeProfile> volumeProfiles = new ArrayList<>();
        VolumeProfilesRecyclerViewAdapter adapter = new VolumeProfilesRecyclerViewAdapter(this, audioManager, databaseHelper);

        // DATABASE - empty? --> insert sample data
        if (databaseHelper.getUserDefinedVolumeProfiles().size() == 0) {
            volumeProfiles.add(new VolumeProfile(1, "Výchozí", 8, 4, 4, 4, 4, 4, 0, 0, 0, 0, 1440, true, true, true, true, true, true, true, true));
            volumeProfiles.add(new VolumeProfile(2, "Práce", 0, 7, 4, 7, 5, 5, 35.2, 105.1, 0, 480, 990, true, true, true, true, true, false, false, false));
            volumeProfiles.add(new VolumeProfile(3, "Sport", 15, 7, 0, 0, 5, 7, 40.2, 110.1, 0, 1050, 1140, true, false, true, false, true, false, false, false));
            volumeProfiles.add(new VolumeProfile(4, "Domov", 10, 4, 5, 5, 4, 4, 30.2, 100.1, 0, 0, 1440, true, true, true, true, true, true, true, false));

            boolean add1 = databaseHelper.addRow(volumeProfiles.get(0));
            boolean add2 = databaseHelper.addRow(volumeProfiles.get(1));
            boolean add3 = databaseHelper.addRow(volumeProfiles.get(2));
            boolean add4 = databaseHelper.addRow(volumeProfiles.get(3));

            if (add1 && add2 && add3 && add4)
                Toast.makeText(MainActivity.this, "Databáze úspěšně inicializována!", Toast.LENGTH_LONG).show();
        }

        // RECYCLER VIEW - show data
        volumeProfiles = databaseHelper.getUserDefinedVolumeProfiles();
        adapter.setVolumeProfiles(volumeProfiles);
        volumeProfilesRecyclerView.setAdapter(adapter);
        volumeProfilesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // VOLUME AGENT - check & request permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startVolumeAgent();
        }
    }

    // FILE - tempfile path
    private String getRecordingFilePath() {
        contextWrapper = new ContextWrapper(getApplicationContext());
        musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        tmpFile = new File(musicDirectory, "tempFile.mp3");
        return tmpFile.getPath();
    }

    // MICROPHONE - is mircophone?
    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        }
        return false;
    }

    // MICROPHONE - request permission
    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

    // VOLUME AGENT - request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startVolumeAgent();
            } else {
                Toast.makeText(this, "Práva neudělena", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // VOLUME AGENT - is running?
    private boolean isVolumeAgentRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (VolumeAgent.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    // VOLUME AGENT - start
    private void startVolumeAgent() {
        if (!isVolumeAgentRunning()) {
            Intent intent = new Intent(getApplicationContext(), VolumeAgent.class);
            intent.setAction(VolumeAgent.ACTION_START_VOLUME_AGENT_SERVICE);
            startService(intent);
            Toast.makeText(this,"Volume agent nastartován", Toast.LENGTH_SHORT).show();
        }
    }

    // VOLUME AGENT - stop
    private void stopVolumeAgent() {
        if (isVolumeAgentRunning()) {
            Intent intent = new Intent(getApplicationContext(), VolumeAgent.class);
            intent.setAction(VolumeAgent.ACTION_STOP_VOLUME_AGENT_SERVICE);
            startService(intent);
            Toast.makeText(this,"Volume agent zastaven", Toast.LENGTH_SHORT).show();
        }
    }

    // MENU - create
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // MENU - click
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_menu:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                Intent intentHome = new Intent(this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
                return true;
            case R.id.edit_menu:
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
                Intent intentEdit = new Intent(this, EditActivity.class);
                intentEdit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentEdit);
                return true;
            case R.id.add_menu:
                Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                Intent intentAdd = new Intent(this, AddActivity.class);
                intentAdd.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentAdd);
                return true;
            case R.id.delete_menu:
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                Intent intentDelete = new Intent(this, DeleteActivity.class);
                intentDelete.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentDelete);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}