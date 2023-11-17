package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class selecion extends AppCompatActivity {
    private WebSocketExample webSocketExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccion);
        webSocketExample = MyApp.webSocketExample;

        Button textos = findViewById(R.id.textos);
        textos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totextos();
            }
        });

        Button disconnect = findViewById(R.id.disconnect3);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomain();
            }
        });

        Button img = findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toimgenes();
            }
        });

    }
    private void totextos() {
        Intent intent = new Intent(selecion.this, mensajeria.class);
        startActivity(intent);
        finish();
    }

    private void toimgenes() {
        Intent intent = new Intent(selecion.this, imagenes.class);
        startActivity(intent);
        finish();
    }

    private void tomain() {
        Intent intent = new Intent(selecion.this, MainActivity.class);
        startActivity(intent);
        webSocketExample.disconnect();
        finish();
    }

}
