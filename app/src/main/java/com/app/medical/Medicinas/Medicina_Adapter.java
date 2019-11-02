package com.app.medical.Medicinas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.medical.R;

import java.util.ArrayList;

public class Medicina_Adapter extends RecyclerView.Adapter<Medicina_Adapter.ViewHolderDatos> {

    ArrayList<Medicina_model> listDatos;

    public Medicina_Adapter(ArrayList<Medicina_model> listDatos) {
        this.listDatos = listDatos;
    }


    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_medicinas,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombre_medicina.setText(listDatos.get(position).getNombre());
        holder.dosis.setText(listDatos.get(position).getDosis());
        holder.imagen.setImageResource(listDatos.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombre_medicina, dosis;
        ImageView imagen;

        public ViewHolderDatos(View itemView) {
            super(itemView);

            nombre_medicina = (TextView) itemView.findViewById(R.id.medicina_name);
            dosis = (TextView) itemView.findViewById(R.id.medicina_dosis);
            imagen = (ImageView) itemView.findViewById(R.id.medicina_imagen);
        }

    }
}
