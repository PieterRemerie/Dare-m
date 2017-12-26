package be.nmct.howest.darem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.nmct.howest.darem.Adapter.MessageAdapter;
import be.nmct.howest.darem.Model.Challenge;
import be.nmct.howest.darem.Model.ChatBubble;

public class ChatActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2, reference3, reference4;
    ArrayList<String> friends;
    Challenge challenge;
    ImageView btnImage;
    ImageView btnCamera;

    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;
    private static final int CAMERA_INTENT = 2;
    private ProgressDialog progressDialog;
    //Bitmap bitmap;

    private ListView listView;
    private View btnSend;
    boolean myMessage = true;
    private List<ChatBubble> chatBubbles;
    private ArrayAdapter<ChatBubble> adapter;
    private JSONArray object = null;
    HashMap<String, String> mapje = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        progressDialog = new ProgressDialog(this);
        chatBubbles = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = findViewById(R.id.btn_chat_send);
        messageArea = (EditText)findViewById(R.id.msg_type);
        btnImage = (ImageView) findViewById(R.id.btnImage);
        btnCamera = (ImageView) findViewById(R.id.btnCamera);

        adapter = new MessageAdapter(this, R.layout.left_chat_bubble, chatBubbles);
        listView.setAdapter(adapter);

        /*linearLayout = (LinearLayout) findViewById(R.id.layout1);
        relativeLayout = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.btn_chat_send);
        messageArea = (EditText)findViewById(R.id.msg_type);
        scrollView = (ScrollView)findViewById(R.id.scrollView);*/

        if(savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            challenge = bundle.getParcelable("challenge");
            mapje = (HashMap<String, String>)bundle.getSerializable("friends");


        }
        Firebase.setAndroidContext(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        reference1 = new Firebase("https://gastleshowest2017-dc94f.firebaseio.com/messages/" + challenge.getName() +"/_friends" );
        //reference2 = new Firebase("https://gastleshowest2017-dc94f.firebaseio.com/messages/"+ challenge.getName() +"friends_" + AccessToken.getCurrentAccessToken().getUserId() );
        reference3 = new Firebase("https://gastleshowest2017-dc94f.firebaseio.com/chat/" + challenge.getName() + "/users/");
        reference4 = new Firebase("https://gastleshowest2017-dc94f.firebaseio.com/chat/"+ challenge.getName() + "/messages/");

        //reference3.push().setValue(map);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", AccessToken.getCurrentAccessToken().getUserId());
                    reference4.push().setValue(map);
                }
            }
        });
        reference4.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String naam = mapje.get(userName);
                if(AccessToken.getCurrentAccessToken().getUserId().equals(userName)){
                    addMessageBox(message, "YOU", 1);


                }else{
                    addMessageBox(message, naam, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_INTENT);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            progressDialog.setMessage("Uploading Image ...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            try {
                UploadImage(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == CAMERA_INTENT && resultCode == RESULT_OK){
            try {
                UploadImage(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMessageBox(String message, String name, int type){
        ChatBubble chatBubble = new ChatBubble(message,name, type);
        chatBubbles.add(chatBubble);
        adapter.notifyDataSetChanged();
        messageArea.setText("");
    }
    private void UploadImage(Intent data) throws IOException {


        Uri uri = data.getData();
        StorageReference filepath = mStorage.child("photos").child(AccessToken.getCurrentAccessToken().getUserId() + System.currentTimeMillis());
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, bytes);
        byte[] compressed = bytes.toByteArray();
        UploadTask uploadTask = filepath.putBytes(compressed);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String naam = mapje.get(AccessToken.getCurrentAccessToken().getUserId());
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                if(AccessToken.getCurrentAccessToken().getUserId().equals(AccessToken.getCurrentAccessToken().getUserId())){
                    addMessageBox(downloadUri.toString(), "YOU", 3);
                    progressDialog.dismiss();

                }else{
                    addMessageBox(downloadUri.toString(), naam, 4);

                }
            }
        });
    }
}
