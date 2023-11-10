package model;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class message {

    private int id;
    private String sender;
    private String receiver;
    private String subject;
    private String body;
    private File attachments;
    private LocalDateTime sendDate;

    public message(String sender, String subject, LocalDateTime sendDate) {
		super();
		this.sender = sender;
		this.subject = subject;
		this.sendDate = sendDate;
	}

	public message(int id, String sender, String receiver, String subject, String body, File attachments, LocalDateTime sendDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
        this.sendDate = sendDate;

    }

    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public File getAttachments() {
        return attachments;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }
//    public void addAttachment(Attachment attachment) {
//        this.attachments.add(attachment);
//    }
//
//    public void removeAttachment(Attachment attachment) {
//        this.attachments.remove(attachment);
//    }

    @Override
    public String toString() {
        return String.format("message(id=%d, sender=%s, receiver=%s, subject=%s, body=%s, attachments=%s, sendDate=%s, isRead=%s, isDeleted=%s)",
                id, sender, receiver, subject, body, attachments, sendDate);
    }
}


