package view;

import controller.AuthController;
import model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private AuthController authController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginFrame() {
        this.authController = new AuthController();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        setTitle("Sistem Manajemen Restoran - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Set window icon and background
        getContentPane().setBackground(new Color(245, 245, 245));
        
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("LOGIN");
        
        // Set enhanced styling
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);
        
        usernameField.setFont(inputFont);
        passwordField.setFont(inputFont);
        loginButton.setFont(buttonFont);
        
        // Enhanced field styling
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        usernameField.setPreferredSize(new Dimension(300, 45));
        passwordField.setPreferredSize(new Dimension(300, 45));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(245, 245, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        
        // Logo/Icon placeholder
        JLabel logoLabel = new JLabel("🍽️");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("SISTEM MANAJEMEN RESTORAN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Silakan masuk untuk melanjutkan");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        // Username Section
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        usernameLabel.setForeground(new Color(52, 73, 94));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Password Section
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(52, 73, 94));
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Login Button
        loginButton.setPreferredSize(new Dimension(300, 50));
        loginButton.setMaximumSize(new Dimension(300, 50));
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(52, 152, 219));
            }
        });
        
        // Arrange form components
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(30));
        formPanel.add(loginButton);
        
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // // JLabel infoTitle = new JLabel("Akun Default untuk Testing:");
        // infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // infoTitle.setForeground(new Color(52, 73, 94));
        
        // // JTextArea infoArea = new JTextArea(
        // //     // "👨‍💼 Admin       : admin / admin123\n" +
        // //     // "💰 Kasir        : kasir1 / kasir123\n" +
        // //     // "📊 Manager     : manager1 / manager123\n" +
        // //     // "👤 Customer   : customer1 / customer123"
        // // );
        // // infoArea.setEditable(false);
        // // infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        // // infoArea.setBackground(new Color(236, 240, 241));
        // // infoArea.setForeground(new Color(52, 73, 94));
        // // infoArea.setOpaque(false);
        
        // infoPanel.add(infoTitle, BorderLayout.NORTH);
        infoPanel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        // infoPanel.add(infoArea, BorderLayout.SOUTH);
        
        // Assemble main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        // Enter key support
        ActionListener loginAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        };
        
        usernameField.addActionListener(loginAction);
        passwordField.addActionListener(loginAction);
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showErrorDialog("Username dan password harus diisi!");
            return;
        }
        
        if (authController.login(username, password)) {
            User currentUser = authController.getCurrentUser();
            showSuccessDialog("Login berhasil!\nSelamat datang, " + currentUser.getFullName());
            
            // Open appropriate dashboard
            openDashboard(currentUser.getRole());
            dispose();
        } else {
            showErrorDialog("Username atau password salah!");
            passwordField.setText("");
        }
    }
    
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void openDashboard(User.Role role) {
        switch (role) {
            case ADMIN:
                new AdminFrame(authController).setVisible(true);
                break;
            case KASIR:
                new KasirFrame(authController).setVisible(true);
                break;
            case MANAGER:
                new ManagerFrame(authController).setVisible(true);
                break;
            case CUSTOMER:
                new CustomerFrame(authController).setVisible(true);
                break;
        }
    }
}