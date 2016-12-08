package com.example.yugenshtil.finalproject.Adapter;

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


public class MyMessagesAdapter extends RecyclerView.Adapter<MyMessagesAdapter.DerpHolder>  {
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

    public MyMessagesAdapter(Context c, JSONArray jsonArray){
        this.inflater = LayoutInflater.from(c);
        //  this.listData = listData;
        Log.d("LOG : ",jsonArray.toString());
        this.itemList = jsonArray;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.list_conversation,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {
        Log.d("LOG : ", "Setting onBind "+i);

        String title="";
        String description="";


        try {
            JSONObject item = (JSONObject)itemList.get(i);
            JSONObject recentMsg = item.getJSONObject("recentMessage");
            title = item.get("UserFirstName").toString();
            description = recentMsg.get("Text").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        derpHolder.title.setText(title);
        derpHolder.description.setText(description);
    }

    @Override
    public int getItemCount() {

        return itemList.length();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        private TextView title;
        private TextView description;
        private View container;
        private ImageView deleteIcon;


        public DerpHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title_message);
            description = (TextView) itemView.findViewById(R.id.tv_description_message);
            deleteIcon = (ImageView) itemView.findViewById(R.id.im_delete_icon_message);
            container = itemView.findViewById(R.id.cont_message_root);

            deleteIcon.setOnClickListener(this);
            container.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.cont_message_root){
                itemClickCallBack.onItemClick(getAdapterPosition());
                Log.d("LOG : ","Clicked Item for line :" + getAdapterPosition());
            }
            else if (v.getId() == R.id.im_delete_icon_message) {
                Log.d("LOG :", "Clicked delete for line : " + getAdapterPosition());
                itemClickCallBack.onDeleteIconClick(getAdapterPosition());
            }

        }

    }


}
