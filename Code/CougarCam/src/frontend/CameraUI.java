package frontend;

import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import frontend.CameraUIActionListener;
import javax.swing.JTextField;
import backend.TestDataAccess;
import cougarCam.DoorManager;
import utils.Resource;

public class CameraUI{

	private JLabel Welcome;
	public JButton login;
	public JButton start;
	private JFrame StartFrame;
	private JPanel Layout;

	public void startApp() {
		ButtonListener listener = new ButtonListener();
		
		// Create a new JFrame
		StartFrame = new JFrame("Cougar Camera");
		Layout = new JPanel(new BorderLayout());

		login = new JButton("Log In");
		start = new JButton("Start Camera");
		login.addActionListener(listener);
		start.addActionListener(listener);

		Welcome = new JLabel("Welcome to Cougar Camera");

		Layout.add(start, BorderLayout.EAST);
		Layout.add(login, BorderLayout.WEST);
		Layout.add(Welcome, BorderLayout.CENTER);

		// Set frame properties
		StartFrame.setSize(300,
				200); // Set the size of the frame

		// Close operation
		StartFrame.setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);
		StartFrame.add(Layout);

		// Make the frame visible
		StartFrame.setVisible(true);

		login.addActionListener(null);
	}
	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton clickedButton = (JButton) e.getSource();
			if (clickedButton == login) {
				System.out.println("Login pressed");
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
