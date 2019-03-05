package com.charlyge.android.packingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TravelHistoryAdapter extends RecyclerView.Adapter<TravelHistoryAdapter.TravelHistoryViewHolder> {
    private static final String DATE_FORMAT = "dd/MM/yyyyhh:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private List<PackingReminder> packingReminderList;

    public TravelHistoryAdapter(){
        this.packingReminderList = packingReminderList;
    }
    @NonNull
    @Override
    public TravelHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.travel_history_list_item,viewGroup,false);
        return new TravelHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelHistoryViewHolder travelHistoryViewHolder, int i) {
      PackingReminder packingReminder = packingReminderList.get(travelHistoryViewHolder.getAdapterPosition());
        String timeOfTravel = dateFormat.format(packingReminder.getTimeOfTravel());
        travelHistoryViewHolder.time.setText(timeOfTravel);
        String modeOftravel = packingReminder.getTravelMode();
        int imageResource = 0;

        if (modeOftravel.equals("Car")) {
            imageResource = R.drawable.ic_directions_car_black_24dp;
        }
        if (modeOftravel.equals("Bus")) {
            imageResource = R.drawable.ic_airport_shuttle_black_24dp;
        }
        if (modeOftravel.equals("Airplane")) {
            imageResource = R.drawable.ic_airplanemode_active_black_24dp;
        }
        if (modeOftravel.equals("Bike")) {
            imageResource = R.drawable.ic_directions_bike_black_24dp;
        }
        travelHistoryViewHolder.travelMode.setImageResource(imageResource);
        travelHistoryViewHolder.destination.setText("Trip to " + packingReminder.getDestination());
    }

    @Override
    public int getItemCount() {
        if(packingReminderList==null){
            return 0;
        }
     return packingReminderList.size();
    }

    public void setTravelList(List<PackingReminder> packingReminderList) {
        this.packingReminderList = packingReminderList;
        notifyDataSetChanged();
    }

    class TravelHistoryViewHolder extends RecyclerView.ViewHolder{

        TextView destination, time;
        ImageView travelMode;
        public TravelHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            destination = itemView.findViewById(R.id.destination);
            travelMode = itemView.findViewById(R.id.mode_of_travel);
            time = itemView.findViewById(R.id.time_of_travel);
        }
    }
}
