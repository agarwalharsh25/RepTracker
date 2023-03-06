package com.reptracker.android.fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.reptracker.android.R;
import com.reptracker.android.activities.HomeActivity;
import com.reptracker.android.adapters.VisitsRecyclerViewAdapter;
import com.reptracker.android.databinding.FragmentVisitHospitalInfoBinding;
import com.reptracker.android.models.PendingVisit;
import com.reptracker.android.utilities.DBHandler;
import com.reptracker.android.utilities.GeofenceBroadcastReceiver;
import com.reptracker.android.utilities.Utility;

import java.util.ArrayList;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class VisitHospitalInfoFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = VisitHospitalInfoFragment.class.getSimpleName();
    private Utility utility = new Utility();

    private FragmentVisitHospitalInfoBinding binding;
    private PendingVisit pendingVisit;
    private boolean reachedHospital = false;

    private static final int REQUEST_PERMISSION_CODE = 100;

    private GeofencingClient geofencingClient;
    private ArrayList<Geofence> geofenceList;
    private PendingIntent geofencePendingIntent;

    public VisitHospitalInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pendingVisit = getArguments().getParcelable("PendingVisit");
            reachedHospital = getArguments().getBoolean("reachedHospital");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentVisitHospitalInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (checkPermissions() && getActivity() != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                String [] permissions = {ACCESS_FINE_LOCATION, ACCESS_BACKGROUND_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_PERMISSION_CODE);
            } else {
                String [] permissions = {ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_PERMISSION_CODE);
            }
        }

        utility.setTextView(binding.hospitalName, pendingVisit.getHospitalName());
        utility.setTextView(binding.hospitalAddress, pendingVisit.getAddress());
        utility.setTextView(binding.contactNumber, pendingVisit.getContact());

        RecyclerView doctorVisitsRecyclerView = binding.doctorVisitsRecyclerView;
        VisitsRecyclerViewAdapter visitsRecyclerViewAdapter = new VisitsRecyclerViewAdapter(getActivity(), pendingVisit.getVisits(), reachedHospital, Utility.VisitStatus.PENDING);
        doctorVisitsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doctorVisitsRecyclerView.setAdapter(visitsRecyclerViewAdapter);
        doctorVisitsRecyclerView.setNestedScrollingEnabled(false);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(VisitHospitalInfoFragment.super.getActivity()).onBackPressed();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
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

        geofencingClient = LocationServices.getGeofencingClient(Objects.requireNonNull(getActivity()));
        geofenceList = new ArrayList<>();
        geofencePendingIntent = null;

        if (reachedHospital)
            binding.startVisit.setVisibility(View.GONE);

        binding.startVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGeofence();

                utility.saveVisitTimeStamp(pendingVisit, DBHandler.COLUMN_VISIT_STARTED, getContext());

                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + String.valueOf(pendingVisit.getLatitude()) + "," + String.valueOf(pendingVisit.getLongitude()));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(pendingVisit.getLatitude(), pendingVisit.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(Html.fromHtml(pendingVisit.getHospitalName()).toString()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    private void addGeofence() {

        geofenceList.add(new Geofence.Builder()
                .setRequestId(String.valueOf(pendingVisit.getHospitalId()))

                .setCircularRegion(
                        pendingVisit.getLatitude(),
                        pendingVisit.getLongitude(),
                        500
                )
                .setExpirationDuration(5 * 60 * 60 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT |
                        Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(1000)
                .build());

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully added geofence");
                        // Geofences added
                        // ...
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to add geofences", e);
                        // Failed to add geofences
                        // ...
                    }
                });
    }

    private boolean checkPermissions() {
        boolean permission = false;
        if (getContext() != null) {
            permission = ContextCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                permission &= ContextCompat.checkSelfPermission(getContext(), ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED;
            }
        }
        return permission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionsGranted = false;
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
                if (grantResults.length == 2) {
                    permissionsGranted = (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED;
                } else if (grantResults.length == 1) {
                    permissionsGranted = (grantResults[0]) == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }
        if (!permissionsGranted) {
            Log.d(TAG, "Permission not granted");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
        }
    }

}
