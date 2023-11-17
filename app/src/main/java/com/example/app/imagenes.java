package com.example.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class imagenes extends AppCompatActivity {

    private WebSocketExample webSocketExample;
    private ArrayList<Record> records;
    private ImageAdapter adapter;

    class Record {
        public int imagenResource;

        public Record(int _imagenResource) {
            imagenResource = _imagenResource;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagenes);
        webSocketExample = MyApp.webSocketExample;

        records = new ArrayList<>();
        imgs();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ImageAdapter(this, records);
        recyclerView.setAdapter(adapter);

        Button disconnectButton = findViewById(R.id.disconnect2);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomain();
            }
        });

        Button volverButton = findViewById(R.id.volversel);
        volverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toseleccion();
            }
        });
    }

    public void imgs() {
        int[] imagenes = {R.drawable.img1, R.drawable.img2, R.drawable.img3};
        for (int imagen : imagenes) {
            records.add(new Record(imagen));
        }
    }

    public void toseleccion() {
        Intent intent = new Intent(imagenes.this, selecion.class);
        startActivity(intent);
        finish();
    }

    private void tomain() {
        Intent intent = new Intent(imagenes.this, MainActivity.class);
        startActivity(intent);
        webSocketExample.disconnect();
        finish();
    }

    // Adapter class
    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

        private ArrayList<Record> records;
        private Context context; // Agregamos el contexto para mostrar el Toast

        public ImageAdapter(Context context, ArrayList<Record> records) {
            this.context = context;
            this.records = records;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listimgs, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            int imagenResId = records.get(position).imagenResource;
            holder.imageView.setImageResource(imagenResId);

            // Agregamos el OnClickListener para mostrar el Toast y obtener la representación en base64
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la imagen en base64
                    String base64Image = getBase64Image(imagenResId);
                    // Mostrar el Toast con la representación en base64
                    enviarMensajeJsonAlServidor(base64Image);
                    Toast.makeText(context, "imagen enviada", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return records.size();
        }

        public class ImageViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imagen);
            }
        }
    }
    private void enviarMensajeJsonAlServidor(String mensaje) {
        if (webSocketExample != null) {
            webSocketExample.sendJsonimg(mensaje);
        }
    }
    // Método para obtener la representación en base64 de una imagen
    private String getBase64Image(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}


