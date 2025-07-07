package view;

import controller.AuthController;
import java.util.Scanner;
import model.User;

public class LoginView {
    private Scanner scanner;
    private AuthController authController;
    
    public LoginView() {
        this.scanner = new Scanner(System.in);
        this.authController = new AuthController();
    }
    
    public void showLoginScreen() {
        System.out.println("=================================");
        System.out.println("   SISTEM MANAJEMEN RESTORAN");
        System.out.println("=================================");
        System.out.println();
        
        while (!authController.isLoggedIn()) {
            System.out.print("Username: ");
            String username = scanner.nextLine();
            
            System.out.print("Password: ");
            String password = scanner.nextLine();
            
            if (authController.login(username, password)) {
                User currentUser = authController.getCurrentUser();
                System.out.println("\nLogin berhasil! Selamat datang, " + currentUser.getFullName());
                System.out.println("Role: " + currentUser.getRole());
                
                // Redirect to appropriate dashboard
                redirectToDashboard(currentUser.getRole());
            } else {
                System.out.println("\nLogin gagal! Username atau password salah.");
                System.out.println("Silakan coba lagi.\n");
            }
        }
    }
    
    private void redirectToDashboard(User.Role role) {
        switch (role) {
            case ADMIN:
                new AdminView(authController).showDashboard();
                break;
            case KASIR:
                new KasirView(authController).showDashboard();
                break;
            case MANAGER:
                new ManagerView(authController).showDashboard();
                break;
            case CUSTOMER:
                new CustomerView(authController).showDashboard();
                break;
        }
    }
}
