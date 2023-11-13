package com.example.app;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketExample extends WebSocketListener {

    private WebSocket webSocket;

    public WebSocketExample(String ipAddress) {
        String WS_URL = "ws://" + ipAddress + ":8080";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        webSocket = client.newWebSocket(request, this);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {

        Log.i("comprobar","Conexión abierta");
        notifyConnectionEstablished();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // Se ha recibido un mensaje
        // Puedes manejar el mensaje recibido aquí
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.i("comprobar","Conexión cerrada");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i("comprobar","Error en la conexión WebSocket: " + t.getMessage());
    }

    private void notifyConnectionEstablished() {
        Log.i("comprobar","Conexión establecida con éxito");
    }
}
