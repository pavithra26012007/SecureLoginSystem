package com.mycompany.securelogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm {

    private static String registeredUsername = null;
    private static String registeredPassword = null;
    private static int loginAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> showRegistrationForm());
    }

    // Registration Form
    private static void showRegistrationForm() {
        JFrame regFrame = new JFrame("User Registration");
        regFrame.setSize(450, 400);
        regFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        regFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 248, 255)); // light background

        JLabel title = new JLabel("Register New User");
        title.setFont(new Font("Verdana", Font.BOLD, 18));
        title.setBounds(100, 20, 300, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 70, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(180, 70, 200, 25);
        panel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(180, 110, 200, 25);
        panel.add(passField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setBounds(50, 150, 120, 25);
        panel.add(confirmLabel);

        JPasswordField confirmField = new JPasswordField();
        confirmField.setBounds(180, 150, 200, 25);
        panel.add(confirmField);

        // Show password checkbox
        JCheckBox showPassCheck = new JCheckBox("Show Password");
        showPassCheck.setBounds(180, 180, 150, 25);
        showPassCheck.setBackground(new Color(240, 248, 255));
        panel.add(showPassCheck);

        showPassCheck.addActionListener(e -> {
            if(showPassCheck.isSelected()) {
                passField.setEchoChar((char)0);
                confirmField.setEchoChar((char)0);
            } else {
                passField.setEchoChar('•');
                confirmField.setEchoChar('•');
            }
        });

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(180, 220, 100, 30);
        panel.add(registerBtn);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBounds(50, 260, 350, 40);
        panel.add(messageLabel);

        registerBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmField.getPassword());

            if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("All fields are required!");
            } else if(!password.equals(confirmPassword)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Passwords do not match!");
            } else if(!isValidPassword(password)) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("<html>Password must be 8+ chars,<br>1 uppercase, 1 lowercase, 1 digit, 1 special</html>");
            } else {
                registeredUsername = username;
                registeredPassword = password;
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Registration Successful!");

                // Wait 1.5 sec, then go to login
                Timer timer = new Timer(1500, ev -> {
                    regFrame.dispose();
                    showLoginForm();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        regFrame.add(panel);
        regFrame.setVisible(true);
    }

    // Login Form
    private static void showLoginForm() {
        JFrame loginFrame = new JFrame("User Login");
        loginFrame.setSize(400, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(224, 255, 255)); // light cyan background

        JLabel title = new JLabel("Login");
        title.setFont(new Font("Verdana", Font.BOLD, 18));
        title.setBounds(150, 20, 100, 30);
        panel.add(title);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 70, 100, 25);
        panel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(150, 70, 180, 25);
        panel.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 110, 100, 25);
        panel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(150, 110, 180, 25);
        panel.add(passField);

        JCheckBox showPassCheck = new JCheckBox("Show Password");
        showPassCheck.setBounds(150, 140, 150, 25);
        showPassCheck.setBackground(new Color(224, 255, 255));
        panel.add(showPassCheck);

        showPassCheck.addActionListener(e -> {
            if(showPassCheck.isSelected()) {
                passField.setEchoChar((char)0);
            } else {
                passField.setEchoChar('•');
            }
        });

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 180, 100, 30);
        panel.add(loginBtn);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setBounds(50, 220, 300, 25);
        panel.add(messageLabel);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if(username.equals(registeredUsername) && password.equals(registeredPassword)) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Login Successful!");
                JOptionPane.showMessageDialog(loginFrame, "Welcome, " + username + "!");
                System.exit(0);
            } else {
                loginAttempts++;
                int attemptsLeft = MAX_ATTEMPTS - loginAttempts;
                messageLabel.setForeground(Color.RED);
                if(attemptsLeft > 0) {
                    messageLabel.setText("Invalid credentials! Attempts left: " + attemptsLeft);
                } else {
                    messageLabel.setText("Account Locked due to multiple failed attempts!");
                    JOptionPane.showMessageDialog(loginFrame, "Maximum login attempts exceeded. Logging out.");
                    System.exit(0);
                }
            }
        });

        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }

    // Password validation
    private static boolean isValidPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(pattern);
    }
}
