package com.charlyge.android.packingapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.charlyge.android.packingapp.Database.Entities.Items;
import com.charlyge.android.packingapp.R;

import java.util.Date;
import java.util.List;

public class SetReminderListAdapter extends RecyclerView.Adapter<SetReminderListAdapter.myViewHolder> {
    private List<Items> itemsList;
    private Context context;
    private ItemClickedListener itemClickedListener;

    public SetReminderListAdapter(Context context,ItemClickedListener itemClickedListener){
        this.context = context;
        this.itemClickedListener = itemClickedListener;
    }


    public interface ItemClickedListener{
        void OnItemClicked(int Id);
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       Context context = viewGroup.getContext();
       View view = LayoutInflater.from(context).inflate(R.layout.set_reminder_list_item,viewGroup,false);
       return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
     final Items items = itemsList.get(myViewHolder.getAdapterPosition());
     myViewHolder.displayitem.setText(items.getItems());
     myViewHolder.removeItem.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             //delete Item
             Toast.makeText(context, "Item Removed ", Toast.LENGTH_SHORT).show();
             itemClickedListener.OnItemClicked(myViewHolder.getAdapterPosition());
         }
     });
    }

    @Override
    public int getItemCount() {
        if (itemsList==null){
            return 0;
        }
        return itemsList.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView displayitem;
        ImageView removeItem;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            displayitem = itemView.findViewById(R.id.display_item);
            removeItem = itemView.findViewById(R.id.display_item_delete);
        }
    }

    public void setItemsList(List<Items> itemsList) {
        this.itemsList = itemsList;
        notifyDataSetChanged();
    }
}
