package frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import backend.IDataAccess;
import backend.TestDataAccess;
import cougarCam.DoorManager;
import utils.Resource;

public class MainUI {

    private JFrame StartFrame;
    private JPanel loginPanel, adminPanel, manageUsersPanel;
    private CardLayout cardLayout;
    private JButton login, startFrontDoorCam, stopFrontDoorCam;
    private JButton manageUsersBtn, logOutBtn, addUserBtn, editUserBtn, deleteUserBtn, cancelBtn;
    private DoorManager _dmanager;
    private IDataAccess _dao;

    // Consistent color theme for the app
    private static final Color BACKGROUND_COLOR = new Color(169, 169, 169);  // Gray Background
    private static final Color BUTTON_COLOR = new Color(0, 0, 128);     // Orange
    private static final Color BUTTON_HOVER_COLOR = new Color(0, 0, 128);  // Tomato red
    private static final Color SECONDARY_COLOR = new Color(30, 144, 255);  // Light Blue
    private static final Color TEXT_COLOR = Color.WHITE;

    public MainUI(IDataAccess dao) {
        _dao = dao;
    }

    public void startApp() {
        // Initialize the main frame and CardLayout
        StartFrame = new JFrame("Cougar Camera");
        cardLayout = new CardLayout();
        StartFrame.setLayout(cardLayout);

        // Initialize panels for each page
        loginPanel = createLoginPanel();
        adminPanel = createAdminPanel();
        manageUsersPanel = createManageUsersPanel();

        // Add panels to the frame
        StartFrame.add(loginPanel, "Login");
        StartFrame.add(adminPanel, "Admin");
        StartFrame.add(manageUsersPanel, "Manage Users");

        // Set frame properties
        StartFrame.setSize(800, 500);
        StartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Make the frame visible
        StartFrame.setVisible(true);
    }

    // Create login panel with consistent theme
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("COUGAR CAM!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeLabel = new JLabel("Welcome to Cougar Camera");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        welcomeLabel.setForeground(TEXT_COLOR);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        login = new JButton("Log In");
        styleButton(login);

        login.addActionListener(e -> showLoginDialog());

        // Camera buttons - added to Login Panel
        startFrontDoorCam = new JButton("Start Front Door Camera");
        startFrontDoorCam.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(startFrontDoorCam);
        startFrontDoorCam.addActionListener(e -> startCamera());

        stopFrontDoorCam = new JButton("Stop Front Door Camera");
        stopFrontDoorCam.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleButton(stopFrontDoorCam);
        stopFrontDoorCam.setEnabled(false);  // Initially disabled
        stopFrontDoorCam.addActionListener(e -> stopCamera());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(50));  // Space between title and buttons
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalStrut(30));  // Space between label and button
        panel.add(login);
        panel.add(Box.createVerticalStrut(20));  // Space between login and camera buttons
        panel.add(startFrontDoorCam);
        panel.add(stopFrontDoorCam);

        return panel;
    }

    // Create admin panel with consistent theme
    private JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("COUGAR CAM!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        manageUsersBtn = new JButton("Manage Users");
        manageUsersBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        manageUsersBtn.setBackground(BUTTON_COLOR);
        manageUsersBtn.setForeground(TEXT_COLOR);
        manageUsersBtn.setFont(new Font("Arial", Font.BOLD, 16));
        manageUsersBtn.addActionListener(e -> showManageUsersPage());

        logOutBtn = new JButton("Log Out");
        logOutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logOutBtn.setBackground(BUTTON_COLOR);
        logOutBtn.setForeground(TEXT_COLOR);
        logOutBtn.setFont(new Font("Arial", Font.BOLD, 16));
        logOutBtn.addActionListener(e -> logOut());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(50));  // Space between title and buttons
        panel.add(manageUsersBtn);
        panel.add(Box.createVerticalStrut(20));  // Space between buttons
        panel.add(logOutBtn);

        return panel;
    }

    // Create manage users panel with consistent theme
    private JPanel createManageUsersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Manage Users");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addUserBtn = new JButton("Add User");
        editUserBtn = new JButton("Edit User");
        deleteUserBtn = new JButton("Delete User");
        cancelBtn = new JButton("Go Back");

        styleButton(addUserBtn);
        styleButton(editUserBtn);
        styleButton(deleteUserBtn);
        styleButton(cancelBtn);

        addUserBtn.addActionListener(e -> addUser());
        editUserBtn.addActionListener(e -> editUser());
        deleteUserBtn.addActionListener(e -> deleteUser());

        cancelBtn.addActionListener(e -> goBackToAdminPage());

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(50));  // Space between title and buttons
        panel.add(addUserBtn);
        panel.add(Box.createVerticalStrut(20));  // Space between buttons
        panel.add(editUserBtn);
        panel.add(Box.createVerticalStrut(20));  // Space between buttons
        panel.add(deleteUserBtn);
        panel.add(Box.createVerticalStrut(20));  // Space between buttons
        panel.add(cancelBtn);

        return panel;
    }

    // Method to style buttons consistently
    private void styleButton(JButton button) {
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

    // Show login dialog
    private void showLoginDialog() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        Object[] message = {
            "Username:", usernameField,
            "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(StartFrame, message, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateLogin(username, password)) {
                // Login successful, go to Admin page
                cardLayout.show(StartFrame.getContentPane(), "Admin");
            } else {
                JOptionPane.showMessageDialog(StartFrame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Dummy method to validate login credentials (you can modify this for actual authentication)
    private boolean validateLogin(String username, String password) {
        return username.equals("ad") && password.equals("pass");
    }

    // Admin actions
    private void showManageUsersPage() {
        cardLayout.show(StartFrame.getContentPane(), "Manage Users");
    }

    private void logOut() {
        cardLayout.show(StartFrame.getContentPane(), "Login");
    }

    private void goBackToAdminPage() {
        cardLayout.show(StartFrame.getContentPane(), "Admin");
    }

    // Camera Start/Stop Logic
    private void startCamera() {
        System.out.println("Starting Front Door Camera...");
        startFrontDoorCam.setEnabled(false);  // Disable start button
        _dmanager = new DoorManager(_dao);
        _dmanager.startDoorManager();
        stopFrontDoorCam.setEnabled(true);    // Enable stop button
    }

    private void stopCamera() {
        System.out.println("Stopping Front Door Camera...");
        startFrontDoorCam.setEnabled(true);   // Re-enable start button
        _dmanager.stopDoorManager();
        stopFrontDoorCam.setEnabled(false);   // Disable stop button
    }

    /// User management actions
  private void addUser() {
      // Add user logic here
  }

  private void editUser() {
      // Edit user logic here
  }

  private void deleteUser() {
      // Delete user logic here
  }

}

/*
public class MainUI
{

  private JLabel Welcome;
  public JButton login;
  public JButton startFrontDoorCam;
  public JButton stopFrontDoorCam;
  private JFrame StartFrame;
  JPanel tidy;
  DoorManager _dmanager;
  IDataAccess _dao;
  
  public MainUI(IDataAccess dao)
  {
    _dao = dao;
   
  }
  
  public void startApp() 
  {
    ButtonListener listener = new ButtonListener();
    tidy = new JPanel();
    FlowLayout layout = new FlowLayout();
    
    tidy.setLayout(layout);
    
    // Create a new JFrame
    StartFrame = new JFrame("Cougar Camera");

    login = new JButton("Log In");
    startFrontDoorCam = new JButton("Start Front Door Camera");
    stopFrontDoorCam = new JButton("Stop Front Door Camera");
    stopFrontDoorCam.addActionListener(listener);
    login.addActionListener(listener);
    startFrontDoorCam.addActionListener(listener);

    Welcome = new JLabel("Welcome to Cougar Camera");
    
    tidy.add(Welcome);
    tidy.add(login);
    tidy.add(startFrontDoorCam);
    tidy.add(stopFrontDoorCam);
    StartFrame.add(tidy);
    
    
     //Set frame properties
    StartFrame.setSize(800, 500);  //Set the size of the frame
    StartFrame.pack();
    // Close operation
    StartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     //Make the frame visible
    StartFrame.setVisible(true);
  }
  
  public void setMainFrameVisible(Boolean option)
  {
    StartFrame.setVisible(option);
  }
  
  class ButtonListener implements ActionListener
  {

    @Override
    public void actionPerformed(ActionEvent e) 
    {
       //TODO Auto-generated method stub
      JButton clickedButton = (JButton) e.getSource();
      if (clickedButton == login) 
      {
        
      } 
      
      else if (clickedButton == startFrontDoorCam) 
      {
        //starts system
        System.out.println("Start pressed");
        System.out.println("Starting Camera");
        _dmanager = new DoorManager(_dao);
        _dmanager.startDoorManager();
      }
      
      else if(clickedButton == stopFrontDoorCam)
      {
        System.out.println("Stop pressed");
        System.out.println("Stoping Camera");
        _dmanager.stopDoorManager();
        
        
      }
    }
  }
}
*/
