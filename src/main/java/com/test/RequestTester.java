package com.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

public class RequestTester extends HttpServlet {

	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("application/json");

		DbUtil dbutil=null;
		try {
			dbutil = new DbUtil();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(searchByKeyValue(req, res, dbutil)) {
			return;
		}

		String queryString = req.getQueryString();
		String postedValues = getAllPostedValues(req).toString();

		res.getWriter().println("Recieved data in JSON format:");


		try {
			if((queryString==null || queryString.equals("")) && postedValues.equals("")) {
				JSONArray output = dbutil.getData();
				 res.getWriter().println(output);
				return;
			}
			dbutil.insertData( "get", queryString);
			dbutil.insertData( "post", postedValues);
			dbutil.insertData( "header", getAllHeaders(req).toString());
			JSONArray output = dbutil.getData();
			res.getWriter().println(output);
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

	private boolean searchByKeyValue(HttpServletRequest req, HttpServletResponse res, DbUtil dbutil) throws IOException {
		String key = req.getParameter("key");
		if (key != null && !key.equals("")) {
			String value = req.getParameter("value");
			try {
				JSONArray output = dbutil.getDataByKeyValue(key,value);
				 res.getWriter().println(output);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}


}