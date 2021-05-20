package ChatApp;

import java.awt.EventQueue;

import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.UIManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import ChatApp.gui.ClientGUI;
import ChatApp.gui.LoginGUI;

/**
 * @author Thomas
 */

@SpringBootApplication
@ComponentScan("ChatApp")
public class Springbootapp {

	/**
	 * Deze main method zorgt ervoor dat je de chatapp applicatie kan opstarten.
	 */

	public static void main(String[] args) {
		try {
			ConfigurableApplicationContext ctx = new SpringApplicationBuilder(Springbootapp.class).headless(false)
					.run(args);
			EventQueue.invokeLater(() -> {

				LoginGUI ex = ctx.getBean(LoginGUI.class);
				ex.display();
			});
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
}
