package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAO {

    int addMessage(Message newMessage);

    List<Message> getAllMessages();

    Message getMessage(int messageId);

    int deleteMessage(int messageId);

    int updateMessage(int messageId, String newMessageText);

    List<Message> getAllAccountMessages(int accountId);
    
}
