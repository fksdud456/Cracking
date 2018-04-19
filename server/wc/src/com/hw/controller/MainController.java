package com.hw.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hw.frame.Biz;
import com.hw.vo.User;

@Controller
public class MainController {

	@Resource(name = "userBiz")
	Biz<User, String> biz;
	

	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}

	@RequestMapping("/useraddimpl.do")
	public String addimpl(Model m, User u) {

		biz.register(u);
		return "complete";
	}

	@RequestMapping("/apply.do")
	public String apply(HttpServletRequest res) {

		String id = res.getParameter("id");
		String pwd = res.getParameter("pwd");
		String name = res.getParameter("name");
		System.out.println(id + " " + pwd + " " + name);

		User u = new User(id, pwd, name);
		biz.register(u);
		return "main";
	}

	@RequestMapping("/login.do")
	public String login(Model m, HttpServletRequest res, HttpSession session) {
		System.out.println("login.do");
		String id = res.getParameter("id");
		String pwd = res.getParameter("pwd");

		
		User u = new User(id);
		User result = biz.login(u);

		
		m.addAttribute("key", 0);

		if (result != null)
			if ((result.getPwd()).equals(pwd)) {					
				HashMap<String, Integer> loginList = (HashMap<String, Integer>)session.getAttribute("login");				
				if(loginList == null) {
					loginList = new HashMap<String, Integer>();					
				}			
				loginList.put(id, 1);
				session.setAttribute("login", loginList);
				
				m.addAttribute("key", 1);
			}
		return "login";
	}

	@RequestMapping("/loginuser.do") // 테블릿으로 로그인한 정보 보내기
	public String loginuser(Model m, HttpSession session) {
		System.out.println("loginuser.do");
		HashMap<String, Integer> loginList =  (HashMap<String, Integer>)session.getAttribute("login");
		if(loginList != null) {
			m.addAttribute("loginIds",loginList);				
		}
		return "loginuser";
	}
	
	@RequestMapping("/rmsession.do") // 테블릿으로 로그인한 정보 보내기
	public void rmsession(Model m, HttpSession session) {
		System.out.println("rmsession.do");
		session.invalidate();		
	}	
	
	@RequestMapping("/alluser.do") // 테블릿으로 로그인한 정보 보내기
	public String allUser(Model m, HttpSession session) {
		System.out.println("allUser.do");
		List<User> u = biz.selectAll();

		JSONArray ja = new JSONArray();
		for(int i=0;i<5;i++) {
	    	 JSONObject member = new JSONObject();
	    	 member.put("id",u.get(i).getId());
	    	 member.put("name", u.get(i).getName());
	    	 member.put("registdate", u.get(i).getRegistdate());
	    	 member.put("img", null);
	         ja.add(member);	      
	      }	
		System.out.println(ja.toString());
		m.addAttribute("allUser", ja);		
		return "allUser";
	
	}
	
	@RequestMapping("/connection.do") // 안드로이드에서 오는 신호 들어오기
	public String connection(HttpServletRequest request, HttpSession session) {
		System.out.println("connection.do");
		
		String comm = request.getParameter("comm");
		String id = request.getParameter("id");
		System.out.println(comm);
		HashMap<String, Integer> conn;
		
		
		if(comm.equals("s")) {			
			conn =  (HashMap<String, Integer>)session.getAttribute("conn");	
			if(conn == null)
				conn = new HashMap<String, Integer>();
			conn.put(id, 1);
			System.out.println((conn.get(id)).toString());
			session.setAttribute("conn", conn);
			request.setAttribute("id", "connected with server");
			return "connectionS";
		}else if(comm.equals("t")) {
			conn =  (HashMap<String, Integer>)session.getAttribute("conn");				
			System.out.println(conn);
			if(conn == null) {
				request.setAttribute("id", "nothing is connected");
			}else {
				request.setAttribute("id",conn);
				session.setAttribute("conn", null);
			}
			return "connectionT";
		}
		return "connectionE";
		
	}

}