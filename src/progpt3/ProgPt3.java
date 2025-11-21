/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package progpt3;

/**
 *
 * @author RC_Student_Lab
 */
public class ProgPt3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RegisterLogin obj = new RegisterLogin();

        // Show welcome message
        javax.swing.JOptionPane.showMessageDialog(null, "Welcome to the Messaging System!");

        // Main menu loop
        boolean running = true;
        while (running) {
            String[] options = {"Register", "Login", "Exit"};
            int choice = javax.swing.JOptionPane.showOptionDialog(
                    null,
                    "Select an option:",
                    "Main Menu",
                    javax.swing.JOptionPane.DEFAULT_OPTION,
                    javax.swing.JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            switch (choice) {
                case 0: // Register
                    obj.registerUser();
                    break;
                case 1: // Login
                    obj.loginUser();
                    break;
                case 2: // Exit
                    running = false;
                    javax.swing.JOptionPane.showMessageDialog(null,"Goodbye! thank you for using");
                    System.exit(0);  // Ensure JVM exits
                    break;
                default:
                    // If dialog is closed or an unknown value is returned, also exit
                    running = false;
                    System.exit(0);
            }
        }
        }
    }
    
