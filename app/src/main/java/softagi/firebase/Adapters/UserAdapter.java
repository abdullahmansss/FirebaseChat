package softagi.firebase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import softagi.firebase.Models.UserModel;
import softagi.firebase.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH>
{
    List<UserModel> userModels;
    Context context;

    public UserAdapter(List<UserModel> userModels, Context context)
    {
        this.userModels = userModels;
        this.context = context;
    }

    @NonNull
    @Override
    public UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserVH holder, int position)
    {
        UserModel userModel = userModels.get(position);

        String name = userModel.getUsername();
        String email = userModel.getEmail();
        String mobile = userModel.getMobile();

        holder.user_name.setText(name);
        holder.user_email.setText(email);
        holder.user_mobile.setText(mobile);
    }

    @Override
    public int getItemCount()
    {
        return userModels.size();
    }

    class UserVH extends RecyclerView.ViewHolder
    {
        TextView user_name,user_email,user_mobile;

        UserVH(@NonNull View itemView)
        {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_name);
            user_email = itemView.findViewById(R.id.user_email);
            user_mobile = itemView.findViewById(R.id.user_mobile);
        }
    }
}
