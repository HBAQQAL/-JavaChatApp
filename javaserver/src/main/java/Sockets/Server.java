package Sockets;

import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private int port;
    static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public Server(int port) {
        this.port = port;
        this.start();
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while (true) {
                System.out.println("Waiting for a client ...");
                clients.add(new ClientHandler(serverSocket.accept()));
                System.out.println("Client accepted");
                // show list of clients
                System.out.println("showing list of clients");
                for (ClientHandler clientHandler : clients) {
                    System.out.println(clientHandler.id);

                }
            }
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public static ClientHandler getClient(String idd) {
        for (ClientHandler c : clients) {
            if (c.id.equals(idd)) {
                return c;
            }
        }
        return null;
    }

    public static void SendListOfClients() {
        String list = "";
        for (ClientHandler c : clients) {
            list += c.id + ",";
        }
        for (ClientHandler c : clients) {
            System.out.println("sending list to " + c);
            Message m = new Message(list, "server", c.id, "contacts");

            c.sendMessage(m);
        }
    }
}
