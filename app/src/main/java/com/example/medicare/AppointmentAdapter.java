package com.example.medicare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private static List<Appointment> appointmentsList;
    private OnAppointmentClickListener listener;

    public interface OnAppointmentClickListener {
        void onAppointmentClick(Appointment appointment);
    }

    public AppointmentAdapter(List<Appointment> appointmentsList, OnAppointmentClickListener listener) {
        this.appointmentsList = appointmentsList != null ? appointmentsList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView headingTextView, dateTextView, timeTextView, locationTextView;


        public AppointmentViewHolder(@NonNull View itemView, OnAppointmentClickListener listener) {
            super(itemView);
            headingTextView = itemView.findViewById(R.id.headingTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onAppointmentClick(appointmentsList.get(position));
                }
            });
        }

        public void bind(Appointment appointment) {
            headingTextView.setText(appointment.getService());
            dateTextView.setText(appointment.getFormattedDate());
            timeTextView.setText(appointment.getFormattedTime());
            locationTextView.setText(appointment.getLocation());
        }
    }
}
