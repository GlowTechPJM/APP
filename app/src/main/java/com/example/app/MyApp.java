package com.example.app;

import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class MyApp extends Application {
    private static final String CHANNEL_ID = "Gestion de conexiones";
    private static int notificationCounter = 0;
    static WebSocketExample webSocketExample;
    static Context Mycontext;
    static String lista_conct_A ="";
    static String lista_conct_D ="";
    static String conexion = "";
    static String typeconexion = "";
    static String envio = "";
    static String id_envio = "";
    static String usuario = "";
    static String ip = "";
    static MenuItem receiveMenuItem;

    public static void createNotificationChannel(Context context) {
        CharSequence name = "conexion";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription("");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }
    public static void showNotification(Context context, String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Generar un ID de notificación único
        int uniqueId = ++notificationCounter;

        // El primer parámetro es el ID de la notificación
        notificationManager.notify(uniqueId, builder.build());
    }
    public static void lista_conetados(Context context,String android, String desk) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] android_list = android.split(";");
        String[] desk_list = desk.split(";");
        String mensaje = "Usuario de Android totales: "+ android_list.length+"\n";
        for (String value : android_list) {
            mensaje += value + "\n";
        }
        mensaje+="Usuario de Pc totales: "+ android_list.length+"\n";
        for (String s : desk_list) {
            mensaje += s + "\n";
        }
        builder.setMessage(mensaje)
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Acción a realizar cuando se hace clic en "No"
                        dialog.dismiss(); // Cierra el AlertDialog
                    }
                });

        // Crea y muestra el AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
