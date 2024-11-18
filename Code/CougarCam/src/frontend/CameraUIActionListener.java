package frontend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import backend.TestDataAccess;

import javax.swing.JButton;
import cougarCam.DoorManager;
import utils.Resource;
public class CameraUIActionListener implements ActionListener {
	private JButton pressedButton;
	private JButton login;
	private JButton start;

	@Override
	public void actionPerformed(ActionEvent e) {
		pressedButton = (JButton) e.getSource();
		if(pressedButton == login) {
			//Will call AdminUI
			System.out.println("Login pressed");
		}
		else if(pressedButton == start) {
			//starts 
			System.out.println("Start pressed");
//			System.out.println("Starting Camera");
//			TestDataAccess dao = new TestDataAccess();
//			Resource user = new Resource();
//			dao.addUser(user);
//			DoorManager dmanager = new DoorManager(dao);
//			dmanager.startDoorManager();
		}
	}
}

