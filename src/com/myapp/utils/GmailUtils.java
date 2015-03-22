package com.myapp.utils;



import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
public class GmailUtils {
	// ...

	/**
	 * List all Threads of the user's mailbox matching the query.
	 *
	 * @param service Authorized Gmail API instance.
	 * @param userId User's email address. The special value "me"
	 * can be used to indicate the authenticated user.
	 * @param query String used to filter the Threads listed.
	 * @return 
	 * @throws IOException

	/**
	 * List all Threads of the user's mailbox with labelIds applied.
	 *
	 * @param service Authorized Gmail API instance.
	 * @param userId User's email address. The special value "me"
	 * can be used to indicate the authenticated user.
	 * @param labelIds String used to filter the Threads listed.
	 * @return 
	 * @throws IOException
	 */
	public static List<Thread> listThreadsWithLabels (Gmail service, String userId,
			List<String> labelIds) throws IOException {
		ListThreadsResponse response = service.users().threads().list(userId).setLabelIds(labelIds).execute();
		List<Thread> threads = new ArrayList<Thread>();
		while(response.getThreads() != null) {
			threads.addAll(response.getThreads());
			if(response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().threads().list(userId).setLabelIds(labelIds)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		for(Thread thread : threads) {
			System.out.println(thread.toPrettyString());
		}
		return threads;
	}

	// ...
	public static List<Label>  listLabels(Gmail service, String userId,HttpServletResponse resp) throws IOException {
		//ListMessagesResponse response = service.users().messages().list("me").setQ("label:INBOX").execute();
		ListLabelsResponse response = service.users().labels().list(userId).execute();
		if(response!=null){
			List<Label> labels = response.getLabels();
			for (Label label : labels) {
				resp.getWriter().println("credential token1"+label.toPrettyString());
				System.out.println(label.toPrettyString());
			}
			return labels;

		}
		else{
			resp.getWriter().println("null");
		}
		return null;
	}

	public static List<Message> listMessagesWithLabels(Gmail service, String userId,
			List<String> labelIds) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId)
				.setLabelIds(labelIds).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setLabelIds(labelIds)
						.setPageToken(pageToken).execute();
			} else {
				break;
			}
		}

		for (Message message : messages) {
			System.out.println(message.toPrettyString());
		}

		return messages;
	}



	public static List<Message> listMessagesMatchingQuery(Gmail service, String userId,
			String query) throws IOException {
		ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

		List<Message> messages = new ArrayList<Message>();
		while (response.getMessages() != null) {
			messages.addAll(response.getMessages());
			if (response.getNextPageToken() != null) {
				String pageToken = response.getNextPageToken();
				response = service.users().messages().list(userId).setQ(query)
						.setPageToken(pageToken).execute();
				messages.addAll(response.getMessages());
			} else {
				break;
			}
		}

		for (Message message : messages) {
			System.out.println(message.toPrettyString());
		}

		return messages;
	}
	
	
	
	 public static Message getMessage(Gmail service, String userId, String messageId)
		      throws IOException {
		    Message message = service.users().messages().get(userId, messageId).execute();

		    System.out.println("Message snippet: " + message.getSnippet());

		    return message;

}

}