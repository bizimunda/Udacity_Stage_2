package udacity.hamid.picassogridviewproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hamid on 5/24/2017.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    public TrailerAdapter(@NonNull Activity context, ArrayList<Trailer> trailerArray) {
        super(context, 0, trailerArray);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.trailer, parent, false);
        }

        Trailer trailer=getItem(position);
        TextView tvTitle=(TextView)listItemView.findViewById(R.id.tv_trailer);
        tvTitle.setText(trailer.getKey());

        return listItemView;
    }
}
