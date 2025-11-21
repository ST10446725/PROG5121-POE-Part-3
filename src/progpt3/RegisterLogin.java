package progpt3;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * 
 * 
 */
public class RegisterLogin {
    // Public user variables
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String cellNumber;

    // Message collections
    public ArrayList<Message> sentMessages = new ArrayList<>();
    public ArrayList<Message> storedMessages = new ArrayList<>();
    public ArrayList<Message> discardedMessages = new ArrayList<>();
    public ArrayList<String> messageHashes = new ArrayList<>();
    public ArrayList<String> messageIDs = new ArrayList<>();

    //REGISTER
    public void registerUser() {
        JOptionPane.showMessageDialog(null, "Welcome to the Messaging System!");

        firstName = JOptionPane.showInputDialog("Enter your First Name:");
        if (firstName == null || firstName.trim().isEmpty()) return;

        lastName = JOptionPane.showInputDialog("Enter your Last Name:");
        if (lastName == null || lastName.trim().isEmpty()) return;

        boolean validUsername = false;
        while (!validUsername) {
            username = JOptionPane.showInputDialog("Create username (must contain '_' and be at least 5 characters):");
            if (username == null) return;
            validUsername = checkUsernameFormat(username);
            if (!validUsername) {
                JOptionPane.showMessageDialog(null, "Incorrect username format.");
            }
        }

        boolean validPassword = false;
        while (!validPassword) {
            password = JOptionPane.showInputDialog("Create password (8+ chars, uppercase, lowercase, number, special char):");
            if (password == null) return;
            validPassword = checkPasswordComplexity(password);
            if (!validPassword) {
                JOptionPane.showMessageDialog(null, "Incorrect password format.");
            }
        }

        boolean validCellphone = false;
        while (!validCellphone) {
            cellNumber = JOptionPane.showInputDialog("Enter cellphone number (+27 or 0):");
            if (cellNumber == null) return;
            validCellphone = checkCellphoneNumber(cellNumber);
            if (!validCellphone) {
                JOptionPane.showMessageDialog(null, "Cell number incorrectly formatted.");
            }
        }

        JOptionPane.showMessageDialog(null, "Registration successful!");
    }

    // LOGIN 
    public void loginUser() {
        if (username == null || password == null) {
            JOptionPane.showMessageDialog(null, "Please register first.");
            return;
        }

        String u = JOptionPane.showInputDialog("Enter username:");
        if (u == null) return;

        String p = JOptionPane.showInputDialog("Enter password:");
        if (p == null) return;

        if (u.equals(username) && p.equals(password)) {
            JOptionPane.showMessageDialog(null, "Welcome " + firstName + " " + lastName);
            showMessagingMenu();
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect login details.");
        }
    }

    //MAIN MENU 
    public void showMessagingMenu() {
        boolean messaging = true;

        while (messaging) {
            String menu = "Select an option:\n" +
                    "1. Send Messages\n" +
                    "2. Message Management\n" +
                    "3. View Message Statistics\n" +
                    "4. Exit";

            String choice = JOptionPane.showInputDialog(menu);
            if (choice == null) return;

            switch (choice) {
                case "1": sendMessageFeature(); break;
                case "2": showManagementMenu(); break;
                case "3": showMessageStatistics(); break;
                case "4": messaging = false; break;
                default: JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }

    //MANAGEMENT MENU 
    public void showManagementMenu() {
        boolean managing = true;

        while (managing) {
            String menu = "Message Management:\n" +
                    "1. Display sender + recipient of all sent messages\n" +
                    "2. Display longest sent message\n" +
                    "3. Search message by Message ID\n" +
                    "4. Search messages by Recipient\n" +
                    "5. Delete a message using Message Hash\n" +
                    "6. Display full detailed message report\n" +
                    "7. Back";

            String choice = JOptionPane.showInputDialog(menu);
            if (choice == null) return;

            switch (choice) {
                case "1": displaySenderRecipient(); break;
                case "2": displayLongestMessage(); break;
                case "3": searchMessageID(); break;
                case "4": searchByRecipient(); break;
                case "5": deleteByHash(); break;
                case "6": displayFullList(); break;
                case "7": managing = false; break;
                default: JOptionPane.showMessageDialog(null, "Invalid option.");
            }
        }
    }

    //TASK FUNCTIONS (PART 3)
    public void displaySenderRecipient() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages.");
            return;
        }

        StringBuilder sb = new StringBuilder("Sender → Recipient:\n\n");

        for (Message m : sentMessages) {
            sb.append(firstName).append(" ").append(lastName)
              .append(" → ").append(m.recipient).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public void displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages.");
            return;
        }

        Message longest = sentMessages.get(0);

        for (Message m : sentMessages) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }

        JOptionPane.showMessageDialog(null,
                "Longest Message:\n" +
                longest.printMessage());
    }

    public void searchMessageID() {
        String id = JOptionPane.showInputDialog("Enter Message ID:");
        if (id == null) return;

        for (Message m : sentMessages) {
            if (m.messageID.equals(id)) {
                JOptionPane.showMessageDialog(null, m.printMessage());
                return;
            }
        }

        JOptionPane.showMessageDialog(null, "No message found with that ID.");
    }

    public void searchByRecipient() {
        String rec = JOptionPane.showInputDialog("Enter recipient number:");
        if (rec == null) return;

        StringBuilder sb = new StringBuilder("Messages sent to " + rec + ":\n\n");

        boolean found = false;

        for (Message m : sentMessages) {
            if (m.recipient.equals(rec)) {
                sb.append(m.printMessage()).append("\n\n");
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "No messages for that recipient.");
        } else {
            JOptionPane.showMessageDialog(null, sb.toString());
        }
    }

    public void deleteByHash() {
        String hash = JOptionPane.showInputDialog("Enter Message Hash:");
        if (hash == null) return;

        Message toRemove = null;

        for (Message m : sentMessages) {
            if (m.messageHash.equals(hash)) {
                toRemove = m;
                break;
            }
        }

        if (toRemove != null) {
            sentMessages.remove(toRemove);
            JOptionPane.showMessageDialog(null, "Message deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Message hash not found.");
        }
    }

    public void displayFullList() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages.");
            return;
        }

        StringBuilder sb = new StringBuilder("Full Sent Message Report:\n\n");

        for (Message m : sentMessages) {
            sb.append(m.printMessage()).append("\n-------------------\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    //SEND MESSAGE FEATURE
    public void sendMessageFeature() {
        try {
            String numMessagesStr = JOptionPane.showInputDialog("How many messages?");
            if (numMessagesStr == null) return;

            int numMessages = Integer.parseInt(numMessagesStr);

            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Enter a positive number.");
                return;
            }

            for (int i = 0; i < numMessages; i++) {
                String rec = JOptionPane.showInputDialog("Enter recipient:");
                if (rec == null) return;

                // Validate recipient before creating message
                if (!checkCellphoneNumber(rec)) {
                    JOptionPane.showMessageDialog(null, "Recipient number incorrectly formatted. Message skipped.");
                    continue;
                }

                String text = JOptionPane.showInputDialog("Enter message text:");
                if (text == null) return;

                Message m = new Message(rec, text);

                messageHashes.add(m.messageHash);
                messageIDs.add(m.messageID);

                String[] options = {"Send", "Discard", "Store"};
                int action = JOptionPane.showOptionDialog(null,
                        m.printMessage(),
                        "Message Options",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null, options, options[0]);

                switch (action) {
                    case 0: sentMessages.add(m); break;
                    case 1: discardedMessages.add(m); break;
                    case 2: storedMessages.add(m); break;
                    default: discardedMessages.add(m); break; // treat close as discard
                }
            }

            saveMessagesToJSON();
            showMessageStatistics();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Please enter a valid integer for number of messages.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    //STATISTICS
    public void showMessageStatistics() {
        JOptionPane.showMessageDialog(null,
                "Message Statistics:\n" +
                "Sent: " + sentMessages.size() + "\n" +
                "Stored: " + storedMessages.size() + "\n" +
                "Discarded: " + discardedMessages.size() + "\n" +
                "Total: " + Message.totalMessages);
    }

    //VALIDATION
    public boolean checkUsernameFormat(String username) {
        return username != null && username.length() >= 5 && username.contains("_");
    }

    public boolean checkPasswordComplexity(String password) {
        return password != null &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#$%^&*+=].*") &&
                password.length() >= 8;
    }

    public boolean checkCellphoneNumber(String cellNumber) {
        return cellNumber != null && cellNumber.matches("^(\\+27|0)[6-8][0-9]{8}$");
    }

    //JSON SAVE (manual)
    public void saveMessagesToJSON() {
        try (FileWriter w = new FileWriter("messages.json")) {
            StringBuilder arr = new StringBuilder();
            arr.append("[");

            boolean first = true;
            for (Message m : sentMessages) {
                if (!first) arr.append(",");
                arr.append(m.toJSON());
                first = false;
            }
            for (Message m : storedMessages) {
                if (!first) arr.append(",");
                arr.append(m.toJSON());
                first = false;
            }

            arr.append("]");

            StringBuilder root = new StringBuilder();
            root.append("{");
            root.append("\"User\":\"").append(escapeJSON(firstName)).append(" ")
                    .append(escapeJSON(lastName)).append("\",");
            root.append("\"Messages\":").append(arr.toString());
            root.append("}");

            w.write(root.toString());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving JSON: " + e.getMessage());
        }
    }

    // Simple JSON escape for names
    private String escapeJSON(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
