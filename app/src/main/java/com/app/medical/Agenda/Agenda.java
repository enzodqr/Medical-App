package com.app.medical.Agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.medical.R;

public class Agenda extends AppCompatActivity {

    Button boton;
    //para el modal de editar
    Dialog pop_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        //para el modal editar
        pop_up = new Dialog(this);

        boton = (Button) findViewById(R.id.btn_prueba_popUp);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_modal(view);
            }
        });
    }

    public void show_modal(View v){
        TextView cerrar;
        ImageButton btn_cancel;

        pop_up.setContentView(R.layout.edit_popup);
        cerrar = (TextView) pop_up.findViewById(R.id.txt_cerrar_popup);
        cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up.dismiss();
            }
        });
        pop_up.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop_up.show();

        btn_cancel = (ImageButton) pop_up.findViewById(R.id.btn_cancelEdit);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_up.dismiss();
            }
        });

    }
}
