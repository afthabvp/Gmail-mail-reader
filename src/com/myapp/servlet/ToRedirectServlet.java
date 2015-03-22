package com.myapp.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.myapp.utils.GetAuthCode;
import com.myapp.utils.GmailUtils;
import com.myapp.utils.Handler;

@SuppressWarnings("serial")
public class ToRedirectServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException { 
		resp.setContentType("text/plain");
		String code=req.getQueryString().split("=")[1];	

		//Credential credential= GetAuthCode.getToken("");
		Credential credential= GetAuthCode.getToken(code);
		//		Userinfoplus userinfoplus;
		//		try {
		//			userinfoplus = MyApp.getUserInfo(credential);
		//			resp.getWriter().println("service"+userinfoplus.getEmail());
		//			resp.getWriter().println("service"+userinfoplus.getId());
		//		} catch (NoUserIdException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}	

		Gmail service=GetAuthCode.createGmailService(credential);
		//		Integer maxRes=GmailUtils.getSizeInbox(service);
		//		resp.getWriter().println(""+maxRes);
		String startDate="2015/02/01";
		String endDate="20115/02/30";
		String query="in:inbox after:"+startDate+" before:"+endDate+"";
		List<Message> messages=GmailUtils.listMessagesMatchingQuery(service, "me", query);
		Map<String, Integer> sortedMapDesc = Handler.handle(service, messages);
		resp.getWriter().println("email_id --------> convesrations  ");
		Iterator<Entry<String, Integer>> entries = sortedMapDesc.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry entry = entries.next();
			String key = (String) entry.getKey();
			Integer value = (Integer) entry.getValue();
			resp.getWriter().println("" + key + "--------> " + value);
		}



		//DataStore.addTomessage_Details(messages);


	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {


	}


}