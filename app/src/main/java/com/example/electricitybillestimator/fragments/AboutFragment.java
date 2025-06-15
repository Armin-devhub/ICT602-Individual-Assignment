package com.example.electricitybillestimator.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.electricitybillestimator.R;

public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        Button btnGithub = view.findViewById(R.id.btnGitHub);

        btnGithub.setOnClickListener(v -> {
            String url = "https://github.com/Armin-devhub/ICT602-Individual-Assignment.git";  // Change to your actual GitHub URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        return view;
    }
}
