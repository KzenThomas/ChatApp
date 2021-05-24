package ChatApp.Entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

@Entity(name = "Conversations")
public class Conversations {

	@Id
	@GeneratedValue
	@Column(name = "conversationsid")
	private Integer conversationsid = 0;

	@Column(name = "name")
	private String name;

	public Integer getconversationsid() {
		return conversationsid;
	}

	public String getname() {
		return name;
	}

	public void setconversationsid(Integer conversationsid) {
		this.conversationsid = conversationsid;
	}

	public void setname(String name) {
		this.name = name;
	}

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "conversationstologin", joinColumns = { @JoinColumn(name = "conversationsid") }, inverseJoinColumns = {
			@JoinColumn(name = "loginid") })
	Set<Login> logins = new HashSet<>();

	public Conversations(String convonaam) {
		this.name = convonaam;
	}

	public Conversations() {

	}



	public Set<Login> selected() {
		return logins;
	}

	public void setLogins(Set<Login> logins) {
		this.logins = logins;
	}

	@Override
	public String toString() {
		return this.getname();
	}

	public Integer getConversationsid() {
		return conversationsid;
	}

	public void setConversationsid(Integer conversationsid) {
		this.conversationsid = conversationsid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addlogin(Login login) {
		this.logins.add(login);
	}
	
	
}