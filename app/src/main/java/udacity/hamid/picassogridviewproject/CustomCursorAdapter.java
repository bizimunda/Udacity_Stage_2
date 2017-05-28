package udacity.hamid.picassogridviewproject;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import udacity.hamid.picassogridviewproject.data.MovieContract;

/**
 * Created by hamid on 15/05/2017.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public CustomCursorAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fav_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry._ID);
        int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int movieIdIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int descriptionIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIPTION);
        int imageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE);

        mCursor.moveToPosition(position); // get to the right location in the cursor
        final int id = mCursor.getInt(idIndex);
        String title = mCursor.getString(titleIndex);
        String description = mCursor.getString(descriptionIndex);
        String image = mCursor.getString(imageIndex);
        int movieId = mCursor.getInt(movieIdIndex);

        //Set values
        holder.itemView.setTag(id);
        holder.textViewTitle.setText(title);
        holder.textViewDescription.setText(description);
        Picasso.with(mContext)
                .load(image)
                .into(holder.imageView);

        String movieIdString = "" + movieId; // converts int to String
        holder.textViewId.setText(movieIdString);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewId;
        TextView textViewTitle;
        TextView textViewDescription;


        public TaskViewHolder(View itemView) {
            super(itemView);

            imageView=(ImageView)itemView.findViewById(R.id.imageViewProduct);
            textViewId=(TextView)itemView.findViewById(R.id.custom_tvId);
            textViewTitle=(TextView)itemView.findViewById(R.id.custom_tvTitle);
            textViewDescription=(TextView)itemView.findViewById(R.id.custom_tvDescription);

        }
    }


}
