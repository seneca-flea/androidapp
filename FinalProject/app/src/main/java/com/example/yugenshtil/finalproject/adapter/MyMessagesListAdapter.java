package com.example.yugenshtil.finalproject.adapter;

/**
 * Created by rbocanegramez on 11/25/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MyMessagesListAdapter extends RecyclerView.Adapter<MyMessagesListAdapter.DerpHolder>  {
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

    public MyMessagesListAdapter(Context c, JSONArray jsonArray){
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
        //TODO: update to process response by server
        Log.d("LOG : ", "Setting onBind "+i);
        String dateSent="";
        String sender="";
        String content="";

        try {
            JSONObject item = (JSONObject)itemList.get(i);
            dateSent = item.get("Title").toString();
            sender = item.get("Description").toString();
            content = item.get("Price").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        derpHolder.msgDate.setText(dateSent);
        derpHolder.msgContent.setText(sender);



    }

    @Override
    public int getItemCount() {

        return itemList.length();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        //TODO: update to reflect contents of messagelist_list;(individual message)

        private TextView msgDate;
        private TextView msgContent;
        private View container;



        public DerpHolder(View itemView) {
            super(itemView);

            msgDate = (TextView) itemView.findViewById(R.id.tv_message_date);
            msgContent = (TextView) itemView.findViewById(R.id.tv_content_message);
            container = itemView.findViewById(R.id.cont_message_list_root);

            container.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.cont_item_root_history){
                itemClickCallBack.onItemClick(getAdapterPosition());
                Log.d("LOG : ","Clicked Item for line :" + getAdapterPosition());
            }
        }

    }


}
