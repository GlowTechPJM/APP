package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class selecion extends AppCompatActivity {
    private WebSocketExample webSocketExample;
    private MenuItem receiveMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccion);
        webSocketExample = MyApp.webSocketExample;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_button) {
            // Aquí maneja el clic en tu botón de la ActionBar
            // Puedes agregar cualquier lógica que desees ejecutar al hacer clic en el botón.

            Toast.makeText(this, "Botón de ActionBar clickeado", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
