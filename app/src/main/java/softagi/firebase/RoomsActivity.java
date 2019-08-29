package softagi.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import softagi.firebase.Adapters.UserAdapter;
import softagi.firebase.Fragments.MyRoomsFragment;
import softagi.firebase.Fragments.RoomsFragment;
import softagi.firebase.Models.RoomModel;
import softagi.firebase.Models.UserModel;

public class RoomsActivity extends AppCompatActivity
{
    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            Fragment [] fragments = new Fragment[]
                    {
                            new RoomsFragment(),
                            new MyRoomsFragment()
                    };

            String [] names = new String[]
                    {
                            "ROOMS",
                            "MY ROOMS"
                    };

            @NonNull
            @Override
            public Fragment getItem(int position)
            {
                return fragments [position];
            }

            @Override
            public int getCount()
            {
                return fragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position)
            {
                return names [position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
