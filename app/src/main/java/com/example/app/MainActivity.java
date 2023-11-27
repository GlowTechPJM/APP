package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;

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
        ip.setText(MyApp.ip);
        Button connectar = findViewById(R.id.connectar);
        connectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                conctarse();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem myButton = menu.findItem(R.id.action_button);
        myButton.setVisible(false);
        return true;
    }
    private void conctarse() {
        String ipText = ip.getText().toString();
        MyApp.ip = ipText;
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
                toinicio();
            } else {
                Toast.makeText(this, "No se pudo establecer la conexión WebSocket", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "La dirección IP introducida no es válida", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidIPAddress(String ip) {
        String ipAddressRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        return ip.matches(ipAddressRegex);
    }


    public void toinicio() {
        String ipText = ip.getText().toString();
        Intent intent = new Intent(MainActivity.this, int_sesio.class);
        MyApp.webSocketExample = webSocketExample;
        intent.putExtra("ip",ipText);
        startActivity(intent);
        finish();
    }

}
