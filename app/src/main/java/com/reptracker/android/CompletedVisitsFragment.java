package com.reptracker.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reptracker.android.databinding.FragmentCompletedVisitsBinding;


public class CompletedVisitsFragment extends Fragment {

    private FragmentCompletedVisitsBinding binding;

    public CompletedVisitsFragment() {
        // Required empty public constructor
    }

    public static CompletedVisitsFragment newInstance() {
        return new CompletedVisitsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCompletedVisitsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        List<Visit> completedVisitsList = setData();

//        RecyclerView completedVisitsRecyclerView = binding.completedVisitsRecyclerView;
//        VisitsRecyclerViewAdapter visitsRecyclerViewAdapter = new VisitsRecyclerViewAdapter(getActivity(), completedVisitsList);
//        completedVisitsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        completedVisitsRecyclerView.setAdapter(visitsRecyclerViewAdapter);

    }

//    public static List<Visit> setData() {
//        List<Visit> completedVisitsList = new ArrayList<>();
//
//        Hospital hospital1 = new Hospital(1, "Max Super Speciality Hospital", "Patparganj", "011-2322324", 38.6121558, 37.2728417, "");
//        Hospital hospital2 = new Hospital(2, "Max Smart Super Speciality Hospital", "Saket", "011-2322324", 28.6121558, 27.2728417, "");
//        Hospital hospital3 = new Hospital(3, "Max Hospital", "Gurgaon", "011-2322324", 18.6121558, 7.2728417, "");
//        Hospital hospital4 = new Hospital(4, "Max Multi Speciality Centre", "Panchsheel Park", "011-2322324", 58.6121558, 57.2728417, "");
//        Hospital hospital5 = new Hospital(5, "Max Super Speciality Hospital", "Saket", "011-2322324", 98.6121558, 37.2728417, "");
//
//        Doctor doctor1 = new Doctor(1, "DR. SANDEEP BUDHIRAJA", "011-23132324", "Internal Medicine", "SAFDSAfc sdacasdfc", hospital1);
//        Doctor doctor2 = new Doctor(2, "DR. HARIT CHATURVEDI", "011-23132324", "Cancer Care / Oncology", "Breast Cancer", hospital1);
//        Doctor doctor3 = new Doctor(3, "DR. PRADEEP CHOWBEY", "011-23132324", "Minimal Access / Laparoscopic Surgery", "Metabolic And Bariatric Surgery", hospital1);
//        Doctor doctor4 = new Doctor(4, "PROF (DR.) SUBHASH GUPTA", "011-23132324", "Liver Transplant And Biliary Sciences", "Liver and Biliary Sciences", hospital1);
//        Doctor doctor5 = new Doctor(5, "DR. BALBIR SINGH", "011-23132324", "Cardiac Sciences", "Cardiology", hospital2);
//        Doctor doctor6 = new Doctor(6, "DR. S.K.S. MARYA", "011-23132324", "Orthopaedics and Joint Replacement", "SAFDSAfc sdacasdfc", hospital2);
//        Doctor doctor7 = new Doctor(7, "DR. AMBRISH MITHAL", "011-23132324", "Endocrinology & Diabetes", "SAFDSAfc sdacasdfc", hospital2);
//        Doctor doctor8 = new Doctor(8, "DR. ANURAG KRISHNA", "011-23132324", "Pediatrics/Paediatric Urology", "Pediatrics", hospital3);
//        Doctor doctor9 = new Doctor(9, "DR. ANANT KUMAR", "011-23132324", "Urology", "Kidney Transplant", hospital3);
//        Doctor doctor10 = new Doctor(10, "DR. A.K. SINGH", "011-23132324", "Neurosciences", "Gastrointestinal Oncology", hospital4);
//        Doctor doctor11 = new Doctor(11, "DR. RUDRA PRASAD ACHARYA", "011-23132324", "Cancer Care / Oncology", "Gynecologic Oncology", hospital4);
//        Doctor doctor12 = new Doctor(12, "DR. SANDEEP AGARWAL", "011-23132324", "Cancer Care / Oncology", "Cardiology", hospital5);
//        Doctor doctor13 = new Doctor(13, "DR. RAJIV AGARWAL", "011-23132324", "Cardiac Sciences", "Neurosciences", hospital5);
//
//        Visit visit1 = new Visit(1, doctor1, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit2 = new Visit(2, doctor2, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit3 = new Visit(3, doctor3, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit4 = new Visit(4, doctor4, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit5 = new Visit(5, doctor5, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit6 = new Visit(6, doctor6, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit7 = new Visit(7, doctor7, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit8 = new Visit(8, doctor8, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit9 = new Visit(9, doctor9, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit10 = new Visit(10, doctor10, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit11 = new Visit(11, doctor11, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit12 = new Visit(12, doctor12, Visit.VisitStatus.COMPLETED, null, null, null, null);
//        Visit visit13 = new Visit(13, doctor13, Visit.VisitStatus.COMPLETED, null, null, null, null);
//
//        completedVisitsList.add(visit13);
//        completedVisitsList.add(visit12);
//        completedVisitsList.add(visit11);
//        completedVisitsList.add(visit10);
//        completedVisitsList.add(visit9);
//        completedVisitsList.add(visit8);
//        completedVisitsList.add(visit7);
//        completedVisitsList.add(visit6);
//        completedVisitsList.add(visit5);
//        completedVisitsList.add(visit4);
//        completedVisitsList.add(visit3);
//        completedVisitsList.add(visit2);
//        completedVisitsList.add(visit1);
//
//        return completedVisitsList;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
