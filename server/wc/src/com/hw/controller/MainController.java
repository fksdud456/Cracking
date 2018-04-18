package com.hw.controller;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hw.frame.Biz;
import com.hw.vo.User;

@Controller
public class MainController {	

	   @Resource(name="userBiz")
		Biz<User,String> biz;
		

   @RequestMapping("/main.do")
   public String main() {
      return "main";
   } 
   
	@RequestMapping("/useraddimpl.do")
	public String addimpl(Model m,User u) {		
			
		biz.register(u);
		return "complete";
	}
	
   @RequestMapping("/apply.do")
   public String apply(HttpServletRequest res) {
	  
	  String id = res.getParameter("id");
	  String pwd = res.getParameter("pwd");
	  String name = res.getParameter("name");
	  System.out.println(id+" "+pwd +" "+name);
	  
	  User u = new User(id, pwd, name);
	  biz.register(u);	 
      return "main";
   } 
}