package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText ip;
    private String ipText;
    private WebSocketExample webSocketExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip = findViewById(R.id.ip);

        Button connectar = findViewById(R.id.connectar);
        connectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                conctarse();
            }
        });

    }

    private void conctarse() {
        String ipText = ip.getText().toString();
        if (isValidIPAddress(ipText)) {
            webSocketExample = new WebSocketExample(ipText);
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            boolean comp = webSocketExample.isConnected();
            Log.i("conexion", String.valueOf(comp));
            if (webSocketExample.isConnected()) {
                Toast.makeText(this, "Conexión con éxito", Toast.LENGTH_SHORT).show();
                tomensajeria();
            } else {
                Toast.makeText(this, "No se pudo establecer la conexión WebSocket", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "La dirección IP introducida no es válida", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para verificar si una cadena es una dirección IP válida
    private boolean isValidIPAddress(String ip) {
        String ipAddressRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        return ip.matches(ipAddressRegex);
    }


    public void tomensajeria() {
        Intent intent = new Intent(MainActivity.this, mensajeria.class);
        startActivity(intent);
    }

}
