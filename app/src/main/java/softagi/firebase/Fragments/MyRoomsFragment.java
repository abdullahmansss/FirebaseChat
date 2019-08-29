package softagi.firebase.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import softagi.firebase.ChatActivity;
import softagi.firebase.Models.RoomModel;
import softagi.firebase.R;

public class MyRoomsFragment extends Fragment
{
    View view;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    List<RoomModel> roomModels;

    roomAdapter adapter;

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.myrooms_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        roomModels = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        getRooms(user.getUid());
    }

    private void getRooms(String id)
    {
        databaseReference.child("MyRooms").child(id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                roomModels.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    RoomModel roomModel = dataSnapshot1.getValue(RoomModel.class);
                    roomModels.add(roomModel);
                }

                adapter = new roomAdapter(roomModels);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    class roomAdapter extends RecyclerView.Adapter<roomAdapter.roomVH>
    {
        List<RoomModel> roomModels;

        roomAdapter(List<RoomModel> roomModels)
        {
            this.roomModels = roomModels;
        }

        @NonNull
        @Override
        public roomAdapter.roomVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.room_item, parent, false);
            return new roomAdapter.roomVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull roomAdapter.roomVH holder, final int position)
        {
            final RoomModel roomModel = roomModels.get(position);
            final String name = roomModel.getRoom_name();
            final String id = roomModel.getRoom_id();

            holder.room_name.setText(name);

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("chat", id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return roomModels.size();
        }

        class roomVH extends RecyclerView.ViewHolder
        {
            TextView room_name;
            Button join_room;

            roomVH(@NonNull View itemView)
            {
                super(itemView);

                room_name = itemView.findViewById(R.id.room_name_txt);
                join_room = itemView.findViewById(R.id.join_room_btn);

                join_room.setVisibility(View.GONE);
            }
        }
    }
}
