package com.example.yugenshtil.finalproject.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.model.ListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.DerpHolder>  {
    private LayoutInflater inflater;
    private JSONArray itemList;

    private ItemClickCallback itemClickCallBack;

    public interface ItemClickCallback{
        void onItemClick(int p);
        void onDeleteIconClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallBack){
        this.itemClickCallBack = itemClickCallBack;

    }

    public HistoryAdapter(Context c, JSONArray jsonArray){
        this.inflater = LayoutInflater.from(c);
        //  this.listData = listData;
        Log.d("LOG : ",jsonArray.toString());
        this.itemList = jsonArray;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.list_history_item,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {
        Log.d("LOG : ", "Setting onBind "+i);
        String title="";
        String description="";
        String price="";

        try {
            JSONObject item = (JSONObject)itemList.get(i);

            JSONObject historyItem = item.getJSONObject("Item");



            title = historyItem.get("Title").toString();
            description = historyItem.get("Description").toString();
            price = historyItem.get("Price").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        derpHolder.title.setText(title);
        derpHolder.description.setText(description);
        derpHolder.price.setText(price+"$");


    }

    @Override
    public int getItemCount() {

        return itemList.length();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        private TextView title;
        private TextView description;
        private TextView price;
        private View container;
        private ImageView deleteIcon;


        public DerpHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title_history);
            description = (TextView) itemView.findViewById(R.id.tv_description_history);
            price = (TextView) itemView.findViewById(R.id.tv_price_history);
            deleteIcon = (ImageView) itemView.findViewById(R.id.im_history_delete_icon);
            container = itemView.findViewById(R.id.cont_item_root_history);

            deleteIcon.setOnClickListener(this);
            container.setOnClickListener(this);

            }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.cont_item_root_history){
                itemClickCallBack.onItemClick(getAdapterPosition());
                Log.d("LOG : ","Clicked Item for line :" + getAdapterPosition());
            }
            else if (v.getId() == R.id.im_history_delete_icon) {
                Log.d("LOG : ", "Clicked delete for line " + getAdapterPosition());
                itemClickCallBack.onDeleteIconClick(getAdapterPosition());
            }

        }

    }


}
