package com.example.app;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.internal.ws.RealWebSocket;

public class WebSocketExample extends WebSocketListener  {

    private WebSocket webSocket;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean isConnected = false;
    private boolean isValid = true;


    public WebSocketExample(String ipAddress) {
        String WS_URL = "ws://" + ipAddress + ":8080";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        webSocket = client.newWebSocket(request, this);
    }
    public void disconnect() {
        if (webSocket != null) {
            webSocket.cancel();
        }
    }
    @Override
    public void onOpen(WebSocket webSocket, Response response) {

        Log.i("comprobar","Conexión abierta");
        incio();
        notifyConnectionEstablished();
        isConnected = true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            // Parsear el JSON recibido
            JSONObject jsonMessage = new JSONObject(text);
            Log.i("recivido",jsonMessage.toString());


            // Obtener el valor asociado con la clave "validacion"
            String validacion = jsonMessage.optString("type");


            if (jsonMessage.has("validacion") && jsonMessage.getString("validacion").equals("correcto")) {
                Log.i("WebSocket", "Mensaje de conexión recibido");
                isValid = true;

            } else   if (jsonMessage.has("validacion") && jsonMessage.getString("validacion").equals("incorrecto")) {
                // El valor es incorrecto, realiza las acciones correspondientes
                Log.i("comprobar", "Mensaje recibido: incorrecto");
                isValid = false;
            }

        } catch (JSONException e) {
            Log.e("comprobar", "Error al parsear el mensaje JSON: " + e.getMessage());

        }
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
    public boolean isValid() {
        return isValid;
    }
    public void incio() {
        JSONObject objResponse = null;
        try {
            objResponse = new JSONObject("{}");
            objResponse.put("platform", "android");
            String mensaje = objResponse.toString();
            webSocket.send(mensaje);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendJsonMessage(String messageContent) {
        JSONObject objResponse = null;
        try {
            objResponse = new JSONObject("{}");
            objResponse.put("msgPlatform", "android");
            objResponse.put("message", messageContent);
            String mensaje = objResponse.toString();
            webSocket.send(mensaje);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendJsonimg(String messageContent) {
        JSONObject objResponse = null;
        try {
            objResponse = new JSONObject("{}");
            objResponse.put("imgPlatform", "android");
            objResponse.put("imagen", messageContent);
            Log.i("",messageContent);
            String mensaje = objResponse.toString();
            webSocket.send(mensaje);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendJsonMessageusu(String usuario,String contra) {
        JSONObject objResponse = null;
        try {
            objResponse = new JSONObject("{}");
            objResponse.put("userPlatform", "android");
            objResponse.put("user", usuario);
            objResponse.put("password", contra);
            String mensaje = objResponse.toString();
            webSocket.send(mensaje);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
