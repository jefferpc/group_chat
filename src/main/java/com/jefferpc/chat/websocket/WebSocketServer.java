/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jefferpc.chat.websocket;

import com.jefferpc.chat.model.User;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Ing. Jefferson Pérez Cervera
 */
@ServerEndpoint("/actions")
public class WebSocketServer {
    
    

    @Inject
    private SessionHandler sessionHandler;

    @OnOpen
    public void open(Session session) {
        sessionHandler.addSession(session);
    }

    @OnClose
    public void close(Session session) {
        sessionHandler.removeSession(session);
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(WebSocketServer.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();

            if ("addUser".equals(jsonMessage.getString("action"))) {
                User user = new User();
                user.setUserName(jsonMessage.getString("userName"));
                user.setStatus("connected");
                sessionHandler.addUser(user, session);
                return;
            }
            if ("addMessage".equals(jsonMessage.getString("action"))) {
                User user = new User();
                user.setUserName(jsonMessage.getString("userName"));
                user.setStatus("connected");
                sessionHandler.addMessage(jsonMessage.getString("message"), user);
                return;
            }

        }
    }
}
