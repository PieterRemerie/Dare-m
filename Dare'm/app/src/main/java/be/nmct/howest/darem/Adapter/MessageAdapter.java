package be.nmct.howest.darem.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nmct.howest.darem.ChatActivity;
import be.nmct.howest.darem.Model.ChatBubble;
import be.nmct.howest.darem.R;

/**
 * Created by Piete_000 on 19/12/2017.
 */

public class MessageAdapter extends ArrayAdapter<ChatBubble> {

    private Activity activity;
    private List<ChatBubble> messages;
    private FirebaseStorage mStorage;
    private Bitmap bitmap;
    private Map<String, Bitmap> mBitmapCache = new HashMap<String, Bitmap>();

    public MessageAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ChatBubble> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutResource = 0; // determined by view type
        final ChatBubble ChatBubble = getItem(position);
        int viewType = getItemViewType(position);

        if (ChatBubble.getType() ==  1) {
                convertView = inflater.inflate(R.layout.left_chat_bubble, parent, false);
        } else if(ChatBubble.getType() == 2) {
                convertView = inflater.inflate(R.layout.right_chat_bubble, parent, false);
        } else if(ChatBubble.getType() == 3){
            convertView = inflater.inflate(R.layout.left_image_chat, parent, false);
        } else if (ChatBubble.getType() == 4){
            convertView = inflater.inflate(R.layout.right_image_chat, parent, false);
        }
        holder = new ViewHolder(convertView);

        //set message content
        if(holder.msg != null && holder.name != null){
            holder.msg.setText(ChatBubble.getContent());
            holder.name.setText(ChatBubble.getName());
        }
        if(holder.image != null && holder.name != null){
            mStorage = FirebaseStorage.getInstance();

            if(mBitmapCache.containsKey(ChatBubble.getContent())){
                holder.image.setImageBitmap(mBitmapCache.get(ChatBubble.getContent()));
                holder.name.setText(ChatBubble.getName());
            }else{
                StorageReference storageReference = mStorage.getReferenceFromUrl(ChatBubble.getContent());
                final long ONE_MEGABYTE = 1024 * 1024;
                storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        mBitmapCache.put(ChatBubble.getContent().toString(), bitmap);
                        holder.image.setImageBitmap(bitmap);
                        holder.name.setText(ChatBubble.getName());
                    }

                });
            }
        }
        return convertView;
    }
    private void scaleImage(ImageView image, Bitmap bitmap, int newScale){
        int originalWidth = bitmap.getWidth();
        int originalHeight =  bitmap.getHeight();
        if(originalHeight > originalWidth){
            float scale =  (float) newScale / originalWidth;
            Integer newHeight = (int) Math.round(originalHeight * scale);
            image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            image.getLayoutParams().width = newScale;
            image.getLayoutParams().height = newHeight;
        }else{
            float scale =  (float) newScale / originalHeight;
            Integer newWidth = (int) Math.round(originalWidth * scale);
            image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            image.getLayoutParams().width = newWidth;
            image.getLayoutParams().height = newScale;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    private class ViewHolder{
        private TextView msg;
        private TextView name;
        private ImageView image;
        public ViewHolder(View v){

            msg = (TextView) v.findViewById(R.id.txt_msg);
            name = (TextView) v.findViewById(R.id.friendName);
            image = (ImageView) v.findViewById(R.id.imageChat);

        }
    }

}
