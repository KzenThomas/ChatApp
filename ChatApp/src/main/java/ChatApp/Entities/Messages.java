package ChatApp.Entities;

import java.awt.Menu;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "Messages")
public class Messages {

	@Id
	@GeneratedValue
	@Column(name = "messageid")
	private Integer messageid = 0;

	@ManyToOne
	@JoinColumn(name = "loginid")
	private Login login;
	
	@ManyToOne
	@JoinColumn(name = "conversationsid")
	private Conversations conversations;

	@Column(name = "messagetext")
	private String messagetext;

	@Column(name = "offset")
	private Integer offset = 0;

	@Column(name = "messagedate")
	private LocalDateTime messagedate;

	public String getmessagetext() {
		return messagetext;
	}

	public void setmessageid(Integer messageid) {
		this.messageid = messageid;
	}

	public Integer getoffset() {
		return offset;
	}

	public void setoffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getMessageid() {
		return messageid;
	}

	public void setMessageid(Integer messageid) {
		this.messageid = messageid;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
	

	public String getMessagetext() {
		return messagetext;
	}

	public void setMessagetext(String messagetext) {
		this.messagetext = messagetext;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public void setmessagetext(String messagetext) {
		this.messagetext = messagetext;
	}

	public LocalDateTime getMessagedate() {
		return messagedate;
	}

	public void setMessagedate(LocalDateTime messagedate) {
		this.messagedate = messagedate;
	}

	public Messages() {

	}

	public Messages(Login login,Conversations conversations, String text, Integer positie, LocalDateTime formattedDate) {
		this.login = login;
		this.conversations = conversations;
		this.messagetext = text;
		this.offset = positie;
		this.messagedate = formattedDate;
	}
}