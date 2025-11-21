package progpt3;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * 
 */
public class Message {
    // Public variables required by rubric
    public String messageID;
    public String recipient;
    public String messageText;
    public String messageHash;

    // Static counters and arrays for Part 3
    public static int totalMessages = 0;
    public static String[] messageIDArray = new String[100];
    public static String[] messageHashArray = new String[100];

    // Constructor
    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient == null ? "" : recipient;
        this.messageText = messageText == null ? "" : messageText;
        this.messageHash = createMessageHash();

        totalMessages++;
        storeIDandHash();
    }

    // Store message IDs + message hashes into global arrays
    private void storeIDandHash() {
        for (int i = 0; i < messageIDArray.length; i++) {
            if (messageIDArray[i] == null) {
                messageIDArray[i] = this.messageID;
                messageHashArray[i] = this.messageHash;
                break;
            }
        }
    }

    // Check Message ID length (10 digits)
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    // Instance-level recipient validation
    public boolean checkRecipient(String recipient) {
        return isValidRecipient(recipient);
    }

    // Static helper: validate recipient format
    public static boolean isValidRecipient(String recipient) {
        if (recipient == null) return false;
        return recipient.matches("^(\\+27|0)[6-8][0-9]{8}$");
    }

    // Create Message Hash following POE format
    public String createMessageHash() {
        String id = (messageID == null) ? "0000000000" : messageID;

        // FIRST TWO digits of message ID
        String firstTwo = id.substring(0, 2);

        // REST of message ID
        String messageIDNumber = id.substring(2); // 8 digits

        // FIRST and LAST words of message
        String[] words = (messageText == null) ? new String[0] : messageText.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;

        // Format required by POE document
        return firstTwo + ":0:" + messageIDNumber + firstWord + lastWord;
    }

    // Send message options text (keeps behavior from rubric)
    public String sendMessageOption(int option) {
        switch (option) {
            case 1: return "Message successfully sent.";
            case 2: return "Message successfully discarded.";
            case 3: return "Message successfully stored.";
            default: return "Invalid option selected.";
        }
    }

    // Print message details (for display)
    public String printMessage() {
        return "MessageID: " + messageID + "\n" +
               "Message Hash: " + messageHash + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + messageText;
    }

    // Return total messages (static)
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Manual JSON serialization (returns a JSON string for the message)
    public String toJSON() {
        return "{" +
               "\"MessageID\":\"" + escapeJSON(messageID) + "\"," +
               "\"MessageHash\":\"" + escapeJSON(messageHash) + "\"," +
               "\"Recipient\":\"" + escapeJSON(recipient) + "\"," +
               "\"Message\":\"" + escapeJSON(messageText) + "\"" +
               "}";
    }

    // Helper to escape JSON special chars
    private String escapeJSON(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Generate 10-digit Message ID
    public String generateMessageID() {
        long id = ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L);
        return String.format("%010d", id);
    }
}
