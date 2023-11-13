package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class lista extends AppCompatActivity {

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

    ArrayList<item> items;
    ArrayAdapter<item> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_mensajes);

        items = new ArrayList<>();

        // Leer el contenido del archivo y agregar elementos a la lista
        try {
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

        Button volver = findViewById(R.id.volver);
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomensajeria();
            }
        });
    }

    private void readFromFile(String fileName) throws IOException {
        InputStream inputStream = openFileInput(fileName);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            // Dividir la línea en partes utilizando el punto y coma como delimitador
            String[] parts = line.split(";");
            if (parts.length == 3) {
                // Agregar un nuevo elemento a la lista utilizando los datos del archivo
                items.add(new item(parts[0], parts[1], parts[2]));
            }
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
    }

    public void tomensajeria() {
        Intent intent = new Intent(lista.this, mensajeria.class);
        startActivity(intent);
    }
}

