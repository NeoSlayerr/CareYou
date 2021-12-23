package com.example.pkb2021;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<DataDoctor> {

    public GridAdapter(Context context, ArrayList<DataDoctor> dataDoctorArrayList) {
        super(context, 0, dataDoctorArrayList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }

        DataDoctor dataDoctor = getItem(position);
        TextView doctorName = listItemView.findViewById(R.id.item_name);
        TextView occupation = listItemView.findViewById(R.id.occupation);
        ImageView profile_pic = listItemView.findViewById(R.id.grid_image);
        doctorName.setText(dataDoctor.getDoctorName());
        occupation.setText(dataDoctor.getSpeciality());
        profile_pic.setImageResource(getImageId(getContext(), dataDoctor.getprofilePic()));
        return listItemView;
    }

    public static int getImageId(Context context, String imageName) {
        return context.getResources().getIdentifier("drawable/" + imageName, null, context.getPackageName());
    }
}
