package Socket;

public class Message {
    private static int id = 0;
    private String message = null;
    private String sender;
    private String receiver;
    private String type = "connection";

    public Message(String message, String sender, String receiver, String type) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        id++;
    }

    @Override
    public String toString() {
        return id + " " + message + " " + sender + " " + receiver + " " + type;
    }

}
