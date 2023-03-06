package com.reptracker.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.reptracker.android.CompletedVisitInfoActivity;
import com.reptracker.android.R;
import com.reptracker.android.databinding.DoctorVisitListItemBinding;
import com.reptracker.android.fragments.VisitDoctorFragment;
import com.reptracker.android.models.PendingVisit.Visits;
import com.reptracker.android.models.Visit;
import com.reptracker.android.utilities.Utility;

import java.util.List;

public class VisitsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = VisitsRecyclerViewAdapter.class.getSimpleName();

    private List<Visits> visitList;
    private LayoutInflater mInflater;
    private Context mContext;
    private boolean reachedHospital = false;
    private Utility.VisitStatus visitStatus;

    VisitsRecyclerViewAdapter(Context context, List<Visit> visitList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
//        this.visitList = visitList;
    }

    public VisitsRecyclerViewAdapter(Context context, List<Visits> visitList, boolean reachedHospital, Utility.VisitStatus visitStatus) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.visitList = visitList;
        this.reachedHospital = reachedHospital;
        this.visitStatus = visitStatus;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        DoctorVisitListItemBinding binding = DoctorVisitListItemBinding.inflate(mInflater, parent, false);
        viewHolder = new DoctorVisit(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        Visits visit = visitList.get(position);

        ((DoctorVisit)holder).doctorName.setText(Html.fromHtml(visit.getDoctorName()));
        ((DoctorVisit)holder).doctorSpeciality.setText(Html.fromHtml(visit.getDoctorSpeciality()));
        ((DoctorVisit)holder).doctorContactNumber.setText(visit.getDoctorContact());

        if (visitStatus.compareTo(Utility.VisitStatus.PENDING) == 0) {
            ((DoctorVisit) holder).startPresentation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: clicked on: " + visit);

                    VisitDoctorFragment visitDoctorFragment = new VisitDoctorFragment();
                    Bundle args = new Bundle();
                    args.putParcelable("Visit", visit);
                    visitDoctorFragment.setArguments(args);

                    ((FragmentActivity) mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.visitWorkflowFrame, visitDoctorFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            ((DoctorVisit) holder).startPresentation.setVisibility(View.VISIBLE);
            ((DoctorVisit) holder).startPresentation.setEnabled(false);
            if (reachedHospital) {
                ((DoctorVisit) holder).startPresentation.setEnabled(true);
            }
        } else if (visitStatus.compareTo(Utility.VisitStatus.COMPLETED) == 0) {
            ((DoctorVisit) holder).doctorVisitListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: clicked on: " + visit);

                    Intent intent = new Intent(mContext, CompletedVisitInfoActivity.class);
                    intent.putExtra("Visit", visit);
                    mContext.startActivity(intent);
                }
            });
            ((DoctorVisit) holder).startPresentation.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    static class DoctorVisit extends RecyclerView.ViewHolder {
        ConstraintLayout doctorVisitListItem;
        TextView doctorName, doctorSpeciality, doctorContactNumber;
        Button startPresentation;

        DoctorVisit(DoctorVisitListItemBinding binding) {
            super(binding.getRoot());
            doctorVisitListItem = binding.doctorVisitListItem;
            doctorName = binding.doctorName;
            doctorSpeciality = binding.doctorSpeciality;
            doctorContactNumber = binding.doctorContactNumber;
            startPresentation = binding.startPresentation;
        }
    }
}
