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

public class EditSelectActivity extends AppCompatActivity {

    // AUDIO MANAGER
    AudioManager audioManager;

    // RECYCLER VIEW
    private RecyclerView volumeProfilesRecyclerView;

    // DATABASE
    DatabaseHelper databaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_select);

        // DATABASE - inicialization
        databaseHelper = new DatabaseHelper(EditSelectActivity.this);

        // AUDIO MANAGER - inicialization
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // RECYCLER VIEW - inicialization & data & adapter
        volumeProfilesRecyclerView = findViewById(R.id.volumeProfilesRecyclerView);
        ArrayList<VolumeProfile> volumeProfiles = new ArrayList<>();
        VolumeProfilesRecyclerViewAdapter adapter = new VolumeProfilesRecyclerViewAdapter(this, audioManager, databaseHelper);

        // RECYCLER VIEW - show data
        volumeProfiles = databaseHelper.getUserDefinedVolumeProfiles();
        adapter.setVolumeProfiles(volumeProfiles);
        volumeProfilesRecyclerView.setAdapter(adapter);
        volumeProfilesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Intent intentEditSelect = new Intent(this, EditSelectActivity.class);
                intentEditSelect.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentEditSelect);
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