package com.myapp.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
public class GetAuthCode {
	public static void getAuth(HttpServletResponse resp, String jsonPath)throws IOException {
		MyApp.SetJsonPath(jsonPath);
		GoogleAuthorizationCodeFlow flow=MyApp.getFlow();
		String url = flow.newAuthorizationUrl().setRedirectUri("https://zapdemotest.appspot.com/signIn")
				.build();
		resp.setStatus(resp.SC_MOVED_TEMPORARILY);
		resp.setHeader("Location", url);   
	}

	public static Credential getToken(String code) throws IOException {
		return MyApp.exchangeCode(code);
	}

	public static Gmail createGmailService(Credential credential) {
		HttpTransport httpTransport = new NetHttpTransport();
		JacksonFactory jsonFactory = new JacksonFactory();
		return  new Gmail.Builder(httpTransport, jsonFactory, credential)
		.setApplicationName("spartan").build();

	}




}
