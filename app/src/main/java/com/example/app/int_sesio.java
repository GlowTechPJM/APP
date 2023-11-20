package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class int_sesio extends AppCompatActivity {
    private EditText usuario, contra;

    private WebSocketExample webSocketExample;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);

        usuario = findViewById(R.id.usuario);
        contra = findViewById(R.id.contra);
        usuario.setText(MyApp.usuario);

        Button iniciar = findViewById(R.id.Iniciar);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inciciar();
            }
        });


    }
    private void inciciar(){
        String usu = usuario.getText().toString();
        MyApp.usuario = usu;
        String past = contra.getText().toString();
        Intent intent = getIntent();
        webSocketExample = MyApp.webSocketExample;
        enviarMensajeJsonAlServidor(usu,past);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(webSocketExample.isBalid()){
            tomensajeria();
        }else {
            Toast.makeText(int_sesio.this, "usuario o contraseña incorectos", Toast.LENGTH_SHORT).show();
            tomain();

        }

    }
    private void tomain() {
        Intent intent = new Intent(int_sesio.this, MainActivity.class);
        startActivity(intent);
        webSocketExample.disconnect();
        finish();
    }
    public void tomensajeria() {
        Intent intent = new Intent(int_sesio.this, selecion.class);

        startActivity(intent);
    }
    private void enviarMensajeJsonAlServidor(String usu, String contra) {
        if (webSocketExample != null) {
            webSocketExample.sendJsonMessageusu(usu,contra);
        }
    }
}
