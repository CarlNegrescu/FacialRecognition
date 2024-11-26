
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
import java.util.Iterator;
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

	public AdminUI(IDataAccess dao) 
	{
		_dao = dao;
	}
	//Frames
	protected void addFrame() 
	{
		//Initialize Frame, Panel, and listener
		addFrame = new JFrame("Add a user");
		addFrame.setLayout(new BorderLayout());
		addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
	
	
	public void deleteFrame() {
    // Initialize Frame
    deleteFrame = new JFrame("Delete a User");
    deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    deleteFrame.setSize(500, 450);
    deleteFrame.setLocationRelativeTo(null);
    deleteFrame.setVisible(true);

    // Retrieve the user list
    listUsers = _dao.getUsers();
    
    if (listUsers.isEmpty()) {
        JOptionPane.showMessageDialog(deleteFrame, "No users to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        deleteFrame.dispose();
        return;
    }

    // Create panel
    JPanel deletePane = new JPanel(new BorderLayout());
    deletePane.setBackground(BACKGROUND_COLOR);

    // List of users to display
    DefaultListModel<String> userListModel = new DefaultListModel<>();
    for (Resource user : listUsers) {
        userListModel.addElement(user.firstName + " " + user.lastName);
    }

    JList<String> displayedUsers = new JList<>(userListModel);
    displayedUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    displayedUsers.setBackground(SECONDARY_COLOR);
    displayedUsers.setForeground(TEXT_COLOR);
    displayedUsers.setFont(new Font("Arial", Font.PLAIN, 16));

    JScrollPane scrollPane = new JScrollPane(displayedUsers);
    scrollPane.setPreferredSize(new Dimension(200, 150));

    // Buttons
    JButton confirmDelete = new JButton("Delete");
    styleButton(confirmDelete);
    confirmDelete.setEnabled(false); // Initially disabled

    JButton cancelDelete = new JButton("Cancel");
    styleButton(cancelDelete);
    cancelDelete.addActionListener(e -> deleteFrame.dispose());

    // Enable delete button only when a user is selected
    displayedUsers.addListSelectionListener(e -> confirmDelete.setEnabled(!e.getValueIsAdjusting()));

    // Confirm deletion and update database
    confirmDelete.addActionListener(e -> {
        int selectedIndex = displayedUsers.getSelectedIndex();
        if (selectedIndex != -1) {
            // Confirm before deletion
            int option = JOptionPane.showConfirmDialog(deleteFrame, "Are you sure you want to delete this user?",
                    "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                // Delete user from database
                Resource selectedUser = listUsers.get(selectedIndex);
                _dao.deleteUser(selectedUser.firstName);

                // Update UI list
                userListModel.remove(selectedIndex);

                // Confirm success
                JOptionPane.showMessageDialog(deleteFrame, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close frame if no users remain
                if (userListModel.isEmpty()) {
                    deleteFrame.dispose();
                }
            }
        }
    });

    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBackground(BACKGROUND_COLOR);
    buttonPanel.add(confirmDelete);
    buttonPanel.add(cancelDelete);

    // Add components to deletePane
    deletePane.add(scrollPane, BorderLayout.CENTER);
    deletePane.add(buttonPanel, BorderLayout.SOUTH);

    deleteFrame.add(deletePane);
}

	
	public void editFrame() {
    // Initialize Frame
    editFrame = new JFrame("Edit a User");
    editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    editFrame.setSize(500, 450);
    editFrame.setLocationRelativeTo(null);
    listUsers = _dao.getUsers();
    JPanel editPane = new JPanel(new BorderLayout());
    editPane.setBackground(BACKGROUND_COLOR);

    // List of users to display
    DefaultListModel<String> userListModel = new DefaultListModel<>();
    for (Resource user : listUsers) {
        userListModel.addElement(user.firstName + " " + user.lastName);
    }

    JList<String> displayedUsers = new JList<>(userListModel);
    displayedUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    displayedUsers.setBackground(SECONDARY_COLOR);
    displayedUsers.setForeground(TEXT_COLOR);
    displayedUsers.setFont(new Font("Arial", Font.PLAIN, 16));

    JScrollPane scrollPane = new JScrollPane(displayedUsers);
    scrollPane.setPreferredSize(new Dimension(200, 150));

    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(BACKGROUND_COLOR);
    formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Labels and Text Fields
    JLabel firstNameLabel = new JLabel("First Name:");
    firstNameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Bold for readability
    firstNameLabel.setForeground(new Color(230, 230, 230)); // Light text color
    firstName = new JTextField(15); // Smaller text box

    JLabel lastNameLabel = new JLabel("Last Name:");
    lastNameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Bold for readability
    lastNameLabel.setForeground(new Color(230, 230, 230)); // Light text color
    lastName = new JTextField(15); // Smaller text box

    JButton fileButton = new JButton("Choose a New File");
    styleButton(fileButton);
    fileButton.addActionListener(e -> fileFrame());

    JButton confirmEdit = new JButton("Confirm");
    styleButton(confirmEdit);
    confirmEdit.addActionListener(e -> {
      int selectedIndex = displayedUsers.getSelectedIndex();
      if (selectedIndex != -1) {
          // Get the selected user
          Resource selectedUser = listUsers.get(selectedIndex);

          // Save the original first name for the WHERE clause in the query
          String originalFirstName = selectedUser.firstName;

          // Update the user object with new data
          selectedUser.firstName = firstName.getText();
          selectedUser.lastName = lastName.getText();
          if (userImage != null) {
              selectedUser.userEncode = userImage; // Update with the new image
          }

          // Update the database with the original first name as the key
          _dao.updateUser(selectedUser, originalFirstName);

          // Confirm success and refresh UI
          JOptionPane.showMessageDialog(editFrame, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

          // Update displayed user list to reflect changes
          userListModel.set(selectedIndex, selectedUser.firstName + " " + selectedUser.lastName);

          // Close the edit frame
          editFrame.dispose();
      } else {
          JOptionPane.showMessageDialog(editFrame, "Please select a user to edit!", "Error", JOptionPane.ERROR_MESSAGE);
      }
  });

    JButton cancelEdit = new JButton("Cancel");
    styleButton(cancelEdit);
    cancelEdit.addActionListener(e -> editFrame.dispose());

    // Populate fields when a user is selected
    displayedUsers.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && displayedUsers.getSelectedIndex() != -1) {
            int selectedIndex = displayedUsers.getSelectedIndex();
            Resource selectedUser = listUsers.get(selectedIndex);
            firstName.setText(selectedUser.firstName);
            lastName.setText(selectedUser.lastName);
        }
    });

    // Add components to form panel
    formPanel.add(firstNameLabel);
    formPanel.add(firstName);
    formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
    formPanel.add(lastNameLabel);
    formPanel.add(lastName);
    formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
    formPanel.add(fileButton);

    // Buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBackground(BACKGROUND_COLOR);
    buttonPanel.add(confirmEdit);
    buttonPanel.add(cancelEdit);

    // Add everything to editPane
    editPane.add(scrollPane, BorderLayout.WEST);
    editPane.add(formPanel, BorderLayout.CENTER);
    editPane.add(buttonPanel, BorderLayout.SOUTH);

    editFrame.add(editPane);
    editFrame.setVisible(true);
}


	private Resource.Result fileFrame() 
	{
	  Resource.Result result = Resource.Result.RESULT_OK;
		//initialize filechooser
		JFileChooser fileChooser = new JFileChooser();
		//logic
		int selection = fileChooser.showSaveDialog(null);
		fileChooser.addChoosableFileFilter(new ImageFilter());
		fileChooser.setAcceptAllFileFilterUsed(false);

		if(selection == JFileChooser.APPROVE_OPTION) 
		{
			File file = fileChooser.getSelectedFile();
			
			if (file == null) 
			{
				System.out.println("No file selected!");
				result = Resource.Result.RESULT_INVALID_FILE;
			}
			else 
			{
				saveFile(file);
				fileAdded = true;
			}

		}
		else 
		{
			fileAdded = false;
			result = Resource.Result.RESULT_FAILED;
		}
		return result;
	}
	private void confirmDeleteFrame() {
		int option = JOptionPane.showConfirmDialog(addFrame, "Are you sure you want to delete this User?",
				"Delete Confirmation",JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if(option == 0)
			deleteUser();
	}

	//Logic
	private Resource.Result addUser() 
	{
	  Resource.Result result = Resource.Result.RESULT_OK;
	  if(fileAdded == true) 
		{
			Resource newUser = new Resource();
			newUser.firstName = firstName.getText();
			newUser.lastName = lastName.getText();
			newUser.userEncode = userImage;
			
			result = _dao.addUser(newUser);
			addFrame.dispose();
			JOptionPane.showMessageDialog(null, "Added User Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
		else 
		{
			JOptionPane.showMessageDialog(new JFrame(), "Enter an Image", "Error",JOptionPane.ERROR_MESSAGE);
			result = Resource.Result.RESULT_INVALID_FILE_FORMAT;
			
		}
	  return result;
	}
	
	private void deleteUser() 
	{
		_dao.deleteUser(user.firstName);
	}
	
	private void saveFile(File file) 
	{
		CascadeClassifier _cascade = new CascadeClassifier("resources/haarcascades/haarcascade_frontalface_default.xml");
		MatOfRect faceDetections = new MatOfRect();
		String image = file.getAbsolutePath();
		Mat userFace = new Mat();
		Mat cropFace = null;
		userFace = Imgcodecs.imread(image, Imgcodecs.IMREAD_COLOR);
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
