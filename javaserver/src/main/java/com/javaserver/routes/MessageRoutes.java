package com.javaserver.routes;

import com.javaserver.controllers.MessageController;
import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;

public class MessageRoutes {
    public static final String API = "/api";
    public static final String MESSAGE = "/message";
    public static final String MESSAGE_ID = "/message/:id";
    public static final String ALL_MESSAGES = "/messages";
    public static final String LASTMESSAGE = "/lastmessage";

    public static void setupMessageRoutes(Javalin app) {
        app.routes(() -> {
            ApiBuilder.path(API, () -> {
                ApiBuilder.path(MESSAGE, () -> {
                    ApiBuilder.post(ctx -> MessageController.sendMessage(ctx));
                    ApiBuilder.path(LASTMESSAGE, () -> {
                        ApiBuilder.get(ctx -> MessageController.getLastMessage(ctx));
                    });
                });
                // ApiBuilder.path(MESSAGE_ID, () -> {
                // ApiBuilder.get(ctx -> MessageController.getMessageById(ctx));
                // ApiBuilder.put(ctx -> MessageController.updateMessage(ctx));
                // ApiBuilder.delete(ctx -> MessageController.deleteMessage(ctx));
                // });
                ApiBuilder.path(ALL_MESSAGES, () -> {
                    ApiBuilder.get(ctx -> MessageController.getAllMessagesOfUser(ctx));
                });
            });
        });
    }

}
