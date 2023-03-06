package com.reptracker.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.reptracker.android.activities.VisitWorkflowActivity;
import com.reptracker.android.databinding.HomePendingVisitListItemBinding;
import com.reptracker.android.models.PendingVisit;

import java.util.List;

public class PendingVisitsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = PendingVisitsRecyclerViewAdapter.class.getSimpleName();

    private List<PendingVisit> pendingVisitList;
    private LayoutInflater mInflater;
    private Context mContext;

    public PendingVisitsRecyclerViewAdapter(Context context, List<PendingVisit> pendingVisitList) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.pendingVisitList = pendingVisitList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        HomePendingVisitListItemBinding binding = HomePendingVisitListItemBinding.inflate(mInflater, parent, false);
        viewHolder = new HomePendingVisit(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        PendingVisit pendingVisit = pendingVisitList.get(position);

        ((HomePendingVisit)holder).hospitalName.setText(Html.fromHtml(pendingVisit.getHospitalName()));
        ((HomePendingVisit)holder).hospitalAddress.setText(Html.fromHtml(pendingVisit.getAddress()));
        ((HomePendingVisit)holder).remainingCount.setText(String.valueOf(pendingVisit.getRemainingVisits()));

        ((HomePendingVisit)holder).homePendingVisitListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + pendingVisitList.get(position));

                Intent intent = new Intent(mContext, VisitWorkflowActivity.class);
                intent.putExtra("PendingVisit", pendingVisit);
                intent.putExtra("reachedHospital", false);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pendingVisitList.size();
    }

    static class HomePendingVisit extends RecyclerView.ViewHolder {
        ConstraintLayout homePendingVisitListItem;
        TextView hospitalName, hospitalAddress, remainingCount;

        HomePendingVisit(HomePendingVisitListItemBinding binding) {
            super(binding.getRoot());
            homePendingVisitListItem = binding.homePendingVisitListItem;
            hospitalName = binding.hospitalName;
            hospitalAddress = binding.hospitalAddress;
            remainingCount = binding.remainingCount;
        }
    }
}
