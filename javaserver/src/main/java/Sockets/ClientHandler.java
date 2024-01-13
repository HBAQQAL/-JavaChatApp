package Sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

public class ClientHandler extends Thread {
    Socket serverSocket;
    private DataInputStream in = null;
    public String id;
    private DataOutputStream out = null;
    private Gson gson = new Gson();

    public ClientHandler(Socket serverSocket) {
        try {
            System.out.println("ClientHandler created");
            this.serverSocket = serverSocket;
            in = new DataInputStream(serverSocket.getInputStream());
            out = new DataOutputStream(serverSocket.getOutputStream());
            this.start();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void run() {
        System.out.println("new ClientHandler started for client ");
        getUserId();
        String line = "";
        while (!line.equals("Over")) {
            try {
                line = in.readUTF();
                handleMessage(line);
            } catch (IOException i) {
                System.out.println(i);
                break;
            }
        }
        System.out.println("Closing connection");
        CloseConnection();
    }

    public void getUserId() {
        try {
            Message m = new Message("pleasesetid", "server", "client", "connection");
            sendMessage(m);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * @param message
     */
    public void handleMessage(String message) {
        try {
            Message m = gson.fromJson(message, Message.class);
            System.out.println("message to : " + m.getReceiver());
            System.out.println("message from : " + m.getSender());
            System.out.println("message type : " + m.getType());
            System.out.println("message content : " + m.getMessage());
            if (m.getReceiver().equals("server")) {
                System.out.println("server message");
                if (m.getType().equals("connection")) {
                    setId(m.getSender());
                    Server.SendListOfClients();
                }
            } else if (m.getReceiver().equals("all")) {
                System.out.println("broadcast message");
                for (ClientHandler receiver : Server.clients) {
                    receiver.sendMessage(m);
                }
            } else {
                System.out.println("private message");
                if (m.getType().equals("file")) {
                    System.out.println("file message");
                    String receivedFile = receiveFile(m.getMessage());
                    System.out.println("know sending file  to " + m.getReceiver());
                    Message fileMessage = new Message(receivedFile, m.getSender(),
                            m.getReceiver(), "file");
                    System.out.println("sending file message to " + fileMessage.toString());
                    ClientHandler receiver = Server.getClient(m.getReceiver());
                    receiver.sendMessage(fileMessage);
                    receiver.sendFile(receivedFile);
                } else {
                    ClientHandler receiver = Server.getClient(m.getReceiver());
                    receiver.sendMessage(m);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void sendMessage(Message message) {
        try {
            String msg = gson.toJson(message);
            System.out.println("sending message from " + this);
            out.writeUTF(msg);
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public void CloseConnection() {
        try {
            // remove client from list
            Server.clients.remove(this);
            // send updated list to all clients
            Server.SendListOfClients();
            serverSocket.close();
            in.close();
            out.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public String receiveFile(String fileName) {
        try {
            int bytes = 0;
            File file = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            long size = in.readLong(); // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0
                    && (bytes = in.read(
                            buffer, 0,
                            (int) Math.min(buffer.length, size))) != -1) {
                // Here we write the file using write method
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
            }
            // Here we received file
            System.out.println("File is Received");
            fileOutputStream.close();
            System.out.println("file full path is : " + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendFile(String filePath) {
        try {
            // Here we read the file and send using FileInputStream
            FileInputStream fileInputStream = new FileInputStream(filePath);
            long size = fileInputStream.available();
            out.writeLong(size); // send file size
            int bytes = 0;
            byte[] buffer = new byte[4 * 1024];
            while ((bytes = fileInputStream.read(buffer)) != -1) {
                // Here we send the file
                out.write(buffer, 0, bytes);
                out.flush();
            }
            // Here we completed sending
            System.out.println("File is Sent");
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setId(String id) {
        this.id = id;
        System.out.println("ClientHandler id set to " + id);
    }

}
