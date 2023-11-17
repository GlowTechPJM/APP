package com.example.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class lista extends AppCompatActivity {
    private WebSocketExample webSocketExample;

    class item {
        public String texto;
        public String dia;
        public String hora;

        public item(String dia, String hora, String texto) {
            this.texto = texto;
            this.dia = dia;
            this.hora = hora;
        }
    }
    private String selectedDia;
    private String selectedHora;
    private String selectedTexto;

    ArrayList<item> items;
    ArrayAdapter<item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_mensajes);
        webSocketExample = MyApp.webSocketExample;


        items = new ArrayList<>();


        // Leer el contenido del archivo y agregar elementos a la lista
        try {
            read("textos.txt");
            readFromFile("textos.txt"); // Reemplaza con el nombre correcto del archivo
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(items, new Comparator<item>() {
            @Override
            public int compare(item item1, item item2) {
                // Comparar las fechas y horas para ordenar de forma descendente
                int dateComparison = item2.dia.compareTo(item1.dia);
                if (dateComparison == 0) {
                    return item2.hora.compareTo(item1.hora);
                }
                return dateComparison;
            }
        });

        adapter = new ArrayAdapter<item>(this, R.layout.listitem, items) {
            public View getView(int pos, View convertView, ViewGroup container) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.listitem, container, false);
                }
                ((TextView) convertView.findViewById(R.id.dia)).setText(getItem(pos).dia);
                ((TextView) convertView.findViewById(R.id.hora)).setText(getItem(pos).hora);
                ((TextView) convertView.findViewById(R.id.texto)).setText(getItem(pos).texto);
                return convertView;
            }
        };
        ListView lv = findViewById(R.id.recordsView);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                item selectedItem = items.get(position);
                selectedDia = selectedItem.dia;
                selectedHora = selectedItem.hora;
                selectedTexto = selectedItem.texto;
                String texto_eliminar = selectedDia +";"+selectedHora +";"+selectedTexto;

                showYesNoAlertDialog(selectedTexto,texto_eliminar);

                // Mostrar un Toast con la información del elemento seleccionado



            }
        });

        Button volver = findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomensajeria();
            }
        });
    }
    private void showYesNoAlertDialog(String selectedTexto, String texto_eliminar) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Quieres reenviar el mensaje?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        enviado(selectedTexto);
                        readFromFileandDelete("textos.txt",texto_eliminar);
                        // Acción a realizar cuando se hace clic en "Sí"
                        Toast.makeText(lista.this, "El mensaje se ha reenviado", Toast.LENGTH_SHORT).show();
                        tomensajeria();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Acción a realizar cuando se hace clic en "No"
                        dialog.dismiss(); // Cierra el AlertDialog
                    }
                });

        // Crea y muestra el AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void read(String fileName) {
        try {
            InputStream inputStream = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            // Crear un mapa para rastrear textos y líneas correspondientes
            HashMap<String, String> textLinesMap = new HashMap<>();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Dividir la línea en partes utilizando el punto y coma como delimitador
                String[] parts = line.split(";");

                if (parts.length >= 3) {
                    for (int i = 3; i < parts.length; i++) {
                        parts[2] += ";" + parts[i];
                    }

                    // Actualizar la línea para el texto específico
                    textLinesMap.put(parts[2], line);
                }
            }

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();

            // Sobrescribir el archivo con las líneas restantes
            writeToFile(fileName, new ArrayList<>(textLinesMap.values()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile(String fileName) throws IOException {
        InputStream inputStream = openFileInput(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            // Dividir la línea en partes utilizando el punto y coma como delimitador
            String[] parts = line.split(";");

            if (parts.length >= 3) {
                for (int i = 3; i<parts.length;i++){
                    parts[2]+= ";"+ parts[i];
                }
                // Agregar un nuevo elemento a la lista utilizando los datos del archivo
                items.add(new item(parts[0], parts[1], parts[2]));
            }
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
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
    private void enviado(String texto) {
        Date currentTime = Calendar.getInstance().getTime();
        String dia = currentTime.toString().substring(4, 10);
        String hora = currentTime.toString().substring(11, 19);
        String text = texto;
        String guardar = dia + ";" + hora + ";" + text + "\n";
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
    public void tomensajeria() {

        Intent intent = new Intent(lista.this, mensajeria.class);
        startActivity(intent);
        finish();
    }
}

