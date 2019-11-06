package com.app.medical.Farmacias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.medical.R;

import java.util.ArrayList;

public class Adapter_Farmacia extends RecyclerView.Adapter<Adapter_Farmacia.ViewHolderDatos>
    implements View.OnClickListener{

    ArrayList<Farmacia_Model> listDatos;
    private View.OnClickListener listener;

    public Adapter_Farmacia(ArrayList<Farmacia_Model> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_farmacias, null, false);

        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.imagen.setImageResource(listDatos.get(position).getImagen());
        holder.nombre.setText(listDatos.get(position).getNombre());
        holder.direccion.setText(listDatos.get(position).getDireccion());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre, direccion;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            imagen = (ImageView) itemView.findViewById(R.id.imagenFarmacia);
            nombre = (TextView) itemView.findViewById(R.id.nombreFarmacia);
            direccion = (TextView) itemView.findViewById(R.id.direcciónFarmacia);

        }
    }
}
