package com.example.modul_spp_ukk2021.UI.UI.Splash.BoardingAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modul_spp_ukk2021.R;

import java.util.List;

public class BoardingAdapter extends RecyclerView.Adapter<BoardingAdapter.OnboardingViewHolder> {
    private final Context context;
    private final List<DataBoarding> dataBoardings;

    public BoardingAdapter(List<DataBoarding> dataBoardings, Context context) {
        this.context = context;
        this.dataBoardings = dataBoardings;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.container_boarding, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(OnboardingViewHolder holder, int position) {
        DataBoarding dataBoarding = this.dataBoardings.get(position);

        holder.tvTitle.setText(dataBoarding.getTitle());
        holder.tvDescription.setText(dataBoarding.getDescription());
        holder.ivOnboarding.setImageDrawable(ContextCompat.getDrawable(context, dataBoarding.getImage()));
    }

    @Override
    public int getItemCount() {
        return dataBoardings.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivOnboarding;
        private final TextView tvTitle, tvDescription;

        OnboardingViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivOnboarding = itemView.findViewById(R.id.ivOnboarding);

        }
    }
}
