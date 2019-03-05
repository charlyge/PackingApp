package com.charlyge.android.packingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.charlyge.android.packingapp.AppExecutors;
import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.Database.packDb.PackingReminderDb;
import com.charlyge.android.packingapp.R;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ItemViewHolder> {
    private List<Items> itemsList;
    private Context context;

    public ItemListAdapter (List<Items> itemsList,Context context){
       this.itemsList = itemsList;
       this.context = context;

      }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.dropdown_packing_list_item,viewGroup,false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        final Items items = itemsList.get(itemViewHolder.getAdapterPosition());
        Log.i("", items.getItems());
       itemViewHolder.checkBox.setText(items.getItems());
       if(items.getStatus()){
           itemViewHolder.checkBox.setChecked(true);
       }
       else {
           itemViewHolder.checkBox.setChecked(false);
       }

        itemViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.getStatus()){
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            PackingReminderDb.getsInstance(context).itemsDao().updateItemsStatus(false,items.getItemsId());
                        }
                    });
                }
                else {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            PackingReminderDb.getsInstance(context).itemsDao().updateItemsStatus(true,items.getItemsId());
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
          if(itemsList==null){
              return 0;
          }
           return itemsList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
       CheckBox checkBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.items_check_box);
        }
    }
}
