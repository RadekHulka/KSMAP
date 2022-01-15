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
import android.widget.CheckBox;
import android.widget.EditText;
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

public class EditActivity extends AppCompatActivity {

    // DATABASE
    DatabaseHelper databaseHelper;

    AudioManager audioManager;

    int volumeProfileId;
    VolumeProfile volumeProfile;

    TextView textName;
    SeekBar seekBarMedia, seekBarAlarm, seekBarRinging, seekBarCall, seekBarSystem, seekBarNotification;
    EditText editTextLatitude, editTextLongitude, editTextFromMin, editTextToMin;
    CheckBox checkBoxMonday, checkBoxTuesday, checkBoxWednesday, checkBoxThursday, checkBoxFriday, checkBoxSaturday, checkBoxSunday;
    Button buttonSave;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // DATABASE - inicialization
        databaseHelper = new DatabaseHelper(EditActivity.this);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeProfileId = getIntent().getExtras().getInt("id");
        volumeProfile = databaseHelper.getVolumeProfileById(volumeProfileId);

        textName = findViewById(R.id.txtName);
        textName.setText(volumeProfile.getName());
        seekBarNotification = findViewById(R.id.seekBarNotification);
        seekBarNotification.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        seekBarNotification.setProgress(volumeProfile.getNotification());
        seekBarSystem = findViewById(R.id.seekBarSystem);
        seekBarSystem.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        seekBarSystem.setProgress(volumeProfile.getSystem());
        seekBarCall = findViewById(R.id.seekBarCall);
        seekBarCall.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
        seekBarCall.setProgress(volumeProfile.getCall());
        seekBarAlarm = findViewById(R.id.seekBarAlarm);
        seekBarAlarm.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        seekBarAlarm.setProgress(volumeProfile.getAlarm());
        seekBarMedia = findViewById(R.id.seekBarMedia);
        seekBarMedia.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBarMedia.setProgress(volumeProfile.getMedia());
        seekBarRinging = findViewById(R.id.seekBarRinging);
        seekBarRinging.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        seekBarRinging.setProgress(volumeProfile.getRinging());
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLatitude.setText(Double.toString(volumeProfile.getLatitude()));
        editTextLongitude = findViewById(R.id.editTextLongitude);
        editTextLongitude.setText(Double.toString(volumeProfile.getLongitude()));
        editTextFromMin = findViewById(R.id.editTextFromMin);
        editTextFromMin.setText(volumeProfile.getFromMin() / 60 + ":" + volumeProfile.getFromMin() % 60);
        editTextToMin = findViewById(R.id.editTextToMin);
        editTextToMin.setText(volumeProfile.getToMin() / 60 + ":" + volumeProfile.getToMin() % 60);
        checkBoxMonday = findViewById(R.id.checkBoxMonday);
        checkBoxMonday.setChecked(volumeProfile.isMonday());
        checkBoxTuesday = findViewById(R.id.checkBoxTuesday);
        checkBoxTuesday.setChecked(volumeProfile.isTuesday());
        checkBoxWednesday = findViewById(R.id.checkBoxWednesday);
        checkBoxWednesday.setChecked(volumeProfile.isWednesday());
        checkBoxThursday = findViewById(R.id.checkBoxthursday);
        checkBoxThursday.setChecked(volumeProfile.isThursday());
        checkBoxFriday = findViewById(R.id.checkBoxFriday);
        checkBoxFriday.setChecked(volumeProfile.isFriday());
        checkBoxSaturday = findViewById(R.id.checkBoxSaturday);
        checkBoxSaturday.setChecked(volumeProfile.isSaturday());
        checkBoxSunday = findViewById(R.id.checkSunday);
        checkBoxSunday.setChecked(volumeProfile.isSunday());
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolumeProfile volumeProfile1 = new VolumeProfile(volumeProfile.getId(),volumeProfile.getName(), seekBarMedia.getProgress(), seekBarAlarm.getProgress(), seekBarSystem.getProgress(), seekBarNotification.getProgress(), seekBarCall.getProgress(), seekBarRinging.getProgress(), Double.parseDouble(editTextLatitude.getText().toString()), Double.parseDouble(editTextLongitude.getText().toString()),0,60 * Integer.parseInt(editTextFromMin.getText().toString().split(":")[0]) + Integer.parseInt(editTextFromMin.getText().toString().split(":")[1]),60 * Integer.parseInt(editTextToMin.getText().toString().split(":")[0]) + Integer.parseInt(editTextToMin.getText().toString().split(":")[1]),checkBoxMonday.isChecked(),checkBoxTuesday.isChecked(),checkBoxWednesday.isChecked(),checkBoxThursday.isChecked(),checkBoxFriday.isChecked(),checkBoxSaturday.isChecked(),checkBoxSunday.isChecked(),false );
                databaseHelper.updateRow(volumeProfile1);
                Intent intentHome = new Intent(EditActivity.this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentHome);
            }
        });
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