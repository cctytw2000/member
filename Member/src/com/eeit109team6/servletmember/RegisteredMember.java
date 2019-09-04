package com.eeit109team6.servletmember;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.eeit109team6.memberDao.IMemberDao;
import com.eeit109team6.memberDao.Member;
import com.eeit109team6.memberDao.MemberDaoFactoery;

@WebServlet("/RegisteredMember")
public class RegisteredMember extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Resource(name = "jdbc/team6project")
	private DataSource ds;
	Connection conn;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			conn = ds.getConnection();
			System.out.println("conn getConnection");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String username = request.getParameter("username");
		String idnumber = request.getParameter("idnumber");
		String sex = request.getParameter("sex");
		String birth = request.getParameter("birth");
//		Date registeredDate = new Date();
		Calendar rightNow = Calendar.getInstance();
		int isactive = 0;

		Member mem = new Member();
		mem.setAccount(account);
		mem.setPassword(password);
		mem.setUsername(username);
		mem.setIdnumber(idnumber);
		mem.setSex(sex);
		mem.setBirth(birth);
		
		String registeredtime = rightNow.get(Calendar.YEAR) + "-" + (rightNow.get(Calendar.MONTH) + 1) + "-"
				+ rightNow.get(Calendar.DATE) + " " + rightNow.get(Calendar.HOUR) + ":" + rightNow.get(Calendar.MINUTE)
				+ ":" + rightNow.get(Calendar.SECOND);
		mem.setRegisteredtime(registeredtime);
		mem.setIsactive(isactive);
		TokenProccessor token_onject = new TokenProccessor();
		String token_notformat = token_onject.makeToken();
		String token = token_notformat.replaceAll("[\\pP\\p{Punct}]","").replace(" ", "");
		mem.setToken(token);
		int memberId = 0;
		try {
			IMemberDao MemDao = MemberDaoFactoery.createMember();

			memberId = MemDao.add(mem, conn);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("memberId="+memberId);
		
		
		System.out.println("token="+token);
		
//
		  String host = "smtp.gmail.com";
		  int port = 587;
		  final String Email = "cctytw2000@gmail.com";
		  final String EmailPwd = "jtes5505";//your password

		  Properties props = new Properties();
		  props.put("mail.smtp.host", host);
		  props.put("mail.smtp.auth", "true");
		  props.put("mail.smtp.starttls.enable", "true");
		  props.put("mail.smtp.port", port);
		  Session session = Session.getInstance(props, new Authenticator() {
		   protected PasswordAuthentication getPasswordAuthentication() {
		    return new PasswordAuthentication(Email, EmailPwd);
		   }
		  });

		  try {

		   Message message = new MimeMessage(session);
		   message.setFrom(new InternetAddress("cctytw2000@gmail.com"));
		   message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(account));
		   message.setSubject("驗證信");
		   message.setText("Wellcome To FootBook \n http://localhost:8090/Member/CheckMember.do?id="+memberId+"&token="+token);

		   Transport transport = session.getTransport("smtp");
		   transport.connect(host, port, Email, EmailPwd);

		   Transport.send(message);
		   
		   System.out.println("寄送email結束.");
		   response.getWriter().write("<script>alert('請至您輸入的信箱收取驗證信開通帳號');document.location.href='http://localhost:8090/Member/home.jsp';</script>");

		  } catch (MessagingException e) {
		   throw new RuntimeException(e);
		  }
		 }
		
		
		
		
		
	}


