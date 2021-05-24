package ChatApp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ChatApp.OneTimePad;
import ChatApp.Entities.Conversations;
import ChatApp.Entities.Login;
import ChatApp.Entities.Messages;
import ChatApp.repositories.ConversationsRepo;
import ChatApp.repositories.LoginRepo;
import ChatApp.repositories.MessageRepo;

@Component
public class ClientGUI extends JFrame {
	@Autowired
	OneTimePad oneTimePad;
	@Autowired
	MessageRepo messagerepo;
	@Autowired
	ConversationsRepo conversationsRepo;
	@Autowired
	LoginRepo loginrepo;
	@Autowired
	LoginGUI logingui;

	JFrame newFrame = new JFrame("ChatApp");
	JButton sendMessage;
	JButton NewConversation;
	JTextField messageBox;
	JTextArea chatBox;
	JTextArea NewChatBox;
	JTextField usernameChooser;
	LocalDateTime myDateObj = LocalDateTime.now();
	static JLabel l;
	File encryptfile;
	ListSelectionModel listSelectionModel;
	String convonaam;
	DefaultListModel<Conversations> conversationslistmodel = new DefaultListModel<>();
	JList<Conversations> conversationslist = new JList<>(conversationslistmodel);
	Login login;
	Conversations conversations;
	JButton Filebutton = new JButton("select a file to encrypt");

	public void setLogin(Login login) {
		this.login = login;
	}

	public void display() {
		Filebutton.setEnabled(false);
		Filebutton.addActionListener((event) -> {
			String com = event.getActionCommand();
			JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			int r = j.showOpenDialog(null);
			if (r == JFileChooser.APPROVE_OPTION) {
				l.setText(j.getSelectedFile().getAbsolutePath());
				encryptfile = j.getSelectedFile();
			} else {
				l.setText("the user cancelled the operation");
			}
		});

		JPanel southPanel = new JPanel();
		southPanel.add(Filebutton);
		l = new JLabel("no file selected");
		l.setSize(100, 30);
		southPanel.add(l);

		this.add(BorderLayout.SOUTH, southPanel);
		this.setBounds(10, 10, 400, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		southPanel.setBackground(Color.GRAY);
		southPanel.setLayout(new GridBagLayout());

		NewConversation = new JButton("new conversation");
		NewConversation.setSize(50, 20);
		JPanel panell = new JPanel();
		panell.add(NewConversation);
		this.getContentPane().add(panell, BorderLayout.NORTH);
		NewConversation.addActionListener(new StartNewConversation());

		messageBox = new JTextField(32);
		sendMessage = new JButton("Send Message");
		sendMessage.setSize(100, 30);
		chatBox = new JTextArea();
		chatBox.setEditable(false);
		this.add(new JScrollPane(chatBox), BorderLayout.CENTER);

		chatBox.setLineWrap(true);
		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.WEST;
		GridBagConstraints right = new GridBagConstraints();
		right.anchor = GridBagConstraints.EAST;
		right.weightx = 2.0;

		southPanel.add(messageBox, left);
		southPanel.add(sendMessage, right);
		messageBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (messageBox.getText().length() < 1) {
					} else if (messageBox.getText().equals(".clear")) {
						chatBox.setText("Cleared all messages\n");
						messageBox.setText("");
					} else {
						String encrypt = oneTimePad.encrypt(encryptfile, messageBox.getText());
						Messages message = new Messages(login, conversations, encrypt,
								oneTimePad.position - encrypt.length(), myDateObj);
						messagerepo.save(message);
						System.out.println("Decryptfile:" + encryptfile);
						messageBox.setText("");
					}
				}
			}
		});

		chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
		sendMessage.addActionListener(new sendMessageButtonListener());
		this.setSize(600, 400);
		showdecryptedtext();
		showConversations();
		this.setVisible(true);
	}

	private void refreshConversationsListmodel() {
		Iterable<Conversations> conversations = conversationsRepo.findAll();
		conversationslistmodel.clear();

		for (Conversations l : conversations) {
			conversationslistmodel.addElement(l);
		}
	}

	public void SelectEncryptFile() {
		JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int r = j.showOpenDialog(null);

		if (r == JFileChooser.APPROVE_OPTION) {
			l.setText(j.getSelectedFile().getAbsolutePath());
			encryptfile = j.getSelectedFile();
		}
		else {
			l.setText("the user cancelled the operation");
		}
	}

	public void showConversations() {
		refreshConversationsListmodel();
		conversationslist = new JList<>(conversationslistmodel);
		conversationslist.setSize(300, this.getHeight() - 50);
		JPanel listpanel = new JPanel();
		listpanel.add(conversationslist);
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				boolean adjust = listSelectionEvent.getValueIsAdjusting();
				Integer tempMessageId = -1;
				Conversations selected = conversationslist.getSelectedValue();
				if (!adjust && selected != null) {
					chatBox.setText("");
					Filebutton.setEnabled(true);
					conversations = selected;
					List<Conversations> selectedconversations = conversationslist.getSelectedValuesList();
					/*
					 * conversationsRepo.OnlyPassCertainUsersInConversations(conversations.
					 * getconversationsid());
					 */
					if (encryptfile == null) {
						SelectEncryptFile();
					}
					refreshConversationsListmodel();
					conversationslist.repaint();
					initializeOneTimePadToCorrectPosition(selected);

				}
			}
		};

		conversationslist.addListSelectionListener(listSelectionListener);
		this.getContentPane().add(listpanel, BorderLayout.WEST);
	}

	private void initializeOneTimePadToCorrectPosition(Conversations selected) {
		List<Messages> messages = messagerepo.getAllMessagesFromconversations(conversations.getconversationsid());
		int maxoffsetinconvo = 0;
		Messages langste = null;
		for (Messages m : messages) {
			if (m.getoffset() > maxoffsetinconvo) {
				maxoffsetinconvo = m.getoffset();
				langste = m;
			}
		}
		if (langste != null) {
			oneTimePad.position = langste.getoffset() + langste.getmessagetext().length();
		}
	}

	public void showdecryptedtext() {
		int delay = 1000;
		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (encryptfile == null) {
					return;
				} else {
					List<Messages> messagess = messagerepo
							.getAllMessagesFromconversations(conversations.getconversationsid());
					chatBox.setText("");
					for (Messages m : messagess) {
						String getmessagetext = m.getmessagetext();
						String decrypt = oneTimePad.decrypt(encryptfile, m.getoffset(), getmessagetext);
						chatBox.append(m.getLogin().getusername() + ": " + decrypt + "\n");
						messagess.spliterator();
					}
				}
			}

		};
		new Timer(delay, taskPerformer).start();
	}

	/**
	 * Deze button zorgt ervoor dat je een bericht kan versturen in de gui en dat de
	 * data daarvan wordt opgeslagen in de database.
	 */

	class sendMessageButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (messageBox.getText().length() < 1) {
			} else if (messageBox.getText().equals(".clear")) {
				chatBox.setText("Cleared all messages\n");
				messageBox.setText("");
			} else {
				String encrypt = oneTimePad.encrypt(encryptfile, messageBox.getText());
				Messages message = new Messages(login, conversations, encrypt, oneTimePad.position - encrypt.length(),
						myDateObj);
				messagerepo.save(message);
				System.out.println("Decryptfile:" + encryptfile);
				messageBox.setText("");
			}
		}
	}

	class StartNewConversation implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFrame newconvoframe = new JFrame();
			newconvoframe.setSize(400, 200);
			JLabel vraag = new JLabel("Name for new conversation?");
			JTextField input = new JTextField();

			Iterable<Login> allloginusersiterable = loginrepo.findAll();
			List<String> allloginusers = new ArrayList<>();
			allloginusersiterable.forEach(login -> allloginusers.add(login.getusername()));

			DefaultListModel<String> users = new DefaultListModel<String>();
			users.addAll(allloginusers);
			JList<String> displayList = new JList<>(users);
			displayList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			JScrollPane scrollPane = new JScrollPane(displayList);

			vraag.setForeground(Color.GRAY);
			input.setPreferredSize(new Dimension(75, 20));
			JPanel panel = new JPanel();
			panel.add(vraag);
			panel.add(input);
			panel.add(scrollPane);
			newconvoframe.add(panel);
			input.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						convonaam = input.getText();
						System.out.println(convonaam);
						Conversations conversations = new Conversations(convonaam);
						Conversations savedconversation = conversationsRepo.save(conversations);
						List<String> SelectedUsers = displayList.getSelectedValuesList();
						Iterable<Login> logins = loginrepo.findAll();
						for (Login l : logins) {
							if (SelectedUsers.contains(l.getusername())) {
								System.out.println("adding login " + l.getusername() + " id: " + l.getloginid()
										+ " to convo: " + savedconversation.getconversationsid());
								savedconversation.addlogin(l);
							}
						}
						conversationsRepo.save(savedconversation);
						setSavedConversation(savedconversation);
						newconvoframe.dispose();
						refreshConversationsListmodel();
						conversationslist.repaint();
					}
				}
			});
			newconvoframe.setVisible(true);
		}
	}

	private Conversations savedConversation;

	private void setSavedConversation(Conversations savedconversation) {
		this.savedConversation = savedconversation;
	}

	public Conversations getSavedConversation() {
		return this.savedConversation;
	}
}