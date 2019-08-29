package softagi.firebase.Fragments;

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

import softagi.firebase.Models.RoomModel;
import softagi.firebase.R;
import softagi.firebase.RoomsActivity;

public class RoomsFragment extends Fragment
{
    View view;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    DividerItemDecoration dividerItemDecoration;

    List<RoomModel> roomModels;

    roomAdapter adapter;
    long mem;

    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.rooms_fragment, container, false);
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

        getRooms();
    }

    private void getRooms()
    {
        databaseReference.child("Rooms").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                roomModels.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    RoomModel roomModel = dataSnapshot1.getValue(RoomModel.class);
                    roomModels.add(roomModel);
                    getMembers(roomModel.getRoom_id());
                }

                adapter = new roomAdapter(roomModels,mem);
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
        long m;

        public roomAdapter(List<RoomModel> roomModels, long m) {
            this.roomModels = roomModels;
            this.m = m;
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

            holder.room_name.setText(name + " " + m);
            holder.join_room.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    databaseReference.child("Members").child(id).child(user.getUid()).setValue(true);
                    databaseReference.child("MyRooms").child(user.getUid()).child(id).setValue(roomModel);
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
            }
        }
    }

    private void getMembers(String id)
    {
        databaseReference.child("Members").child(id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                mem = dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }
}
