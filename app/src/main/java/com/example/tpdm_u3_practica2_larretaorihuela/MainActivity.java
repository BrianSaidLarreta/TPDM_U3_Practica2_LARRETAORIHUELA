package com.example.tpdm_u3_practica2_larretaorihuela;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button productos, sucursales;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productos  = findViewById(R.id.btnP);
        sucursales = findViewById(R.id.btnS);

        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prod = new Intent(v.getContext(), MProductos.class);
                startActivity(prod);
            }
        });
        sucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suc = new Intent(v.getContext(), MSucursales.class);
                startActivity(suc);
            }
        });

    }
}
