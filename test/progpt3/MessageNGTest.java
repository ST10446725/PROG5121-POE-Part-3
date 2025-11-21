package progpt3;

import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;

public class MessageNGTest {

    RegisterLogin system;
    Message m1, m2, m3, m4, m5;

    @BeforeClass
    public void setUpClass() {
        system = new RegisterLogin();
        system.firstName = "Developer";
        system.lastName = "Tester";
        // Test Data Message 1
        m1 = new Message("+27834557896", "Did you get the cake?");
        system.sentMessages.add(m1);
        // Test Data Message 2
        m2 = new Message("+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        system.storedMessages.add(m2);
        // Test Data Message 3
        m3 = new Message("+27834484567", "Yohoooo, I am at your gate.");
        system.discardedMessages.add(m3);
        // Test Data Message 4
        m4 = new Message("0838884567", "It is dinner time !");
        system.sentMessages.add(m4);
        // Test Data Message 5
        m5 = new Message("+27838884567", "Ok, I am leaving without you.");
        system.storedMessages.add(m5);
    }
    //  REQUIRED TESTS
    @Test
    public void testSentMessagesCorrectlyPopulated() {
        assertEquals(system.sentMessages.size(), 2);
        assertEquals(system.sentMessages.get(0).messageText, "Did you get the cake?");
        assertEquals(system.sentMessages.get(1).messageText, "It is dinner time !");
    }
    @Test
    public void testDisplayLongestMessage() {
        // Longest message from test data = Test Data Message 2
        Message longest = m2;
        int max = 0;

        for (Message m : system.sentMessages) {
            if (m.messageText.length() > max) {
                max = m.messageText.length();
                longest = m;
            }
        }

        assertEquals(
                longest.messageText,
                "Where are you? You are late! I have asked you to be on time."
        );
    }
    @Test
    public void testSearchMessageID() {
        String idToFind = m2.messageID;

        Message found = null;
        for (Message m : system.storedMessages) {
            if (m.messageID.equals(idToFind)) {
                found = m;
                break;
            }
        }

        assertNotNull(found);
        assertEquals(found.messageText,
                "Where are you? You are late! I have asked you to be on time.");
    }
    @Test
    public void testSearchMessagesByRecipient() {
        String recipient = "+27838884567";

        StringBuilder results = new StringBuilder();

        for (Message m : system.storedMessages) {
            if (m.recipient.equals(recipient)) {
                results.append(m.messageText).append(" | ");
            }
        }

        String expected =
                "Where are you? You are late! I have asked you to be on time. | " +
                "Ok, I am leaving without you. | ";

        assertEquals(results.toString(), expected);
    }
    @Test
    public void testDeleteMessageByHash() {
        String hashToDelete = m2.messageHash;

        boolean deleted = false;

        for (int i = 0; i < system.storedMessages.size(); i++) {
            if (system.storedMessages.get(i).messageHash.equals(hashToDelete)) {
                system.storedMessages.remove(i);
                deleted = true;
                break;
            }
        }

        assertTrue(deleted, "Message should be deleted");
    }
    @Test
    public void testDisplayFullSentReport() {
        StringBuilder report = new StringBuilder();

        for (Message m : system.sentMessages) {
            report.append(m.messageHash).append(" | ")
                  .append(m.recipient).append(" | ")
                  .append(m.messageText).append("\n");
        }

        assertTrue(report.toString().contains("Did you get the cake?"));
        assertTrue(report.toString().contains("It is dinner time !"));
    }
    // FIXED DEFAULT TESTS
    @Test
    public void testCheckMessageID() {
        Message msg = new Message("+27830000000", "Test");
        assertTrue(msg.checkMessageID());
    }

    @Test
    public void testCheckRecipient() {
        Message msg = new Message("+27838884567", "Hi");
        assertTrue(msg.checkRecipient("+27838884567"));
    }

    @Test
    public void testIsValidRecipient() {
        assertTrue(Message.isValidRecipient("+27838884567"));
        assertFalse(Message.isValidRecipient("12345"));
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message("+27838888888", "Hello World");
        String hash = msg.createMessageHash();
        assertTrue(hash.length() > 5);
    }

    @Test
    public void testSendMessageOption() {
        Message msg = new Message("+27838888888", "Hi");
        assertEquals(msg.sendMessageOption(1), "Message successfully sent.");
        assertEquals(msg.sendMessageOption(2), "Message successfully discarded.");
        assertEquals(msg.sendMessageOption(3), "Message successfully stored.");
    }

    @Test
    public void testPrintMessage() {
        Message msg = new Message("+27838888888", "Hi");
        assertTrue(msg.printMessage().contains("MessageID"));
    }

    @Test
    public void testReturnTotalMessages() {
        assertTrue(Message.returnTotalMessages() >= 5);
    }

    @Test
    public void testToJSON() {
        Message msg = new Message("+27830000000", "JSON test");
        String json = msg.toJSON();
        assertTrue(json.contains("MessageID"));
        assertTrue(json.contains("JSON test"));
    }

    @Test
    public void testGenerateMessageID() {
        Message msg = new Message("+27832222222", "Test");
        assertEquals(msg.generateMessageID().length(), 10);
    }
}
