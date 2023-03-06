package com.reptracker.android.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.reptracker.android.databinding.ActivityVisitWorkflowBinding;
import com.reptracker.android.fragments.VisitHospitalInfoFragment;

public class VisitWorkflowActivity extends AppCompatActivity {

    private ActivityVisitWorkflowBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVisitWorkflowBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (savedInstanceState != null) {
            return;
        }

        VisitHospitalInfoFragment visitHospitalInfoFragment = new VisitHospitalInfoFragment();
        visitHospitalInfoFragment.setArguments(getIntent().getExtras());

        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.visitWorkflowFrame.getId(), visitHospitalInfoFragment)
                .commit();

    }

}
