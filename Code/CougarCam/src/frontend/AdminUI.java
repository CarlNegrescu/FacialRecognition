
package frontend;

import java.awt.image.BufferedImage;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.EmptyBorder;

import backend.DataAccess;
import backend.IDataAccess;
import utils.Resource;
import frontend.MainUI;



public class AdminUI extends JFrame
{
	private static final Color BACKGROUND_COLOR = new Color(22, 22, 22);  // Rich Black
	private static final Color BUTTON_COLOR = new Color(188, 190, 212);     // University Blue
	private static final Color BUTTON_HOVER_COLOR = new Color(209, 211, 212);  // Cougar Blue
	private static final Color SECONDARY_COLOR = new Color(30, 144, 255);  // Light Blue
	private static final Color TEXT_COLOR = new Color(0, 46, 90); // Cougar Blue

	private JFrame addFrame;
	private JFrame editFrame;
	private JFrame deleteFrame;
	protected JButton fileButton;
	protected JTextField firstName;
	protected JTextField lastName;
	private Mat userImage;
	private Resource user;
	private IDataAccess _dao;
	private List<Resource> listUsers = new ArrayList<Resource>();
	private boolean fileAdded = false;

	public AdminUI(IDataAccess dao) {
		_dao = dao;
	}
	//Frames
	protected void addFrame() {

		//Initialize Frame, Panel, and listener
		addFrame = new JFrame("Add a user");
		addFrame.setLayout(new BorderLayout());
		addFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addFrame.setLocationRelativeTo(null);

		JPanel addPane1 = new JPanel();
		addPane1.setBackground(SECONDARY_COLOR);
		addPane1.setBorder(new EmptyBorder(10,10,10,10));
		JPanel addPane3 = new JPanel();
		addPane3.setBackground(BACKGROUND_COLOR);
		fileAdded = false;

		//Initialize Contents
		firstName = new JTextField(60);
		lastName = new JTextField(60);
		JButton fileButton = new JButton("Choose a file");
		JButton cancelAdd = new JButton("Cancel");
		JButton confirmAdd = new JButton("Confirm");
		JLabel firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
		JLabel lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));

		//style
		styleButton(fileButton);
		styleButton(cancelAdd);
		styleButton(confirmAdd);
		//add listeners
		fileButton.addActionListener(e -> fileFrame());
		cancelAdd.addActionListener(e->addFrame.dispose());
		confirmAdd.addActionListener(e->addUser());

		//add contents
		addPane1.add(firstNameLabel);
		addPane1.add(firstName);
		addPane1.add(lastNameLabel);
		addPane1.add(lastName);
		addPane1.add(fileButton);
		addPane3.add(cancelAdd);
		addPane3.add(confirmAdd);

		//Split pane
		//JSplitPane split = new JSplitPane(SwingConstants.VERTICAL, addPane2, addPane1);
		addFrame.add(addPane1, BorderLayout.CENTER);
		addFrame.add(addPane3, BorderLayout.SOUTH);
		addFrame.setSize(800,500);
		addFrame.setVisible(true);
	}
	public void editFrame() {
		//Initialize Frame, Panel, and listener
		editFrame = new JFrame("Edit a user");
		editFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		editFrame.setSize(300,250);
		editFrame.setLocationRelativeTo(null);
		editFrame.setVisible(true);
		JPanel editPane = new JPanel();
		String userNames[] = {};
		
		if(listUsers.get(0).validFace != null) {
			for(int i = 0; i < listUsers.size(); i++) {
				userNames[i] = listUsers.get(i).firstName;
			}
		}
		else {
			JOptionPane.showMessageDialog(new JFrame(), "No users to edit", "Error",JOptionPane.ERROR_MESSAGE);
			editFrame.dispose();
		}
		//Initialize Contents
		firstName = new JTextField(16);
		lastName = new JTextField(16);
		fileButton = new JButton("Choose a file");
		JButton cancelEdit = new JButton("Cancel");
		JButton confirmEdit = new JButton("Confirm");
		JLabel firstNameLabel = new JLabel("First Name:");
		JLabel lastNameLabel = new JLabel("Last Name:");
		JList displayedUsers = new JList(userNames);

		ListSelectionModel listSelectionModel = displayedUsers.getSelectionModel();

		//add listeners
		fileButton.addActionListener(e -> fileFrame());
		cancelEdit.addActionListener(e->editFrame.dispose());
		confirmEdit.addActionListener(e->editUser());

		//add contents
		editPane.add(firstNameLabel);
		editPane.add(firstName);
		editPane.add(lastNameLabel);
		editPane.add(lastName);
		editPane.add(fileButton);
		editPane.add(displayedUsers);
		editPane.add(cancelEdit);
		editPane.add(confirmEdit);


		editFrame.getContentPane().add(editPane);


	}
	public void deleteFrame() {
		deleteFrame = new JFrame("Delete a user");
		String userNames[] = {};
		int userId[]= {};

		deleteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		deleteFrame.setSize(300,250);
		deleteFrame.setLocationRelativeTo(null);
		deleteFrame.setVisible(true);

		listUsers = _dao.getUsers();

		if(listUsers.get(0).validFace != false) {
			for(int i = 0; i < listUsers.size(); i++) {
				userNames[i] = listUsers.get(i).firstName;
			}
		}
		else {
			JOptionPane.showMessageDialog(new JFrame(), "No users to delete", "Error",JOptionPane.ERROR_MESSAGE);
			deleteFrame.dispose();
		}
		JList displayedUsers = new JList(userNames);
		displayedUsers.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!e.getValueIsAdjusting()){
					user.firstName = (String)displayedUsers.getSelectedValue();
					confirmDeleteFrame();
				};
			}
		});
		deleteFrame.add(displayedUsers);

	}
	private void fileFrame() {
		//initialize filechooser
		JFileChooser fileChooser = new JFileChooser();


		//logic
		int selection = fileChooser.showSaveDialog(null);
		fileChooser.addChoosableFileFilter(new ImageFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);

		if(selection== JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file == null) {
				System.out.println("No file selected!");
			}
			else {
				saveFile(file);
				fileAdded = true;
			}

		}
		else {
			fileAdded = false;
		}
	}
	private void confirmDeleteFrame() {
		int option = JOptionPane.showConfirmDialog(addFrame, "Are you sure you want to delete this User?",
				"Delete Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if(option == 0)
			deleteUser();
	}

	//Logic
	private void addUser() {
		if(fileAdded == true) {
			Resource newUser = new Resource();
			newUser.firstName = firstName.getText();
			newUser.lastName = lastName.getText();
			newUser.userEncode = userImage;
			_dao.addUser(newUser);
			addFrame.dispose();
			JOptionPane.showMessageDialog(null, "Added User Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(new JFrame(), "Enter an Image", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	private void editUser() {
		user.firstName = firstName.getText();
		user.lastName = lastName.getText();
		user.userEncode = userImage;
		_dao.updateUser(user, user.firstName);

	}
	private void deleteUser() {
		_dao.deleteUser(user.firstName);
	}
	
	private void saveFile(File file) {
		CascadeClassifier _cascade = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_default.xml");
		MatOfRect faceDetections = new MatOfRect();
		String image = file.getAbsolutePath();
		Mat userFace = new Mat();
		Mat cropFace = null;
		userFace= Imgcodecs.imread(image, Imgcodecs.IMREAD_COLOR);
		_cascade.detectMultiScale(userFace, faceDetections);

		for (Rect rect : faceDetections.toArray())
		{
			Imgproc.rectangle(userFace, new Point(rect.x, rect.y), 
					new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 2);
			cropFace = new Mat(userFace, rect);

		}
		userImage = cropFace;
	}

	//Method to style buttons consistently
	public void styleButton(JButton button) {
		button.setFont(new Font("Arial", Font.BOLD, 16));
		button.setForeground(TEXT_COLOR);
		button.setBackground(BUTTON_COLOR);
		button.setFocusPainted(false);
		button.setPreferredSize(new Dimension(250, 40));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center the button

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(BUTTON_HOVER_COLOR);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(BUTTON_COLOR);
			}
		});
	}
	//Found at https://www.tutorialspoint.com/swingexamples/show_file_chooser_images_only.htm
	class ImageFilter extends FileFilter {
		public final static String JPEG = "jpeg";
		public final static String JPG = "jpg";
		public final static String GIF = "gif";
		public final static String TIFF = "tiff";
		public final static String TIF = "tif";
		public final static String PNG = "png";

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
				return true;
			}

			String extension = getExtension(f);
			if (extension != null) {
				if (extension.equals(TIFF) ||
						extension.equals(TIF) ||
						extension.equals(GIF) ||
						extension.equals(JPEG) ||
						extension.equals(JPG) ||
						extension.equals(PNG)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return "Image Only";
		}

		String getExtension(File f) {
			String ext = null;
			String s = f.getName();
			int i = s.lastIndexOf('.');

			if (i > 0 &&  i < s.length() - 1) {
				ext = s.substring(i+1).toLowerCase();
			}
			return ext;
		} 
	}
}
