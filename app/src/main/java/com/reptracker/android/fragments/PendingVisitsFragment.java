package com.reptracker.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.reptracker.android.R;
import com.reptracker.android.activities.VisitWorkflowActivity;
import com.reptracker.android.adapters.PendingVisitsRecyclerViewAdapter;
import com.reptracker.android.databinding.FragmentPendingVisitsBinding;
import com.reptracker.android.models.PendingVisit;
import com.reptracker.android.utilities.DBHandler;
import com.reptracker.android.utilities.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PendingVisitsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = PendingVisitsFragment.class.getSimpleName();

    private FragmentPendingVisitsBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private List<PendingVisit> pendingVisitList = new ArrayList<>();
    private PendingVisitsRecyclerViewAdapter pendingVisitsRecyclerViewAdapter;
    private RecyclerView pendingVisitsRecyclerView;
    private String pendingVisitId = null;

    private boolean isListView = true;
    private GoogleMap gMap;

    public PendingVisitsFragment() {
        // Required empty public constructor
    }

    private static PendingVisitsFragment newInstance() {
        return new PendingVisitsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (getArguments() != null && getArguments().getString("pendingVisitId") != null) {
            pendingVisitId = getArguments().getString("pendingVisitId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPendingVisitsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pendingVisitsRecyclerView = binding.pendingVisitsRecyclerView;
        pendingVisitsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.changeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isListView) {
                    binding.mapViewLayout.setVisibility(View.VISIBLE);
                    binding.pendingVisitsRecyclerView.setVisibility( View.GONE);
                    binding.changeView.setText(getResources().getString(R.string.list));
                    isListView = false;
                } else {
                    binding.mapViewLayout.setVisibility(View.GONE);
                    binding.pendingVisitsRecyclerView.setVisibility( View.VISIBLE);
                    binding.changeView.setText(getResources().getString(R.string.map));
                    isListView = true;
                }
            }
        });

        fetchHospitalVisits();

    }

    private void setMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        binding.mapBackgroundImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        binding.mapViewLayout.requestDisallowInterceptTouchEvent(true);
                        return false;
                    case MotionEvent.ACTION_UP:
                        binding.mapViewLayout.requestDisallowInterceptTouchEvent(false);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }

    private void fetchHospitalVisits() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            return;
        db.collection("users/" + currentUser.getUid() + "/user_hospitals")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PendingVisit pendingVisit = new PendingVisit(document.getString("hospitalName"), document.getString("address"), document.getString("contact"), document.getDouble("latitude"), document.getDouble("longitude"), document.getLong("remainingVisits"));//document.toObject(PendingVisit.class);
                                List<PendingVisit.Visits> visitList = new ArrayList<>();
                                Map<String, PendingVisit.Visits> visits = (Map<String, PendingVisit.Visits>) document.get("visits");
                                for (String key : visits.keySet()){
                                    Map<String, String> visit = (Map<String, String>) visits.get(key);
                                    visitList.add(new PendingVisit.Visits(key, visit.get("doctorId"), visit.get("doctorName"), visit.get("doctorSpeciality"), visit.get("doctorContact")));
                                }
                                pendingVisit.setHospitalId(document.getId());
                                pendingVisit.setVisits(visitList);
                                pendingVisitList.add(pendingVisit);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            pendingVisitsRecyclerViewAdapter = new PendingVisitsRecyclerViewAdapter(getContext(), pendingVisitList);
                            pendingVisitsRecyclerView.setAdapter(pendingVisitsRecyclerViewAdapter);

                            setMap();

                            if (pendingVisitId != null) {
                                for (PendingVisit pendingVisit: pendingVisitList) {
                                    if (pendingVisit.getHospitalId().equals(pendingVisitId)) {
                                        Log.i("TAG", "pendingVisitId: " + pendingVisitId);

                                        Utility utility = new Utility();
                                        utility.saveVisitTimeStamp(pendingVisit, DBHandler.COLUMN_REACHED_HOSPITAL, getContext());

                                        Intent intent = new Intent(getActivity(), VisitWorkflowActivity.class);
                                        intent.putExtra("PendingVisit", pendingVisit);
                                        intent.putExtra("reachedHospital", true);
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }
                        } else {
                            Log.e(TAG, "Error getting user document.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                if (pendingVisitList.size() <= 0)
                    return;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < pendingVisitList.size(); i++) {
                    LatLng latLng = new LatLng(pendingVisitList.get(i).getLatitude(), pendingVisitList.get(i).getLongitude());
                    gMap.addMarker(new MarkerOptions().position(latLng)
                            .title(Html.fromHtml(pendingVisitList.get(i).getHospitalName()).toString()));
                    builder.include(latLng);
                }
                LatLngBounds bounds = builder.build();
                int padding = 0; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                gMap.animateCamera(cu);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
