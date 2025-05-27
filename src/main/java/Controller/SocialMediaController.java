package Controller;

import java.util.List;
import java.util.Objects;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.get("messages", this::messageGetHandler);
        app.get("messages/{message_id}", this::messageIdGetHandler);
        app.get("accounts/{account_id}/messages", this::accountMessageHandler);

        app.post("register", this::registerPostHandler);
        app.post("login", this::loginPostHandler);
        app.post("messages", this::messagePostHandler);

        app.delete("messages/{message_id}", this::messageIdDeleteHandler);

        app.patch("messages/{message_id}", this::messageIdPatchHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerPostHandler(Context context) {
        Account newAccount = context.bodyAsClass(Account.class);
        Account addedAccount = accountService.addAccount(newAccount);

        if(addedAccount != null) {
            context.json(addedAccount);
        } else {
            context.status(400);
        }

    }

    private void loginPostHandler(Context context) {
        Account loginAccount = context.bodyAsClass(Account.class);
        String username = loginAccount.getUsername();
        String password = loginAccount.getPassword();

        Account foundAccount = accountService.login(username, password);

        if(foundAccount != null) {
            context.json(foundAccount);
        } else {
            context.status(401);
        }
    }

    private void messagePostHandler(Context context) {
        Message ctxMessage = context.bodyAsClass(Message.class);

        // Add message to database
        Message addedMessage = messageService.addMessage(ctxMessage);

        if(addedMessage != null) {
            context.json(addedMessage);
        } else {
            context.status(400);
        }
    }

    private void messageGetHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
    }

    private void messageIdGetHandler(Context context) {
        int messageId;
        try {
            messageId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        } catch(NumberFormatException e) {
            // Result has empty body as no such message exists
            return;
        }

        Message foundMessage = messageService.getMessage(messageId);

        if(foundMessage != null) {
            context.json(foundMessage);
        }
    }

    private void messageIdDeleteHandler(Context context) {
        int messageId;
        try {
            messageId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        } catch(NumberFormatException e) {
            // Result has empty body as no such message exists
            return;
        }

        Message deletedMessage = messageService.deleteMessage(messageId);

        if(deletedMessage != null) {
            context.json(deletedMessage);
        }
    }

    private void messageIdPatchHandler(Context context) {
        Message newMessage = context.bodyAsClass(Message.class);
        String newMessageText = newMessage.getMessage_text();

        int messageId;
        try {
            messageId = Integer.parseInt(Objects.requireNonNull(context.pathParam("message_id")));
        } catch(NumberFormatException e) {
            // Patch unsuccessful: message does not exist
            context.status(400);
            return;
        }

        // Update Message
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);

        if(updatedMessage != null) {
            context.json(updatedMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void accountMessageHandler(Context context) {
        int accountId;
        try {
            accountId = Integer.parseInt(Objects.requireNonNull(context.pathParam("account_id")));
        } catch(NumberFormatException e) {
            // Result has empty body as no such user exists
            return;
        }

        List<Message> accountMessages = messageService.getAllAccountMessages(accountId);

        context.json(accountMessages);
    }
}