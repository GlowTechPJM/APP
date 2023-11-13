package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

public class mensajeria extends AppCompatActivity {
    private EditText texto;
    private WebSocketExample webSocketExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajeria);
        texto = findViewById(R.id.texto);

        Button enviar = findViewById(R.id.enviar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviado();
            }
        });

        Button disconnect = findViewById(R.id.disconnect);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomain();
            }
        });

        Button lista = findViewById(R.id.lista);
        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tolista();
            }
        });
    }

    private void enviado() {
        Date currentTime = Calendar.getInstance().getTime();
        String dia = currentTime.toString().substring(4, 10);
        String hora = currentTime.toString().substring(11, 19);
        String text = texto.getText().toString();
        String guardar = dia + ";" + hora + ";" + text + "\n";
        texto.setText("");
        String ipAddress = "192.168.0.22";
        webSocketExample = new WebSocketExample(ipAddress);
        enviarMensajeJsonAlServidor(text);
        try {
            FileOutputStream fileOutputStream = openFileOutput("textos.txt", Context.MODE_APPEND);
            fileOutputStream.write(guardar.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "mensaje enviado", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void enviarMensajeJsonAlServidor(String mensaje) {
        if (webSocketExample != null) {
            webSocketExample.sendJsonMessage(mensaje);
        }
    }
    private void tolista() {
        Intent intent = new Intent(mensajeria.this, lista.class);
        startActivity(intent);
    }
    private void tomain() {
        Intent intent = new Intent(mensajeria.this, MainActivity.class);
        startActivity(intent);
    }
}

