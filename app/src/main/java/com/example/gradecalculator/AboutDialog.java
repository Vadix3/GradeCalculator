package com.example.gradecalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class AboutDialog extends DialogFragment {

    private TextView okText;
    private TextView version;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_dialog, container, false);
        okText = (TextView) view.findViewById(R.id.about_TXT_okText);
        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
        version = (TextView) view.findViewById(R.id.versionTitle);
        version.setText(MainActivity.versionNum);
        return view;
    }
}
