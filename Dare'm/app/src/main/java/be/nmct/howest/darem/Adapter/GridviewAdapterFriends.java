package be.nmct.howest.darem.Adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.nmct.howest.darem.Loader.HttpGetRequest;
import be.nmct.howest.darem.R;
import be.nmct.howest.darem.Transforms.CircleTransform;

import static java.security.AccessController.getContext;

/**
 * Created by Piete_000 on 2/12/2017.
 */

public class GridviewAdapterFriends extends BaseAdapter {


    private Context mContext;
    private final ArrayList<String> names;
    private final ArrayList<String> photos;
    private final ArrayList<String> friendId;

    public GridviewAdapterFriends(Context context, ArrayList<String> names, ArrayList<String> photos, ArrayList<String> friendId){
        mContext = context;
        this.names = names;
        this.photos = photos;
        this.friendId = friendId;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {


        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_view_items_friends, null);
            ImageView imageView = (ImageView) grid.findViewById(R.id.imageViewFriend);
            TextView textView = (TextView) grid.findViewById(R.id.txtFriendName);
            CheckBox checkBox = (CheckBox) grid.findViewById(R.id.checkboxAddFriends);
            textView.setText(names.get(position));
            Picasso.with(grid.getContext()).load(photos.get(position)).transform(new CircleTransform()).into(imageView);
            checkBox.setTag(friendId.get(position));

        } else{
            grid = (View) convertView;
        }
        return grid;

    }
}
