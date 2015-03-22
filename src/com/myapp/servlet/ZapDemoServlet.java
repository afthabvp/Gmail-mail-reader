package com.myapp.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.myapp.utils.GetAuthCode;

@SuppressWarnings("serial")
public class ZapDemoServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ServletContext context = getServletContext();
		String jsonPath = context.getRealPath("/WEB-INF/client_secret.json");
		GetAuthCode.getAuth(resp,jsonPath);
		
	}
}
