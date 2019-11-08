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

public class AddStudentDialog extends AppCompatDialogFragment {
    EditText et_id;
    EditText et_name;
    EditText et_number;
    StudentListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addstudentdialog,null);
        et_id = view.findViewById(R.id.student_id_et);
        et_name = view.findViewById(R.id.student_name_et);
        et_number = view.findViewById(R.id.student_number_et);
        builder.setView(view).setTitle("Add Student")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String id = et_id.getText().toString();
                String name = et_name.getText().toString();
                String number = et_number.getText().toString();
                listener.ApplyText(id,name,number);//passing id, Number and Name to Activity

            }
        });
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =  (StudentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +"must implement the DepDialogListener");
        }
    }

    public interface StudentListener{
        void ApplyText(String id,String name,String number);
    }
}
