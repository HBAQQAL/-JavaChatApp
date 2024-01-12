package com.client.helpers;

import java.util.Map;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import com.client.model.Message;
import com.client.utils.ApiClient;
import com.client.utils.CreateMessages;

public class ChatManager {
    public static ChatManager instance = null;
    private ArrayList<Message> messages;

    private ChatManager() {
    }

    public static ChatManager getInstance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    public void sendMessage(Message message) {

        String bodyInjson = String.format("{\"content\":\"%s\",\"receiver\":\"%s\",\"dateandtime\":\"%s\"}",
                message.getMessage(), message.getReceiver(), new Date().toString());

        Map<String, Object> response = ApiClient.makeHttpRequest("http://localhost:7000/api/message", bodyInjson,
                "POST");
        response.forEach((key, value) -> {
            System.out.println(key + " " + value);
        });

    }

    public void getAllMessages() {

        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("receiver", ContactManager.getInstance().getSelectedContact().getUsername());

        Map<String, Object> response = ApiClient.makeHttpRequest("http://localhost:7000/api/messages", "GET",
                queryParams);
        response.get("messages");

        // CreateMessages createMessages = new CreateMessages();

        messages = new ArrayList<Message>();
        for (Object message : (ArrayList<Object>) response.get("messages")) {
            Map<String, Object> messageMap = (Map<String, Object>) message;
            Message newMessage = new Message(messageMap.get("content").toString(),
                    messageMap.get("sender").toString(), messageMap.get("receiver").toString(),
                    messageMap.get("date").toString());
            messages.add(newMessage);

        }
    }

    public ArrayList<Message> getMessages() {
        return this.messages;
    }

}
