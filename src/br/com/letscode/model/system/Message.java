package br.com.letscode.model.system;

public class Message {
    private String text;
    private MessageType type;

    public Message() {
    }

    public Message(String text, MessageType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
