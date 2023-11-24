package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class mensajeria extends AppCompatActivity {
    private EditText texto;
    private WebSocketExample webSocketExample;
    private MenuItem receiveMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajeria);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        texto = findViewById(R.id.texto);
        Intent intent = getIntent();
        String ipAddress = intent.getStringExtra("ip");
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
                tolista(ipAddress);
            }
        });
        Button volver = findViewById(R.id.backsel);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toseleccion();
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


    private void readFromFileandDelete(String fileName, String text){
        try {
            InputStream inputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Crear una lista para almacenar las líneas del archivo
            ArrayList<String> lines = new ArrayList<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Dividir la línea en partes utilizando el punto y coma como delimitador
                String[] parts = line.split(";");

                if (parts.length >= 3) {
                    for (int i = 3; i < parts.length; i++) {
                        parts[2] += ";" + parts[i];
                    }
                    if (!parts[2].equals(text)) {
                        // Agregar la línea al ArrayList solo si no coincide con el texto que deseas eliminar
                        lines.add(line);
                    }
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            // Sobrescribir el archivo con las líneas restantes
            writeToFile(fileName, lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Método para escribir en el archivo
    private void writeToFile(String fileName, List<String> lines) throws IOException {
        FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        // Escribir las líneas en el archivo
        for (String line : lines) {
            bufferedWriter.write(line);
            bufferedWriter.newLine();  // Agregar un salto de línea después de cada línea
        }

        bufferedWriter.close();
        outputStreamWriter.close();
        outputStream.close();
    }

    private void enviado() {
        Date currentTime = Calendar.getInstance().getTime();
        String dia = currentTime.toString().substring(4, 10);
        String hora = currentTime.toString().substring(11, 19);
        String text = texto.getText().toString();
        String guardar = dia + ";" + hora + ";" + text + "\n";
        texto.setText("");
        Intent intent = getIntent();
        String ipAddress = intent.getStringExtra("ip");
        webSocketExample = MyApp.webSocketExample;
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
    private void tolista(String ipText) {
        Intent intent = new Intent(mensajeria.this, lista.class);
        intent.putExtra("ip",ipText);
        startActivity(intent);
        finish();
    }
    public void toseleccion() {
        Intent intent = new Intent(mensajeria.this, selecion.class);
        startActivity(intent);
        finish();
    }
    private void tomain() {
        Intent intent = new Intent(mensajeria.this, MainActivity.class);
        startActivity(intent);
        webSocketExample.disconnect();
        finish();
    }
}

