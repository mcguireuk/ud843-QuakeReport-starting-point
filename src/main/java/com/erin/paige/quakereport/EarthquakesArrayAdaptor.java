package com.erin.paige.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.erin.paige.quakereport.Earthquakes;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Shaun McGuire on 14/04/2017.
 */

public class EarthquakesArrayAdaptor extends ArrayAdapter<Earthquakes> {

    public EarthquakesArrayAdaptor(Context context, ArrayList<Earthquakes> earthquakes) {
        super(context, 0, earthquakes);
    }



    private int getMagnitudeColour(double magnitude){

        int magColour;

        switch ((int) magnitude){

            default:
            case 1:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude1);
            break;
            case 2:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude2);
            break;
            case 3:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude3);
            break;
            case 4:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude4);
            break;
            case 5:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude5);
            break;
            case 6:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude6);
            break;
            case 7:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude7);
            break;
            case 8:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude8);
            break;
            case 9:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude9);
            break;
            case 10:
                magColour = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
            break;


        }
        return magColour;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Earthquakes earthquake = getItem(position);

        if (convertView == null) {

            LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
             convertView = inflater.inflate(R.layout.list_item, parent, false);
            }

            String toSplit = earthquake.getPlace();
        String[] place = {"", ""};

        if(toSplit.contains("of")){
            String[] split = toSplit.split(("of "));
            place[0] = split[0] + "of";
            place[1] = split[1];
        } else {
            place[0] = "Near the ";
            place[1] = toSplit;
        }

        DecimalFormat formatter = new DecimalFormat("0.0");

        TextView magView = (TextView) convertView.findViewById(R.id.mag_view);
        GradientDrawable magCicle = (GradientDrawable) magView.getBackground();
        magCicle.setColor(getMagnitudeColour(earthquake.getMag()));

        magView.setText(formatter.format(earthquake.getMag()));

        TextView offsetPlaceView = (TextView) convertView.findViewById(R.id.offset_place_view);
        offsetPlaceView.setText(place[0]);

        TextView primaryPlaceView = (TextView) convertView.findViewById(R.id.primary_place_view);
        primaryPlaceView.setText(place[1]);

        TextView dateView = (TextView) convertView.findViewById(R.id.date_view);
        dateView.setText(earthquake.getDate());

        TextView timeView = (TextView) convertView.findViewById(R.id.time_view);
        timeView.setText(earthquake.getTime());



        return convertView;

        }
    }

