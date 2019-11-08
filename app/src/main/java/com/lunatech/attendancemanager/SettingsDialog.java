package com.lunatech.attendancemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SettingsDialog extends AppCompatDialogFragment {

    Switch aSwitch;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_layout,null);
        aSwitch = view.findViewById(R.id.sstp_switch);
        aSwitch.setChecked(loadData());
        builder.setView(view).setTitle("Settings")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveState();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }

    private void saveState() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("bool",aSwitch.isChecked());
        editor.apply();
    }

    private boolean loadData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("bool",false);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loadData();
    }
}
