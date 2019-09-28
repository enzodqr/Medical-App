package com.app.medical.Profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.medical.R;

import java.util.ArrayList;

public class Profile_Adapter extends RecyclerView.Adapter<Profile_Adapter.ViewHolderData>
    implements View.OnClickListener {

    ArrayList<Profile_Model> user_data;

    /* Test edit recycler*/
    private View.OnClickListener onClickListener;

    public Profile_Adapter(ArrayList<Profile_Model> user_data) {
        this.user_data = user_data;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item_list, null, false);

        /* Test edit recycler*/
        view.setOnClickListener(this);

        return new ViewHolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.title.setText(user_data.get(position).getTitle());
        holder.data.setText(user_data.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return user_data.size();
    }


    /* Test edit recycler*/
    public void setOnClickListener(View.OnClickListener listener){
        this.onClickListener = listener;
    }

    /* Test edit recycler*/
    @Override
    public void onClick(View view) {
        if(onClickListener!=null){
            onClickListener.onClick(view);
        }
    }

    public class ViewHolderData extends RecyclerView.ViewHolder {

        TextView title;
        TextView data;

        public ViewHolderData(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.profile_op_title);
            data = itemView.findViewById(R.id.profile_op_data);
        }
    }
}
