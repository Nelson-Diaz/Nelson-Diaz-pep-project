package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAOImpl implements MessageDAO {
    
    // Singleton Class
    private static MessageDAO instance;

    public static MessageDAO getInstance() {
        if(instance == null) {
            instance = new MessageDAOImpl();
        }
        return instance;
    }

    private Connection connection;

    private MessageDAOImpl() {
        this.connection = ConnectionUtil.getConnection();
    }

    @Override
    public int addMessage(Message newMessage) {
        try {
            String[] generatedColumn = {"message_id"};

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)", generatedColumn);

            stmt.setInt(1, newMessage.getPosted_by());
            stmt.setString(2, newMessage.getMessage_text());
            stmt.setLong(3, newMessage.getTime_posted_epoch());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            while(rs.next()) {
                int id = rs.getInt(1);

                return id;
            }

            // Reach here if insert failed
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Message> getAllMessages() {        
        List<Message> list = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            
            ResultSet result = stmt.executeQuery("SELECT * FROM message");

            while(result.next()) {
                int id = result.getInt("message_id");
                int account_id = result.getInt("posted_by");
                String text = result.getString("message_text");
                long epoch = result.getLong("time_posted_epoch");

                Message newMessage = new Message(id, account_id, text, epoch);
                list.add(newMessage);
            }

            // Return here if no record was found
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }

    @Override
    public Message getMessage(int messageId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message WHERE message_id=?");

            stmt.setInt(1, messageId);
            
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                int id = result.getInt("message_id");
                int account_id = result.getInt("posted_by");
                String text = result.getString("message_text");
                long epoch = result.getLong("time_posted_epoch");

                Message newMessage = new Message(id, account_id, text, epoch);

                return newMessage;
            }

            // Return here if no record was found
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int deleteMessage(int messageId) {
        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM message WHERE message_id=?");

            stmt.setInt(1, messageId);
            
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int updateMessage(int messageId, String newMessageText) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE message SET message_text=? WHERE message_id=?");

            stmt.setString(1, newMessageText);
            stmt.setInt(2, messageId);
            
            return stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Message> getAllAccountMessages(int accountId) {
        List<Message> list = new ArrayList<>();

        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM message WHERE posted_by=?");

            stmt.setInt(1, accountId);
            
            ResultSet result = stmt.executeQuery();

            while(result.next()) {
                int id = result.getInt("message_id");
                int account_id = result.getInt("posted_by");
                String text = result.getString("message_text");
                long epoch = result.getLong("time_posted_epoch");

                Message newMessage = new Message(id, account_id, text, epoch);
                list.add(newMessage);
            }

            // Return here if no record was found
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return list;
        }
    }
}
