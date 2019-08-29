package softagi.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

import softagi.firebase.Models.RoomModel;
import softagi.firebase.Models.UserModel;

public class StartActivity extends AppCompatActivity
{
    EditText room_name_field;
    TextView email_txt,username_txt,mobile_txt,address_txt,textView;
    String email,username,mobile,address,room_name;

    FirebaseAuth auth;

    FirebaseDatabase  firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        room_name_field = findViewById(R.id.room_name_field);
        textView = findViewById(R.id.user_id_txt);
        email_txt = findViewById(R.id.email_txt);
        username_txt = findViewById(R.id.username_txt);
        mobile_txt = findViewById(R.id.mobile_txt);
        address_txt = findViewById(R.id.address_txt);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        String id = auth.getCurrentUser().getUid();

        getData(id);
        textView.setText(id);
    }

    private void getData(String id)
    {
        databaseReference.child("Users").child(id).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);

                email = userModel.getEmail();
                username = userModel.getUsername();
                mobile = userModel.getMobile();
                address = userModel.getAddress();

                email_txt.setText(email);
                username_txt.setText(username);
                mobile_txt.setText(mobile);
                address_txt.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void users(View view)
    {
        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
    }

    public void createroom(View view)
    {
        room_name = room_name_field.getText().toString();

        if (TextUtils.isEmpty(room_name))
        {
            Toast.makeText(getApplicationContext(), "enter room name", Toast.LENGTH_SHORT).show();
            return;
        }

        createRoom(room_name);
    }

    private void createRoom(String room_name)
    {
        String room_id = databaseReference.child("Rooms").push().getKey();
        RoomModel roomModel = new RoomModel(room_name,room_id);
        databaseReference.child("Rooms").child(room_id).setValue(roomModel);
    }

    public void showrooms(View view)
    {
        startActivity(new Intent(getApplicationContext(), RoomsActivity.class));
    }
}