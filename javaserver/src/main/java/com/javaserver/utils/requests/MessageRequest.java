package com.javaserver.utils.requests;

public class MessageRequest {
    private String content;
    private String receiver;
    private String dateandtime;

    public MessageRequest(String content, String receiver, String dateandtime) {
        this.content = content;
        this.receiver = receiver;
        this.dateandtime = dateandtime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }

    @Override
    public String toString() {
        return "MessageRequest [content=" + content + ", receiver=" + receiver + ", dateandtime=" + dateandtime + "]";
    }

}
