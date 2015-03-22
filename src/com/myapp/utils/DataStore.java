package com.myapp.utils;

import java.util.List;

import com.google.api.services.gmail.model.Message;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;



public class DataStore{

	public static  void addTomessage_Details(List<Message> messages) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		for (Message message : messages) {

			String id = message.getId();
			Key key = KeyFactory.createKey("id", id);
			String threadId = message.getThreadId();
			Entity meassages_details = new Entity("message", key);
			if (user != null) {
				meassages_details.setProperty("author_id", user.getUserId());
			}
			meassages_details.setProperty("threadId", threadId);

			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(meassages_details);


		}
	}
}