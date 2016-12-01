package com.example.yugenshtil.finalproject.adapter;

import android.content.Context;
import android.media.Image;
//import android.support.v7.internal.view.menu.MenuView;
//import android.support.v7.widget.RecyclerView;
import android.media.browse.MediaBrowser;
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
import java.util.List;

/**
 * Created by yugenshtil on 05/11/16.
 */

public class BuyItemAdapter extends RecyclerView.Adapter<BuyItemAdapter.DerpHolder>  {

    private LayoutInflater inflater;
    private JSONArray itemList;
    private List<String> ids;

    private ItemClickCallback itemClickCallBack;

    public interface ItemClickCallback{
        void onMyFavoriteDeleteClick(int p);
        void onMyFavoriteAddClick(int p);
        void onItemClick(int p);
        void onMessageIconClick(int p);

    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallBack){
        this.itemClickCallBack = itemClickCallBack;

    }

    public BuyItemAdapter(Context c, JSONArray jsonArray, List<String> ids){
        this.ids = ids;
        this.inflater = LayoutInflater.from(c);
        //  this.listData = listData;
        Log.d("Oleg",jsonArray.toString());
        this.itemList = jsonArray;
    }

    @Override
    public DerpHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.list_buy_item,viewGroup,false);

        return new DerpHolder(view);
    }

    @Override
    public void onBindViewHolder(DerpHolder derpHolder, int i) {
        Log.d("Oleg", "Setting onbind "+i);
        String title="";
        String description="";
        String price="";
      //  boolean isFavorite = false;
        //Here it sets to the view
        try {
            JSONObject item = (JSONObject)itemList.get(i);
            title = item.get("Title").toString();
            description = item.get("Description").toString();
            price = item.get("Price").toString();
            if(ids.contains(item.getString("ItemId"))){
                Log.d("Oleg",item.getString("ItemId") + " was found");
                derpHolder.isFavorite = true;

            }





        } catch (JSONException e) {
            e.printStackTrace();
        }

        //    ListItem item = listData.get(i);
        derpHolder.title.setText(title);
        derpHolder.description.setText(description);
        derpHolder.price.setText(price+"$");
        if(derpHolder.isFavorite){
            derpHolder.favoriteIcon.setImageResource(R.drawable.ic_star_black_36dp);
            derpHolder.favoriteIcon.setTag("fav");
        }else{
            derpHolder.favoriteIcon.setTag("notfav");
        }

        //    derpHolder.icon.setImageResource(item.getImageResId());
      /*
        if(item.isFavourite())
        {
          derpHolder.icon.setImageResource(R.drawable.ic_update_black_36dp);

        }
        else
            derpHolder.icon.setImageResource(R.drawable.ic_picture_in_picture_black_36dp);
*/

    }

    //NEW


    public void setListData(ArrayList<ListItem> exerciseList){
      //  this.listData.clear();
     //   this.listData.addAll(exerciseList);

    }

    @Override
    public int getItemCount() {

        return itemList.length();
        //   return listData.size();
    }

    class DerpHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        private TextView title;
        private TextView description;
        private TextView price;
        private View container;
        private ImageView favoriteIcon;
        private ImageView messageIcon;
        private boolean isFavorite=false;

        //New
        private ImageView thumbnail;
        private ImageView secondaryIcon;


        public DerpHolder(View itemView) {
            super(itemView);

            title =(TextView)itemView.findViewById(R.id.tv_title);
            description = (TextView)itemView.findViewById(R.id.tv_description);
            price = (TextView)itemView.findViewById(R.id.tv_price);
            favoriteIcon = (ImageView)itemView.findViewById(R.id.im_favorite_icon);
            messageIcon = (ImageView) itemView.findViewById(R.id.im_new_message);
            container = itemView.findViewById(R.id.cont_item_root);

           favoriteIcon.setOnClickListener(this);
            messageIcon.setOnClickListener(this);
           container.setOnClickListener(this);

            //  description =(ImageView)itemView.findViewById(R.id.im_item_icon);

            //   thumbnail = (ImageView)itemView.findViewById(R.id.im_item_icon)
        }

        @Override
        public void onClick(View v){
            if(v.getId()==R.id.cont_item_root){

                Log.d("Oleg","Onclick is" + itemClickCallBack);
                itemClickCallBack.onItemClick(getAdapterPosition());
                Log.d("Oleg","Clicked Item for line " + getAdapterPosition());
            }
            else if(v.getId()==R.id.im_favorite_icon){
                Log.d("Oleg","Clicked Favorite for line " + getAdapterPosition());
                if(favoriteIcon.getTag().equals("fav")){
                    favoriteIcon.setTag("notfav");
                    favoriteIcon.setImageResource(R.drawable.ic_star_border_black_36dp);
                    itemClickCallBack.onMyFavoriteDeleteClick(getAdapterPosition());
                }else{
                    favoriteIcon.setTag("fav");
                    favoriteIcon.setImageResource(R.drawable.ic_star_black_36dp);
                    itemClickCallBack.onMyFavoriteAddClick(getAdapterPosition());
                }


                String backgroundImageName = String.valueOf(favoriteIcon.getTag());
                Log.d("Oleg1","Fav tag is " + backgroundImageName);
             //   itemClickCallBack.onDeleteIconClick(getAdapterPosition());
            }
            else if(v.getId()==R.id.im_new_message){
                Log.d("LOG : ","Clicked message item on buyItemAdapter.java for item :" +getAdapterPosition());
                itemClickCallBack.onMessageIconClick(getAdapterPosition());
            }


        }
    }

}
