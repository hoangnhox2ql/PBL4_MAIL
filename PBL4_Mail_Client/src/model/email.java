package model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;

public class email implements Serializable {
    private static final long serialVersionUID = 7488896991625280335L;
    
	private String sender;
    private String receiver;
    private String subject;
    private String body;
    private File attachments;
    private LocalDateTime sendDate;

    // Constructors
    public email(String sender, String subject, LocalDateTime sendDate) {
        this.sender = sender;
        this.subject = subject;
        this.sendDate = sendDate;
    }

    public email(String sender, String receiver, String subject, String body, File attachments, LocalDateTime sendDate) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
        this.attachments = attachments;
        this.sendDate = sendDate;
    }

    // Getters and Setters
    // (Omitted for brevity)
    public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public File getAttachments() {
		return attachments;
	}

	public void setAttachments(File attachments) {
		this.attachments = attachments;
	}

	public LocalDateTime getSendDate() {
		return sendDate;
	}

	public void setSendDate(LocalDateTime sendDate) {
		this.sendDate = sendDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    // Other methods, if needed
    // ...

    @Override
    public String toString() {
        return String.format("Email(sender=%s, receiver=%s, subject=%s, body=%s, attachments=%s, sendDate=%s)",
                sender, receiver, subject, body, attachments, sendDate);
    }

	
}
