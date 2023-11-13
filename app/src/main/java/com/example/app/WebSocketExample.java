package com.example.app;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketExample extends WebSocketListener {

    private WebSocket webSocket;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean isConnected = false;


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
        isConnected = true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // Se ha recibido un mensaje
        // Puedes manejar el mensaje recibido aquí
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.i("comprobar","Conexión cerrada");
        isConnected = false;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        Log.i("comprobar","Error en la conexión WebSocket: " + t.getMessage());
        isConnected = false;
    }

    private void notifyConnectionEstablished() {
        Log.i("comprobar","Conexión establecida con éxito");
        isConnected = true;
    }
    public boolean isConnected() {
        return isConnected;
    }
    public void sendJsonMessage(String messageContent) {
        JSONObject objResponse = null;
        try {
            objResponse = new JSONObject("{}");
            objResponse.put("type", "Broadcast");
            objResponse.put("from", "clientId");
            objResponse.put("value", messageContent);
            String mensaje = objResponse.toString();
            webSocket.send(mensaje);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
