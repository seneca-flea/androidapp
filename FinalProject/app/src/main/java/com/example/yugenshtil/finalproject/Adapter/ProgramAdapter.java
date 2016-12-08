package com.example.yugenshtil.finalproject.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.Filter.Program;

import java.util.ArrayList;
import java.util.List;

import static com.example.yugenshtil.finalproject.Filter.ProgramFilter.selectedPrograms;

/**
 * Created by Oleg Mytryniuk on 22/11/16.
 */

public class ProgramAdapter extends ArrayAdapter<Program> {

    private List<Program> list;
    private List<Program> listtemp;
    private final Activity context;
    boolean checkAll_flag = false;
    boolean checkItem_flag = false;


    public ProgramAdapter(Activity context, List<Program> list) {
        super(context, R.layout.program_info, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            convertView = inflator.inflate(R.layout.program_info, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.programName);
            viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBoxProgram);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    list.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    if(buttonView.isChecked()){
                        selectedPrograms.add(list.get(getPosition).getName());
                    }
                    else {
                        selectedPrograms.remove(list.get(getPosition).getName());
                    }

                }
            });
            convertView.setTag(viewHolder);
            convertView.setTag(R.id.programName, viewHolder.text);
            convertView.setTag(R.id.checkBoxProgram, viewHolder.checkbox);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.checkbox.setTag(position); // This line is important.

        viewHolder.text.setText(list.get(position).getName());
        viewHolder.checkbox.setChecked(list.get(position).isSelected());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                listtemp = new ArrayList<Program>();
                FilterResults filterResults = new FilterResults();

                for(Program p : list){
                    if(p.getName().contains(constraint)){
                        listtemp.add(p);

                    }

                }

                filterResults.values = listtemp;
                filterResults.count = listtemp.size();

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Program>)results.values;
                notifyDataSetChanged();

            }

        };
    }




}