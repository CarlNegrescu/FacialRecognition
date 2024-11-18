package frontend;

import java.awt.image.BufferedImage;
import java.awt.CardLayout;
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
import frontend.CameraUI.ButtonListener;
import utils.Resource;

public class AdminUI {
	final static String BUTTONPANEL = "Card with JButtons";
	final static String TEXTPANEL = "Card with JTextField";

	private JLabel Welcome;
	public JButton add;
	public JButton edit;
	public JButton delete;
	public JButton logout;
	private JFrame AdminFrame;


	public void startAdmin() {
		ButtonListener listener = new ButtonListener();
		JPanel tidy = new JPanel();
		FlowLayout layout = new FlowLayout();

		// Create a new JFrame
		AdminFrame = new JFrame("Cougar Camera: Admin");
		Welcome = new JLabel("Welcome to Cougar Camera Administration System");
		add = new JButton("Add");
		edit = new JButton("Edit");
		delete = new JButton("Delete");
		logout = new JButton("logout");
		
		add.addActionListener(listener);
		edit.addActionListener(listener);
		delete.addActionListener(listener);
		logout.addActionListener(listener);
		
		tidy.add(Welcome);
		tidy.add(add);
		tidy.add(edit);
		tidy.add(delete);
		tidy.add(logout);
		AdminFrame.add(tidy);
		
		// Set frame properties
		AdminFrame.setSize(300,
				200); // Set the size of the frame
		AdminFrame.pack();
		// Close operation
		AdminFrame.setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);

		// Make the frame visible
		AdminFrame.setVisible(true);
	}


	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton clickedButton = (JButton) e.getSource();
			if (clickedButton == add) {

			} else if (clickedButton == edit) {

			}else if (clickedButton == delete) {

			}else if (clickedButton == logout) {

			}
		}
	}}
