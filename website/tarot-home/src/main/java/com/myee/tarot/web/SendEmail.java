//package com.myee.tarot.web;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.stereotype.Component;
//
//import java.util.Properties;
//
//@Component("sendemail")
//public class SendEmail {
//
//	private JavaMailSenderImpl email;
//
//    private SimpleMailMessage message;
//
//	public SendEmail() {
//		// TODO Auto-generated constructor stub
//		email = new JavaMailSenderImpl();
//		email.setHost("smtp.exmail.qq.com");
//		email.setUsername("ray.fu@mrobot.cn");
//		email.setPassword("Fyd8305263");//授权码
//		email.setPort(465);
//		Properties properties = new Properties();
//		properties.put("mail.smtp.auth", true);
//		properties.put("mail.smtp.ssl.enable", true);
//		properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//		properties.put("mail.smtp.timeout", 25000);
//		email.setJavaMailProperties(properties);
//	}
//
//	//发送邮件
//	public void send(){
//		message = new SimpleMailMessage();
//		message.setFrom("ray.fu@mrobot.cn");
//		message.setTo("ray.fu@mrobot.cn");
//		message.setSubject("WOW 邮箱激活");
//		message.setText("test");
//		email.send(message);
//	}
//
//	public static void main(String[] args) {
//		SendEmail sendEmail = new SendEmail();
//		sendEmail.send();
//	}
//}