package Service;

import java.util.List;

import DAO.MessageDAO;
import DAO.MessageDAOImpl;
import Model.Account;
import Model.Message;

public class MessageService {
    
    MessageDAO messageDAO;
    AccountService accountService;

    public MessageService() {
        this.messageDAO = MessageDAOImpl.getInstance();
        this.accountService = new AccountService();
    }

    public Message addMessage(Message newMessage) {
        // Validate message
        String messageText = newMessage.getMessage_text();
        boolean lengthValid = (0 < messageText.length()) && (messageText.length() < 256);

        int claimedUser = newMessage.getPosted_by();
        Account foundAccount = accountService.getAccount(claimedUser);

        // Early return if invalid message
        if(!lengthValid || (foundAccount == null)) {
            return null;
        }

        // Add message to database
        int id = messageDAO.addMessage(newMessage);

        if(id >= 0) {
            return messageDAO.getMessage(id);
        } else {
            return null;
        }
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessage(int messageId) {
        return messageDAO.getMessage(messageId);
    }

    public Message deleteMessage(int messageId) {
        Message existingMessage = messageDAO.getMessage(messageId);
        int success = messageDAO.deleteMessage(messageId);

        if(success > 0) {
            return existingMessage;
        } else {
            return null;
        }
    }

    public Message updateMessage(int messageId, String newMessageText) {
        // Verify new message
        boolean validLength = (0 < newMessageText.length()) && (newMessageText.length() < 256);

        Message existingMessage = messageDAO.getMessage(messageId);

        // Early return if message is not valid
        if(!validLength || (existingMessage == null)) {
            return null;
        }

        // Update Message
        int success = messageDAO.updateMessage(messageId, newMessageText);

        if(success > 0) {
            return messageDAO.getMessage(messageId);
        } else {
            return null;
        }
    }

    public List<Message> getAllAccountMessages(int accountId) {
        return messageDAO.getAllAccountMessages(accountId);
    }
}
