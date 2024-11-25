package frontend;

import java.awt.image.BufferedImage;

import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import backend.TestDataAccess;
import backend.DataAccess;
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
	public JButton confirm;
	public JButton openFile;
	private JFrame AdminFrame;
	public ButtonListener listener = new ButtonListener();

	public void startAdmin() {
		
		JPanel tidy = new JPanel();

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
		AdminFrame.setSize(800,
				400); // Set the size of the frame
		// Close operation
		AdminFrame.setDefaultCloseOperation(
				JFrame.EXIT_ON_CLOSE);

		// Make the frame visible
		AdminFrame.setVisible(true);
	}
	
	public void addFrame() {
		ButtonListener aFListener = new ButtonListener();
		JFrame AddFrame = new JFrame("Add a user");
		JPanel AdminFields = new JPanel();
		JTextField firstName = new JTextField("First Name");
		JTextField lastName = new JTextField("Last Name");
		
		openFile = new JButton("Choose a File");
		confirm = new JButton("Confirm");
		openFile.addActionListener(aFListener);
		confirm.addActionListener(aFListener);
		
		AdminFields.add(firstName);
		AdminFields.add(lastName);
		AdminFields.add(openFile);
		AdminFields.add(confirm);
		AddFrame.add(AdminFields);
		
		AddFrame.setSize(800,400);
		AddFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AddFrame.setVisible(true);
	}
	
	public void editFrame() {
		
	}
	
	public void deleteFrame() {
		
	}
	public void openFile() {
		JFrame addFile = new JFrame("Add a file");
		
		JFileChooser UserPic = new JFileChooser("Choose a file");
		int returnValue = UserPic.showOpenDialog(null);
		
		addFile.add(UserPic);
		
		addFile.setSize(300,300);
		addFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addFile.setVisible(true);
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = UserPic.getSelectedFile();
			System.out.println(selectedFile.getAbsolutePath());
		}
		else if(returnValue ==JFileChooser.CANCEL_OPTION) {
			addFile.dispose();
		}
		
	}
	
	public void createUser() {
		
	}

	class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			// TODO Auto-generated method stub
			JButton clickedButton = (JButton) e.getSource();

			if (clickedButton == add) {
				System.out.println("Pressed add");
				addFrame();


			} else if (clickedButton == edit) {
				System.out.println("Pressed edit");
			}else if (clickedButton == delete) {
				System.out.println("Pressed delete");
			}else if (clickedButton == logout) {
				System.out.println("Pressed logout");
			}
			else if (clickedButton == openFile) {
				System.out.println("Pressed addFile");

				openFile();
			}
			else if(clickedButton == confirm) {
				
		}
	}
}}
