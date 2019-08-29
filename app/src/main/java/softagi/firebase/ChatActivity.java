package softagi.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import softagi.firebase.Models.MessageModel;
import softagi.firebase.Models.UserModel;

public class ChatActivity extends AppCompatActivity
{
    String id,name,myId;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    EditText msg_field;
    Button send_btn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<MessageModel> m;
    chatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        id = getIntent().getStringExtra("chat");

        recyclerView = findViewById(R.id.recyclerview);
        msg_field = findViewById(R.id.chat_field);
        send_btn = findViewById(R.id.send_btn);

        layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        m = new ArrayList<>();

        getChat(id);
        getData();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String c = msg_field.getText().toString();

                if (TextUtils.isEmpty(c))
                {
                    Toast.makeText(getApplicationContext(), "enter message", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessage(c,id);
            }
        });
    }

    private void getData()
    {
        myId = FirebaseAuth.getInstance().getUid();
        databaseReference.child("Users").child(myId).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                name = userModel.getUsername();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)

            {

            }
        });
    }

    private void sendMessage(String c, String id)
    {
        MessageModel messageModel = new MessageModel(c,name,myId);
        String msg_id = databaseReference.child("Chats").child(id).push().getKey();
        databaseReference.child("Chats").child(id).child(msg_id).setValue(messageModel);
    }

    private void getChat(String id)
    {
        databaseReference.child("Chats").child(id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                m.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    MessageModel messageModel = dataSnapshot1.getValue(MessageModel.class);
                    m.add(messageModel);
                }

                adapter = new chatAdapter(m);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(m.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    class chatAdapter extends RecyclerView.Adapter<chatAdapter.chatVH>
    {
        List<MessageModel> messageModels;

        public chatAdapter(List<MessageModel> messageModels)
        {
            this.messageModels = messageModels;
        }

        @NonNull
        @Override
        public chatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.mesaage_item, parent, false);
            return new chatVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull chatVH holder, int position)
        {
            MessageModel messageModel = messageModels.get(position);

            String name = messageModel.getName();
            String body = messageModel.getMsg();
            String id = messageModel.getId();

            holder.name.setText(name);
            holder.body.setText(body);

            if (id.equals(myId))
            {
                holder.linearLayout.setGravity(Gravity.END);
                holder.linearLayout2.setBackgroundColor(getResources().getColor(R.color.chat));
            }
        }

        @Override
        public int getItemCount()
        {
            return messageModels.size();
        }

        class chatVH extends RecyclerView.ViewHolder
        {
            TextView name,body;
            LinearLayout linearLayout,linearLayout2;

            chatVH(@NonNull View itemView)
            {
                super(itemView);

                name = itemView.findViewById(R.id.msg_name);
                body = itemView.findViewById(R.id.msg_body);
                linearLayout = itemView.findViewById(R.id.chat_lin);
                linearLayout2 = itemView.findViewById(R.id.chat_lin2);
            }
        }
    }
}
