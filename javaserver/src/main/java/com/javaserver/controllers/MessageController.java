package com.javaserver.controllers;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.javaserver.model.User;
import com.javaserver.model.Message;
import com.javaserver.utils.HibernateUtil;
import com.javaserver.utils.ResponseUtils;
import com.javaserver.utils.requests.MessageRequest;

public class MessageController {
	public static void sendMessage(Context ctx) {
		// the message that is sent in the request body as json
		System.out.println("POST /api/message");
		MessageRequest message = parseMessageRequest(ctx);
		if (message == null) {
			return;
		}

		String username = getUserNameFromSession(ctx);
		if (username == null) {
			return;
		}
		try {
			saveMessage(username, message);
			ResponseUtils.createResponse("Message sent", ctx, 200);

		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtils.createResponse("Internal server error", ctx, 500);
		}

	}

	public static void getAllMessagesOfUser(Context ctx) {
		// get all the messges from the database
		System.out.println("GET /api/messages");
		String username = getUserNameFromSession(ctx);
		if (username == null) {
			return;
		}
		String receiverUsername = ctx.queryParam("receiver");

		long userId = 0;
		long receiverId = 0;
		System.out.println(receiverId);
		try {
			userId = getUserId(username);
			receiverId = getUserId(receiverUsername);
		} catch (RuntimeException e) {
			// send a response to the client that the user is not found
			ResponseUtils.createResponse("User not found", ctx, 400);
			return;
		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtils.createResponse("Internal server error", ctx, 500);
			return;
		}

		if (receiverUsername == null) {
			ResponseUtils.createResponse("Invalid user", ctx, 400);
			return;
		}
		System.out.println(userId);
		try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {

			Query query = session.createQuery("SELECT m " +
					"FROM Message m " +
					"WHERE (m.sender.id = :userId AND m.receiver.id = :receiverId) " +
					"OR (m.sender.id = :receiverId AND m.receiver.id = :userId)", Message.class)
					.setParameter("userId", userId)
					.setParameter("receiverId", receiverId);

			java.util.List<Message> messages = query.list();

			ArrayList<HashMap<String, String>> messageArrayList = new ArrayList<>();

			messages.forEach(message -> {
				HashMap<String, String> messageHashMap = new HashMap<>();
				messageHashMap.put("sender", message.getSender().getUsername());
				messageHashMap.put("receiver", message.getReceiver().getUsername());
				messageHashMap.put("content", message.getContent());
				messageHashMap.put("date", message.getDate());
				messageHashMap.put("type", message.getType());
				messageArrayList.add(messageHashMap);
			});

			HashMap<String, Object> response = new HashMap<>();
			response.put("messages", messageArrayList);
			ResponseUtils.createResponse(response, ctx, 200);

		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtils.createResponse("Internal server error", ctx, 500);
		}

	}

	private static MessageRequest parseMessageRequest(Context ctx) {
		try {
			MessageRequest message = ctx.bodyAsClass(MessageRequest.class);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			ResponseUtils.createResponse("Invalid request body", ctx, 400);
			return null;
		}
	}

	private static String getUserNameFromSession(Context ctx) {
		String sessionId = ctx.cookie("SessionId");
		if (sessionId == null) {
			ResponseUtils.createResponse("SessionId cookie not found", ctx, 400);
			return null;
		}

		String username = ctx.sessionAttribute(sessionId);
		if (username != null) {
			ctx.cookie("SessionId", sessionId);
			return username;
		} else {
			ResponseUtils.createResponse("Unauthorized", ctx, 401);
			return null;
		}
	}

	private static void saveMessage(String sender, MessageRequest message) {
		try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
			Transaction transaction = session.beginTransaction();
			Message messageEntity = new Message();

			User senderEntity = getUserByUsername(sender);
			User receiverEntity = getUserByUsername(message.getReceiver());

			System.out.println(senderEntity);
			System.out.println(receiverEntity);

			if (senderEntity == null || receiverEntity == null) {
				throw new RuntimeException("User not found");
			}
			messageEntity.setSender(senderEntity);
			messageEntity.setReceiver(receiverEntity);
			messageEntity.setContent(message.getContent());
			messageEntity.setDate(message.getDateandtime());
			messageEntity.setType(message.getType());

			session.persist(messageEntity);
			transaction.commit();

		}
	}

	private static long getUserId(String username) {
		try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
			Long userId = (Long) session.createQuery("SELECT u.id FROM User u WHERE u.username = :username", Long.class)
					.setParameter("username", username)
					.uniqueResult();
			if (userId == null) {
				throw new RuntimeException("User not found");
			}
			return userId;
		}
	}

	private static User getUserByUsername(String username) {
		try (Session session = HibernateUtil.getSessionFactoryInstance().openSession()) {
			User user = (User) session.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
					.setParameter("username", username)
					.uniqueResult();
			if (user == null) {
				throw new RuntimeException("User not found");
			}
			return user;
		}
	}
}
