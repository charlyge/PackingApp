package com.charlyge.android.packingapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.charlyge.android.packingapp.AppExecutors;
import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.Entities.PackingReminder;
import com.charlyge.android.packingapp.R;
import com.charlyge.android.packingapp.Repository.AppRepository;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PackingRemindersAdapter extends RecyclerView.Adapter<PackingRemindersAdapter.myViewHolder> {
    private List<PackingReminder> packingReminderList;
    private Context context;
    private static final String DATE_FORMAT = "MMMMM/yyyy HH:mm";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private AppRepository appRepository;
    private OnShareIconClickListener onShareIconClickListener;
    private List<Items> itemsList;
    private PackingReminder packingReminder;
    private OnLocationIconClickListener onLocationIconClickListener;

    public interface OnShareIconClickListener {
        void onSharedIconClicked(String shareMsg,String Destination);
    }
    public interface OnLocationIconClickListener {
        void onLocationIconClicked(String Destination);
    }

    public PackingRemindersAdapter(List<PackingReminder> packingReminderList, Context context,
        OnShareIconClickListener onShareIconClickListener,OnLocationIconClickListener onLocationIconClickListener) {
        this.packingReminderList = packingReminderList;
        this.context = context;
        appRepository = new AppRepository(context);
        this.onShareIconClickListener = onShareIconClickListener;
        this.onLocationIconClickListener = onLocationIconClickListener;

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.main_activity_list_item, viewGroup, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        itemsList = null;
        packingReminder = packingReminderList.get(myViewHolder.getAdapterPosition());
        String timeOfTravel = dateFormat.format(packingReminder.getTimeOfTravel());
        myViewHolder.Time.setText(timeOfTravel);
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
//        myViewHolder.travelMode.setImageResource(imageResource);
        Picasso.get().load(imageResource).into(myViewHolder.travelMode);
        myViewHolder.destination.setText("Trip to " + packingReminder.getDestination());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                itemsList = appRepository.getItemsByTime(packingReminder.getTimeOfTravel());
            }
        });
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                myViewHolder.recyclerView.setLayoutManager(linearLayoutManager);
                ItemListAdapter itemListAdapter = new ItemListAdapter(itemsList, context);
                myViewHolder.recyclerView.setHasFixedSize(true);
                myViewHolder.recyclerView.setAdapter(itemListAdapter);

            }
        });

        //Compares Date
        Date travelDate = packingReminder.getTimeOfTravel();
        Date currentDate = new Date();
        if (travelDate.before(currentDate)) {
            //Delete entry
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    for (Items items : itemsList) {
                        appRepository.deleteItem(items);
                    }
                    appRepository.deletePackingReminder(packingReminder);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        if (packingReminderList == null) {
            return 0;
        }
        return packingReminderList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView destination, Time;
        ImageView travelMode, share,location;
        RecyclerView recyclerView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            // final PackingReminder packingReminder = packingReminderList.get(getAdapterPosition());
            destination = itemView.findViewById(R.id.destination);
            travelMode = itemView.findViewById(R.id.mode_of_travel);
            Time = itemView.findViewById(R.id.time_of_travel);
            recyclerView = itemView.findViewById(R.id.recycler_view_dropdown);
            share = itemView.findViewById(R.id.share);
            location = itemView.findViewById(R.id.location);

            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PackingReminder packingReminder = packingReminderList.get(getAdapterPosition());
                    onLocationIconClickListener.onLocationIconClicked(packingReminder.getDestination());
                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PackingReminder packingReminder = packingReminderList.get(getAdapterPosition());
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            List<Items> items = appRepository.getItemsByTime(packingReminder.getTimeOfTravel());

                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < items.size(); i++) {
                                stringBuilder.append("--").append(items.get(i).getItems()).append("\n");
                            }
                            onShareIconClickListener.onSharedIconClicked(stringBuilder.toString(),packingReminder.getDestination());
                        }
                    });
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemsList = null;
            final PackingReminder packingReminder = packingReminderList.get(getAdapterPosition());
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    itemsList = appRepository.getItemsByTime(packingReminder.getTimeOfTravel());
                }
            });
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    ItemListAdapter itemListAdapter = new ItemListAdapter(itemsList, context);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(itemListAdapter);
                }
            });


        }
    }
}
