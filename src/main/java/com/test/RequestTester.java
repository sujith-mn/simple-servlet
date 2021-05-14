package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestTester extends HttpServlet {

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");

		DbUtil dbutil=null;
		try {
			dbutil = new DbUtil();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(searchById(req, res, dbutil)) {
			return;
		}

		try {
			dbutil.insertData( "get", req.getQueryString());
			dbutil.insertData( "post", getAllPostedValues(req).toString());
			dbutil.insertData( "header", getAllHeaders(req).toString());
			dbutil.show(res.getWriter());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbutil.close();
	}


	private StringBuffer getAllHeaders(HttpServletRequest req) {
		StringBuffer header = new StringBuffer();

		HttpServletRequest httpRequest = (HttpServletRequest) req;
		Enumeration<String> headerNames = httpRequest.getHeaderNames();

		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				header.append(httpRequest.getHeader(headerNames.nextElement()));
			}
		}
		return header;
	}

	private StringBuffer getAllPostedValues(HttpServletRequest req) {
		StringBuffer postedData = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = req.getReader();
			while ((line = reader.readLine()) != null)
				postedData.append(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postedData;
	}

	private boolean searchById(HttpServletRequest req, HttpServletResponse res, DbUtil dbutil) throws IOException {
		String key = req.getParameter("key");
		if (key != null && !key.equals("")) {
			String value = req.getParameter("value");
			try {
				dbutil.show(key,value, res.getWriter());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}


}