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

        View view = inflater.inflate(R.layout.list_message,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {

        Log.d("LOG : ", "Setting onBind "+i);

        String sender="";
        String content="";

        //TODO: update server to get name with Message instead of SenderId

        try {
            JSONObject item = (JSONObject)itemList.get(i);

            sender = item.get("SenderId").toString();
            content = item.get("Text").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        derpHolder.msgSender.setText(sender);
        derpHolder.msgContent.setText(content);

    }

    @Override
    public int getItemCount() {

        return itemList.length();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        private TextView msgSender;
        private TextView msgContent;
        private View container;

        public DerpHolder(View itemView) {
            super(itemView);

            msgSender = (TextView) itemView.findViewById(R.id.tv_sender_name);
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
