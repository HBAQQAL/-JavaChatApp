package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import com.client.controllers.ChatController;
import com.google.gson.Gson;

public class Client extends Thread {
    private static Socket socket = null;
    private static DataInputStream input = null;
    private static DataOutputStream out = null;
    private static String port = null;
    private static String host = null;
    private static String identity = null;
    private ChatController controller;
    private static Gson gson = null;
    private static FileOutputStream fileOutputStream = null;
    DataInputStream serverInput = null;

    public Client(String host, String port, String identity, ChatController controller) {
        if (socket != null) {
            return;
        }
        this.host = host;
        this.port = port;
        this.identity = identity;
        this.controller = controller;
        System.out.println("controller " + controller);
        gson = new Gson();
        this.start();
    }

    public void run() {
        try {
            socket = new Socket(host, Integer.parseInt(port));
            input = new DataInputStream(System.in);
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Connected");
            serverInput = new DataInputStream(socket.getInputStream());
            while (true) {
                String response = serverInput.readUTF();
                handleResponse(response);
            }
        } catch (IOException e) {
            System.out.println("Error occurred in connection: " + e.getMessage());
        }
    }

    public static void sendMessage(Message message) {
        try {
            System.out.println("Sending message from client");
            message.setSender(identity);

            out.writeUTF(gson.toJson(message));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void handleResponse(String response) {

        // convert the response to an object of type message
        Message message = gson.fromJson(response, Message.class);

        // check if the sender is the server
        if (message.getSender().equals("server")) {
            // check if the message is a connection
            if (message.getType().equals("connection")) {
                // send a connection message
                Message m = new Message(identity, identity, "server", "connection");

                sendMessage(m);

            } else if (message.getType().equals("contacts")) {
                // get the contacts
                String[] contacts = message.getMessage().split(",");
                // convert to an arraylist of contacts and update the contacts
                ArrayList<Contact> contactList = new ArrayList<Contact>();
                for (String contact : contacts) {
                    contactList.add(new Contact(contact));
                    System.out.println("contact " + contact);
                }
                controller.UpdateContacts(contactList);

            }
        } else if (message.getType().equals("message")) {
            System.out.println("Message received");
            System.out.println("message " + message.getMessage());
            controller.showMessage(message.getMessage());
        } else if (message.getType().equals("file")) {

            System.out.println("file to receive : " + message.getMessage());
            System.out.println(receiveFile("test.pdf"));
            // receiveFile("test.pdf");
            // System.out.println("File received");
            // System.out.println("message " + message.getMessage());
        }
    }

    public static void sendFile(String path) {
        // send file to client here
        try {
            System.out.println("Sending file from client");
            int bytes = 0;
            // Open the File where he located in your pc
            File file = new File(path);
            FileInputStream fileInputStream = new FileInputStream(file);
            // Here we send the File to Server
            out.writeLong(file.length());
            // Here we break file into chunks
            byte[] buffer = new byte[4 * 1024];
            while ((bytes = fileInputStream.read(buffer)) != -1) {
                // Send the file to Server Socket
                out.write(buffer, 0, bytes);
                out.flush();
            }
            // close the file here
            fileInputStream.close();
            System.out.println("File is Sent");

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public boolean receiveFile(String fileName) {
        try {
            System.out.println("Receiving file from server");
            int bytes = 0;
            // Open the File where he located in your pc
            File file = new File(fileName);
            fileOutputStream = new FileOutputStream(file);
            // Here we receive the File from Server
            long size = serverInput.readLong();
            // Here we break file into chunks
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = serverInput.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                // Write the file
                fileOutputStream.write(buffer, 0, bytes);
                fileOutputStream.flush();
                size -= bytes;
            }
            // close the file here
            fileOutputStream.close();
            controller.showMessage("File received");
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    public static Socket getSocket() {
        return socket;
    }

}
