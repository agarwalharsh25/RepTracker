package com.reptracker.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reptracker.android.databinding.ActivityCompletedVisitInfoBinding;
import com.squareup.picasso.Picasso;

public class CompletedVisitInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = CompletedVisitInfoActivity.class.getSimpleName();

    private ActivityCompletedVisitInfoBinding binding;
//    Visit completedVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCompletedVisitInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();

//        completedVisit = intent.getParcelableExtra("Visit");
//
//        if (completedVisit == null)
//            return;
//
//        binding.back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//
//        binding.doctorName.setText(Html.fromHtml(completedVisit.getDoctor().getDoctorName()));
//        binding.doctorDesignation.setText(Html.fromHtml(completedVisit.getDoctor().getDesignation()));
//        binding.doctorDepartment.setText(Html.fromHtml(completedVisit.getDoctor().getDepartment()));
//        binding.contactNumberDoctor.setText(Html.fromHtml(completedVisit.getDoctor().getContactNumber()));
//
//        binding.hospitalName.setText(Html.fromHtml(completedVisit.getDoctor().getHospital().getHospitalName()));
//        binding.hospitalAddress.setText(Html.fromHtml(completedVisit.getDoctor().getHospital().getHospitalAddress()));
//        binding.contactNumberHospital.setText(Html.fromHtml(completedVisit.getDoctor().getHospital().getContactNumber()));
//
//        Picasso.get().load("http://i.imgur.com/DvpvklR.png").fit().into(binding.signature);
//        Picasso.get().load("http://i.imgur.com/DvpvklR.png").fit().into(binding.picture);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        binding.mapBackgroundImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        binding.scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        binding.scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        binding.scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;
                    default:
                        return true;
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        LatLng sydney = new LatLng(28.6121558, 77.2728417);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title(Html.fromHtml(completedVisit.getDoctor().getHospital().getHospitalName()).toString()));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
//        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
