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

public class int_sesio extends AppCompatActivity {
    private Context context;
    private EditText usuario, contra;
    private WebSocketExample webSocketExample;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session);
        webSocketExample = MyApp.webSocketExample;
        MyApp.Mycontext = this;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem myButton = menu.findItem(R.id.action_button);
        myButton.setVisible(false);
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

        if(webSocketExample.isValid()){

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
        finish();
    }
    private void enviarMensajeJsonAlServidor(String usu, String contra) {
        if (webSocketExample != null) {
            webSocketExample.sendJsonMessageusu(usu,contra);
        }
    }
}
