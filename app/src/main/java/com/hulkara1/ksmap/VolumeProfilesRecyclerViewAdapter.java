package com.hulkara1.ksmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VolumeProfilesRecyclerViewAdapter extends RecyclerView.Adapter<VolumeProfilesRecyclerViewAdapter.ViewHolder> {

    private ArrayList<VolumeProfile> volumeProfiles = new ArrayList<>();

    private final Context context;
    private final AudioManager audioManager;
    private final DatabaseHelper databaseHelper;

    public VolumeProfilesRecyclerViewAdapter(Context context, AudioManager audioManager, DatabaseHelper databaseHelper) {
        this.context = context;
        this.audioManager = audioManager;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volume_profiles_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position;

        boolean active = volumeProfiles.get(pos).isInUse();
        holder.radioButtonActive.setChecked(active);
        if (active) {
            holder.radioButtonActive.setText("Aktivní");
        } else {
            holder.radioButtonActive.setText("Neaktivní");
        }
        holder.radioButtonActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!volumeProfiles.get(pos).isInUse()) {
                    for (int i = 0; i < volumeProfiles.size(); i++) {
                        if (volumeProfiles.get(i).isInUse()) {
                            volumeProfiles.get(i).setInUse(false);
                            databaseHelper.updateRow(volumeProfiles.get(i));
                        }
                    }
                    volumeProfiles.get(pos).setInUse(true);
                    databaseHelper.updateRow(volumeProfiles.get(pos));
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeProfiles.get(pos).getMedia(), 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volumeProfiles.get(pos).getNotification(), 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, volumeProfiles.get(pos).getAlarm(), 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, volumeProfiles.get(pos).getCall(), 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volumeProfiles.get(pos).getSystem(), 0);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, volumeProfiles.get(pos).getRinging(), 0);
                }
                Intent intentHome = new Intent(context, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intentHome);
            }
        });

        holder.txtName.setText(volumeProfiles.get(pos).getName());

        holder.seekBarMedia.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        holder.seekBarMedia.setProgress(volumeProfiles.get(pos).getMedia());
        holder.seekBarMedia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (active) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volumeProfiles.get(pos).setMedia(seekBar.getProgress());
                databaseHelper.updateRow(volumeProfiles.get(pos));
            }
        });

        holder.seekBarAlarm.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM));
        holder.seekBarAlarm.setProgress(volumeProfiles.get(pos).getAlarm());
        holder.seekBarAlarm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (active) {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volumeProfiles.get(pos).setAlarm(seekBar.getProgress());
                databaseHelper.updateRow(volumeProfiles.get(pos));
            }
        });

        holder.seekBarCall.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL));
        holder.seekBarCall.setProgress(volumeProfiles.get(pos).getCall());
        holder.seekBarCall.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (active) {
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volumeProfiles.get(pos).setCall(seekBar.getProgress());
                databaseHelper.updateRow(volumeProfiles.get(pos));
            }
        });

        holder.seekBarNotification.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        holder.seekBarNotification.setProgress(volumeProfiles.get(pos).getNotification());
        holder.seekBarNotification.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (active) {
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volumeProfiles.get(pos).setNotification(seekBar.getProgress());
                databaseHelper.updateRow(volumeProfiles.get(pos));
            }
        });

        holder.seekBarSystem.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        holder.seekBarSystem.setProgress(volumeProfiles.get(pos).getSystem());
        holder.seekBarSystem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (active) {
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volumeProfiles.get(pos).setSystem(seekBar.getProgress());
                databaseHelper.updateRow(volumeProfiles.get(pos));
            }
        });

        holder.seekBarRinging.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_RING));
        holder.seekBarRinging.setProgress(volumeProfiles.get(pos).getRinging());
        holder.seekBarRinging.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (active) {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                volumeProfiles.get(pos).setRinging(seekBar.getProgress());
                databaseHelper.updateRow(volumeProfiles.get(pos));
            }
        });

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getClass() == DeleteActivity.class) {
                    if (volumeProfiles.get(pos).isInUse()) {
                        databaseHelper.saveActiveVolumeProfile(databaseHelper.getDefaultVolumeProfile());
                    }
                    databaseHelper.deleteRow(volumeProfiles.get(pos));
                    Intent intentHome = new Intent(context, DeleteActivity.class);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intentHome);
                }
                if (context.getClass() == EditSelectActivity.class) {
                    Intent intentEdit = new Intent(context, EditActivity.class);
                    intentEdit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", volumeProfiles.get(pos).getId());
                    intentEdit.putExtras(bundle);
                    context.startActivity(intentEdit);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return volumeProfiles.size();
    }

    public void setVolumeProfiles(ArrayList<VolumeProfile> volumeProfiles) {
        this.volumeProfiles = volumeProfiles;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView parent;
        private final RadioButton radioButtonActive;
        private final TextView txtName, txtMedia, txtNotification, txtRinging, txtAlarm, txtSystem, txtCall;
        private final SeekBar seekBarMedia, seekBarNotification, seekBarRinging, seekBarAlarm, seekBarSystem, seekBarCall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            radioButtonActive = itemView.findViewById(R.id.radioButtonActive);
            txtName = itemView.findViewById(R.id.txtName);
            txtMedia = itemView.findViewById(R.id.txtMedia);
            txtNotification = itemView.findViewById(R.id.txtNotification);
            txtRinging = itemView.findViewById(R.id.txtRinging);
            txtAlarm = itemView.findViewById(R.id.txtAlarm);
            txtSystem = itemView.findViewById(R.id.txtSystem);
            txtCall = itemView.findViewById(R.id.txtCall);
            seekBarMedia = itemView.findViewById(R.id.seekBarMedia);
            seekBarNotification = itemView.findViewById(R.id.seekBarNotification);
            seekBarRinging = itemView.findViewById(R.id.seekBarRinging);
            seekBarAlarm = itemView.findViewById(R.id.seekBarAlarm);
            seekBarSystem = itemView.findViewById(R.id.seekBarSystem);
            seekBarCall = itemView.findViewById(R.id.seekBarCall);
        }
    }
}
