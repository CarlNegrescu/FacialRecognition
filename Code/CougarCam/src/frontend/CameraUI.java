package frontend;

import java.awt.image.BufferedImage;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import backend.TestDataAccess;
import cougarCam.DoorManager;
import frontend.AdminUI;
import utils.Resource;

public class CameraUI{

	private JLabel Welcome;
	public JButton login;
	public JButton start;
	private JFrame StartFrame;
	
	
	public void startApp() {
		ButtonListener listener = new ButtonListener();
		JPanel tidy = new JPanel();
		FlowLayout layout = new FlowLayout();
		
		tidy.setLayout(layout);
		
		// Create a new JFrame
		StartFrame = new JFrame("Cougar Camera");

		login = new JButton("Log In");
		start = new JButton("Start Camera");
		login.addActionListener(listener);
		start.addActionListener(listener);

		Welcome = new JLabel("Welcome to Cougar Camera");
		
		tidy.add(Welcome);
		tidy.add(login);
		tidy.add(start);
		StartFrame.add(tidy);
		
		
		// Set frame properties
		StartFrame.setSize(800,
				400); // Set the size of the frame
		// Close operation
		StartFrame.setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);

		// Make the frame visible
		StartFrame.setVisible(true);
	}
	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton clickedButton = (JButton) e.getSource();
			if (clickedButton == login) {
				System.out.println("Login pressed");
				StartFrame.dispose();
				AdminUI Coug = new AdminUI();
				Coug.startAdmin();
				
			} else if (clickedButton == start) {
				//starts system
				System.out.println("Start pressed");
				System.out.println("Starting Camera");
				TestDataAccess dao = new TestDataAccess();
				Resource user = new Resource();
				dao.addUser(user);
				DoorManager dmanager = new DoorManager(dao);
				dmanager.startDoorManager();
			}
		}

	}
}
