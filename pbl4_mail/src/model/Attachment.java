package model;

public class Attachment {

    private String name;
    private String type;
    private byte[] content;

    public Attachment() {
    }

    public Attachment(String name, String type, byte[] content) {
        this.name = name;
        this.type = type;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("Attachment(name=%s, type=%s, content=[%d bytes])", name, type, content.length);
    }
}

