package ChatApp.gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ChatApp.OneTimePad;
import ChatApp.Entities.Login;
import ChatApp.repositories.ConversationsRepo;
import ChatApp.repositories.LoginRepo;
import ChatApp.repositories.MessageRepo;

@Component
public class LoginGUI extends JFrame {
	@Autowired
	OneTimePad oneTimePad;
	@Autowired
	MessageRepo messagerepo;
	@Autowired
	ConversationsRepo conversationsRepo;
	@Autowired(required = true)
	LoginRepo loginRepo;
	@Autowired
	ClientGUI clientgui;

	public String username;
	public String wachtwoord;
	private Map<String, Integer> duplicates = new HashMap<>();
	Container container = getContentPane();
	JLabel userLabel = new JLabel("USERNAME");
	JLabel passwordLabel = new JLabel("PASSWORD");
	JTextField userTextField = new JTextField();
	JPasswordField passwordField = new JPasswordField();
	JButton loginButton = new JButton("LOGIN");
	JButton resetButton = new JButton("RESET");
	JCheckBox showPassword = new JCheckBox("Show Password");

	public void setLayoutManager() {
		container.setLayout(null);
	}

	public void setLocationAndSize() {
		userLabel.setBounds(50, 150, 100, 30);
		passwordLabel.setBounds(50, 220, 100, 30);
		userTextField.setBounds(150, 150, 150, 30);
		passwordField.setBounds(150, 220, 150, 30);
		showPassword.setBounds(150, 250, 150, 30);
		loginButton.setBounds(50, 300, 100, 30);
		resetButton.setBounds(200, 300, 100, 30);
	}

	public void addComponentsToContainer() {
		container.add(userLabel);
		container.add(passwordLabel);
		container.add(userTextField);
		container.add(passwordField);
		container.add(showPassword);
		container.add(loginButton);
		container.add(resetButton);
	}

	private Login findLoginInDB(String username) {
		Iterable<Login> allloginusersiterable = loginRepo.findAll();
		List<String> allloginusers = new ArrayList<>();
		allloginusersiterable.forEach(login -> allloginusers.add(login.getusername()));
		return null;
	}

	public void addActionEvent() {
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == loginButton) {
					username = userTextField.getText();
					wachtwoord = passwordField.getText();

					if (isUserDuplicate(username)) {
						JOptionPane.showMessageDialog(null, "user already exists!", "USERNAME ERROR",
								JOptionPane.ERROR_MESSAGE);
					}

					Login loginfromdb = findLoginInDB(username);
					Login login = null;
					if (loginfromdb == null) {
						login = new Login(username, wachtwoord);
						login = loginRepo.save(login);
					} else {
						if (login.getwachtwoord().equals(wachtwoord)) {
							login = loginfromdb;
						} else {
						}
					}
					saveLogintoDB(login);
					dispose();
					clientgui.setLogin(login);
					clientgui.display();
				}
			}

		});
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == resetButton) {
					userTextField.setText("");
					passwordField.setText("");
				}
			}
		});
		showPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == showPassword) {
					if (showPassword.isSelected()) {
						passwordField.setEchoChar((char) 0);
					} else {
						passwordField.setEchoChar('*');
					}
				}
			}
		});
	}

	private void saveLogintoDB(Login login) {
		loginRepo.save(login);
	}

	private void preloadUsersFromDB() {
		Iterable<Login> logins = loginRepo.findAll();
		List<String> user = new ArrayList<>();
		logins.forEach(e -> user.add(e.getusername()));
		for (String s : user) {
			duplicates.put(s, 1);
		}
	}

	private boolean isUserDuplicate(String user) {
		return (duplicates.containsKey(user)) ? true : false;
	}

	public void display() {
		this.setTitle("Login Form");
		this.setBounds(10, 10, 370, 600);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayoutManager();
		setLocationAndSize();
		addComponentsToContainer();
		preloadUsersFromDB();
		addActionEvent();
		this.setVisible(true);
	}
}