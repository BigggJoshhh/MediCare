package com.example.medicare;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecylerItemArrayAdapter extends RecyclerView.Adapter<RecylerItemArrayAdapter.MyViewHolder> {

    private final ArrayList<RecyclerItem> appointments;
    private final Context context;

    public RecylerItemArrayAdapter(ArrayList<RecyclerItem> appointments, Context context) {
        this.appointments = appointments;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_appointment_recycler_item, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.viewType.setText(appointments.get(position).getService());
        holder.viewDate.setText(appointments.get(position).getFormattedDate());
        holder.viewTime.setText(appointments.get(position).getFormattedTime());
        holder.viewAddress.setText(appointments.get(position).getAddress());
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.appointment, null);
        holder.viewImage.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView viewImage;
        private final TextView viewType;
        private final TextView viewDate;
        private final TextView viewTime;
        private final TextView viewAddress;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            viewImage = itemView.findViewById(R.id.viewImage);
            viewType = itemView.findViewById(R.id.viewType);
            viewDate = itemView.findViewById(R.id.viewDate);
            viewTime = itemView.findViewById(R.id.viewTime);
            viewAddress = itemView.findViewById(R.id.viewAddress);
        }
    }
}

