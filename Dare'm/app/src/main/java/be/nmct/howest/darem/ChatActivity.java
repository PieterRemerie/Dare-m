package be.nmct.howest.darem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    Firebase reference1, reference2;
    ArrayList<String> friends;
    Challenge challenge;


    private ListView listView;
    private View btnSend;
    boolean myMessage = true;
    private List<ChatBubble> chatBubbles;
    private ArrayAdapter<ChatBubble> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatBubbles = new ArrayList<>();
        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = findViewById(R.id.btn_chat_send);
        messageArea = (EditText)findViewById(R.id.msg_type);

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
        }else{
            friends = (ArrayList<String>) savedInstanceState.getSerializable("friends");
        }
        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://gastleshowest2017-dc94f.firebaseio.com/messages/" + challenge.getName() +"/"+ AccessToken.getCurrentAccessToken().getUserId() + "_friends" );
        reference2 = new Firebase("https://gastleshowest2017-dc94f.firebaseio.com/messages/"+ challenge.getName() +"friends_" + AccessToken.getCurrentAccessToken().getUserId() );


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", AccessToken.getCurrentAccessToken().getUserId());
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                    /*ChatBubble chatBubble = new ChatBubble(messageText, myMessage);
                    chatBubbles.add(chatBubble);
                    adapter.notifyDataSetChanged();
                    messageArea.setText("");
                    if(myMessage){
                        myMessage = false;
                    }else{
                        myMessage = true;
                    }*/
                }
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                if(AccessToken.getCurrentAccessToken().getUserId().equals(userName)){
                    //addMessageBox("You:-\n" + message, 1);
                    ChatBubble chatBubble = new ChatBubble(message,userName, 1);
                    chatBubbles.add(chatBubble);
                    adapter.notifyDataSetChanged();
                    messageArea.setText("");


                }else{
                    ChatBubble chatBubble = new ChatBubble(message,"friend", 2);
                    chatBubbles.add(chatBubble);
                    adapter.notifyDataSetChanged();
                    messageArea.setText("");
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


    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            //textView.setBackgroundResource(R.drawable.bubble);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            //textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        linearLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
