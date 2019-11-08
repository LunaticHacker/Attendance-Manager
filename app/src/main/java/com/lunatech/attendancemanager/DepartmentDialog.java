package com.lunatech.attendancemanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DepartmentDialog extends AppCompatDialogFragment {
    private EditText editText;
    private  DepDialogListener listener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dailog,null);
        editText = view.findViewById(R.id.dep_name);
        builder.setView(view).setTitle("Create New Department")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String dep_name = editText.getText().toString();
                listener.ApplyText(dep_name);

            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =  (DepDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +"must implement the DepDialogListener");
        }
    }

    public interface DepDialogListener{
        void ApplyText(String dep_name);
    }
}
