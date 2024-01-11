package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import com.client.controllers.ChatController;

public class Client extends Thread {
    private static Socket socket = null;
    private static DataInputStream input = null;
    private static DataOutputStream out = null;
    private static String port = null;
    private static String host = null;
    private static String identity = null;
    private ChatController controller;

    public Client(String host, String port, String identity, ChatController controller) {
        if (socket != null) {
            return;
        }
        this.host = host;
        this.port = port;
        this.identity = identity;
        this.controller = controller;
        System.out.println("controller " + controller);
        this.start();
    }

    public void run() {
        try {
            socket = new Socket(host, Integer.parseInt(port));
            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
            DataInputStream serverInput = new DataInputStream(socket.getInputStream());
            while (true) {
                String response = serverInput.readUTF();
                handleResponse(response);
            }
        } catch (IOException e) {
            System.out.println("Error occurred in connection: " + e.getMessage());
        }
    }

    public static void sendMessage(String message) {
        try {
            System.out.println("Sending message from client");
            out.writeUTF(message);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleResponse(String response) {
        System.out.println(response);
        // split the response into an array
        String[] responseArray = response.split(" ");
        // get the type of message
        String type = responseArray[4];
        // get the sender
        String sender = responseArray[2];
        // get the message
        String messageText = responseArray[1];

        // check if the sender is the server
        if (sender.equals("server")) {
            // check if the message is a connection
            if (type.equals("connection")) {
                // send a connection message
                sendMessage(new Message(identity, identity, "server", "connection").toString());
            } else if (type.equals("contacts")) {
                // get the contacts
                String[] contacts = responseArray[1].split(",");
                // convert to an arraylist of contacts and update the contacts
                ArrayList<Contact> contactList = new ArrayList<Contact>();
                for (String contact : contacts) {
                    contactList.add(new Contact(contact));
                    System.out.println("contact " + contact);
                }
                controller.UpdateContacts(contactList);

            }
        } else if (type.equals("message")) {
            System.out.println("Message received");
            controller.showMessage(messageText);
        }
    }

    public static Socket getSocket() {
        return socket;
    }

}
