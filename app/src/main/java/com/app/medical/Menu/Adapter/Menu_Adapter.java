package com.app.medical.Menu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.medical.R;

import java.util.ArrayList;

public class Menu_Adapter extends RecyclerView.Adapter<Menu_Adapter.View_Holder_Options>{

    ArrayList<String> options_list;

    public Menu_Adapter(ArrayList<String> options_list) {
        this.options_list = options_list;
    }

    @NonNull
    @Override
    public Menu_Adapter.View_Holder_Options onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_menu, null, false);
        return new View_Holder_Options(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Menu_Adapter.View_Holder_Options holder, int position) {
        holder.set_options(options_list.get(position));
    }

    @Override
    public int getItemCount() {
        return options_list.size();
    }

    public class View_Holder_Options extends RecyclerView.ViewHolder {

        TextView option;

        public View_Holder_Options(@NonNull View itemView) {
            super(itemView);

            option = itemView.findViewById(R.id.menu_options);
        }

        public void set_options(String options){
            option.setText(options);
        }
    }
}
