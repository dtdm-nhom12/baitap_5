package com.GAEJDictionary;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.io.BufferedReader;
@SuppressWarnings("serial")

public class GAEJDictionaryServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String output = "";

		String keyword = (String) request.getParameter("keyword");
		if (keyword == null || keyword == "" || keyword.length() <= 0) {
			output = "null";
			return;
		}
		keyword = keyword.trim();
		String urlStr = "http://services.aonaware.com/DictService/DictService.asmx/Define?word=";
		urlStr = urlStr + keyword;

		URL url = new URL(urlStr);
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		StringBuffer responseStr = new StringBuffer();
		String line;

		while ((line = reader.readLine()) != null) {
			responseStr.append(line);
		}
		reader.close();

		output = responseStr.toString();
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(output.toString())));
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//Definition[Dictionary[Id='wn']]/WordDefinition/text()");
			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				output = nodes.item(i).getNodeValue();
			}
		} catch (Exception e) {

		}

		response.getWriter().println(output);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
}
