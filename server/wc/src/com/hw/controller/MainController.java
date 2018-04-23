package com.hw.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hw.frame.Biz;
import com.hw.vo.Data;
import com.hw.vo.Login;
import com.hw.vo.User;

@Controller
public class MainController {

	@Resource(name = "userBiz")
	Biz<User, String> uBiz;

	@Resource(name = "loginBiz")
	Biz<Login, String> lBiz;

	@Resource(name = "dataBiz")
	Biz<Data, String> dBiz;

	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}

	@RequestMapping("/apply.do")
	public String apply(HttpServletRequest res, Model m) {

		String id = res.getParameter("id");
		String pwd = res.getParameter("pwd");
		String name = res.getParameter("name");

		User u = new User(id, pwd, name);
		uBiz.insert(u);
		Login l = new Login(id, 0, 0);
		lBiz.insert(l);
		Data d = new Data();
		d.setId(id);
		dBiz.insert(d);

		m.addAttribute("isOk", 1);
		return "apply";
	}

	@RequestMapping("/login.do")
	public String login(Model m, HttpServletRequest res, HttpSession session) {
		String id = res.getParameter("id");
		String pwd = res.getParameter("pwd");
		User u = new User(id);
		User result = uBiz.select(u.getId());
		m.addAttribute("key", 0);

		if (result != null)
			if ((result.getPwd()).equals(pwd)) {
				System.out.println("id : " + id);
				Login login = lBiz.select(id);
				login.setLoginstate(1);
				lBiz.update(login);
				m.addAttribute("key", 1);
			}
		return "login";
	}

	@RequestMapping("/loginuser.do")
	public String loginuser(Model m) {

		List<Login> list = lBiz.find();
		if (list != null) {
			JSONArray ja = new JSONArray();
			for (Login index : list) {
				JSONObject ids = new JSONObject();
				ids.put("id", index.getId());
				ids.put("connection", index.getConn());
				ids.put("data", index.getData());
				ids.put("lat", index.getLat());
				ids.put("lon", index.getLon());
				ja.add(ids);
			}
			m.addAttribute("loginIds", ja);
		}
		return "loginuser";
	}

	@RequestMapping("/location.do") // 테블릿으로 로그인한 정보 보내기
	public String location(Model m, HttpServletRequest request) {
		String comm = request.getParameter("comm");
		if (comm.equals("s")) {
			String id = request.getParameter("id");
			String lat = request.getParameter("lat");
			String lon = request.getParameter("lon");
			Double dLat = Double.parseDouble(lat);
			Double dLon = Double.parseDouble(lon);
			Login l = lBiz.select(id);
			l.setId(id);
			l.setLat(dLat);
			l.setLon(dLon);
			lBiz.update(l);
			m.addAttribute("ja", 1);

		} else if (comm.equals("t")) {
			List<Login> list = lBiz.find();
			if (list != null) {
				JSONArray ja = new JSONArray();
				for (Login index : list) {
					if (index.getLoginstate() == 1) {
						JSONObject ids = new JSONObject();
						ids.put("id", index.getId());
						ids.put("lat", index.getLat());
						ids.put("lon", index.getLon());
						ja.add(ids);
					}
				}
				System.out.println(ja.toString());
				m.addAttribute("ja", ja);
			}
		}
		return "location";
	}

	@RequestMapping("/alluser.do") // 테블릿으로 로그인한 정보 보내기
	public String allUser(Model m) {
		// System.out.println("allUser.do");
		List<User> u = uBiz.selectAll();

		JSONArray ja = new JSONArray();
		for (int i = 0; i < u.size(); i++) {
			JSONObject member = new JSONObject();
			member.put("id", u.get(i).getId());
			member.put("name", u.get(i).getName());
			member.put("registdate", u.get(i).getRegistdate());
			member.put("img", null);
			ja.add(member);
		}
		// System.out.println(ja.toString());
		m.addAttribute("allUser", ja);
		return "allUser";
	}

	@RequestMapping("/connection.do") // 안드로이드에서 오는 신호 들어오기
	public String connection(Model m, HttpServletRequest request, HttpSession session) {
		// System.out.println("---[connection.do]-------------");

		String comm = request.getParameter("comm");
		String id = request.getParameter("id");
		// System.out.println(comm);

		if (comm.equals("s")) {

			Login login = lBiz.select(id);
			login.setConn(login.getConn() + 1);
			lBiz.update(login);
			request.setAttribute("id", "connected with server");
			return "connectionS";

		} else if (comm.equals("t")) {

			List<Login> login = lBiz.find();

			if (login != null) {

				JSONArray connIds = new JSONArray();
				for (Login index : login) {
					if (index.getConn() > 0) {
						JSONObject ids = new JSONObject();
						ids.put("id", index.getId());
						connIds.add(ids);
					}
				}
				m.addAttribute("connIds", connIds);
			}
			return "connectionT";
		}
		return "connectionE";

	}

	@RequestMapping("/disconnect.do")
	public String disconnect(Model m, HttpServletRequest request) {
		String comm = request.getParameter("comm");
		if (comm.equals("t")) {
			String id = request.getParameter("id");
			System.out.println("----[Disconnect : Tablet] id : " + id);
			Login login = new Login();
			login = lBiz.select(id);
			login.setDisconn(1);
			lBiz.update(login);
			m.addAttribute("id", id);
		} else if (comm.equals("s")) {
			// System.out.println("disconnect.do");
			String id = request.getParameter("id");
			Login login = new Login();
			login = lBiz.select(id);
			if (login.getDisconn() == 1) {
				System.out.println("----[Disconnect : SmartPhone] id : " + id);
				m.addAttribute("id", login.getDisconn());
				login.setDisconn(0);
				login.setConn(0);
				login.setLoginstate(0);
				lBiz.update(login);
			}
		}
		return "disconnect";
	}

	@RequestMapping("/logout.do")
	public String logout(Model m, HttpServletRequest request) {
		// System.out.println("------[logout.do]-------------------");
		String id = request.getParameter("id");
		Login login = new Login();
		login = lBiz.select(id);
		login.setConn(0);
		login.setDisconn(0);
		login.setLoginstate(0);
		lBiz.update(login);
		return "logout";
	}

	@RequestMapping("/chart.do")
	public String chart(Model m, HttpServletRequest request) {
		return "chart";
	}

	@RequestMapping("/dataChart.do")
	@ResponseBody
	public String dataChart(Model m, HttpServletRequest request, HttpServletResponse response) throws Exception {

		response.setCharacterEncoding("EUC-KR");
		response.setContentType("application/json");

		List<Data> allData = dBiz.selectAll();
		List<Login> login = lBiz.find();
		JSONArray ja = null;
		if (login != null) {
			String color[] = { "rgba(209, 101, 159, .5)", "rgba(124, 117, 199, .5)", "rgba(160, 109, 171, .5)",
					"rgba(102, 255, 51, .5)", "rgbA(92, 134, 199, .5)" };
			int i = 0;
			ja = new JSONArray();
			for (Login name : login)
				for (Data index : allData) {
					if ((name.getId()).equals(index.getId())) {
						JSONObject ids = new JSONObject();
						ids.put("name", index.getId());
						JSONArray values = new JSONArray();
						values.add(index.getB7());
						values.add(index.getB6());
						values.add(index.getB5());
						values.add(index.getB4());
						values.add(index.getB3());
						values.add(index.getB2());
						values.add(index.getB1());
						values.add(index.getNow());
						ids.put("data", values);
						ids.put("color", color[i++]);
						ja.add(ids);
					}
				}
			
			ServletOutputStream out = response.getOutputStream();
			out.println(ja.toJSONString());
			out.close();
		}

		dBiz.updateAll();
		for (Login index : login) {
			Data data = new Data();
			data.setId(index.getId());
			data.setNow(0);
			dBiz.update(data);
		}

		return "dataChart";
	}

	@RequestMapping("/data.do")
	public String data(Model m, HttpServletRequest request) {
		// System.out.println("---[data.do]----------------------");
		String comm = request.getParameter("comm");

		if (comm.equals("s")) {
			Data data = dBiz.select(request.getParameter("id"));
			String status = request.getParameter("status");
			if (status.equals("ON")) {
				// System.out.println("data.do ON");
				data.setNow(data.getNow() + 1);
				dBiz.update(data);
				Login login = lBiz.select(data.getId());
				login.setData(1);
				lBiz.update(login);
			} else if (status.equals("OFF")) {
				// System.out.println("data.do OFF");
				Login login = lBiz.select(data.getId());
				login.setData(0);
				lBiz.update(login);
			}

		} else if (comm.equals("t")) {
			// System.out.println("data.do t=-===-=-=-=-=-=");
			List<Login> login = lBiz.find();
			JSONArray ja = new JSONArray();
			for (Login index : login) {
				if (index.getData() == 1) {
					JSONObject ids = new JSONObject();
					ids.put("id", index.getId());
					ja.add(ids);
				}
			}
			// System.out.println(ja.toString());
			m.addAttribute("data", ja);
		}
		return "data";
	}
}