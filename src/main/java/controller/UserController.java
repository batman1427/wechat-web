package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.UserService;
import bean.article;

@org.springframework.stereotype.Controller
public class UserController {
	private static final Log logger = LogFactory.getLog(UserController.class);
	@Autowired
	private UserService service;

	@RequestMapping(value = {"","sign_in"})
	public String signIn(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) {
		String ip=request.getRemoteAddr();
		logger.info("sign in called");
		session.setAttribute("user",ip);
		ArrayList<article> al=service.getRecommend(ip);
		session.setAttribute("channel","推荐");
		session.setAttribute("content", al);
		return "Main_recommend";
	}
	
	@RequestMapping("/search")
	public String search(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws Exception {
		request.setCharacterEncoding("UTF-8");
		String search = request.getParameter("search");
		String str = search.replaceAll(" ", "");
		if(str.equals("")) {
			ArrayList<article> al=service.getall("教育");
			session.setAttribute("channel","教育");
			session.setAttribute("content", al);
			return "Main_recommend";
		}
		ArrayList<article>  al=service.getsearch(search);
		//System.out.println(al.size());
		session.setAttribute("searchkey",search);
		session.setAttribute("search", al);
		//System.out.println(search);
		//搜索功能
		return "search";
	}

	@RequestMapping("/Main_recommend")
	public String Main_recommend(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws IOException {
		String ip = (String)session.getAttribute("user");
		ArrayList<article> al=service.getRecommend(ip);
		session.setAttribute("channel","推荐");
		session.setAttribute("content", al);
		return "Main_recommend";
	}
	
	@RequestMapping("/article")
	public String article(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws Exception {
		String get_time = request.getParameter("get_time");
		String ip = (String)session.getAttribute("user");
		String html=service.getarticle(get_time,ip);
		session.setAttribute("html", html);
		return "article_test";
	}
	
	@RequestMapping("/Main_house")
	public String Main_house(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws IOException {
		ArrayList<article> al=service.getall("house");
		session.setAttribute("channel","房产");
		session.setAttribute("content", al);
		return "Main_house";
	}
	
	@RequestMapping("/Main_finance")
	public String Main_finance(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws IOException {
		ArrayList<article> al=service.getall("finance");
		session.setAttribute("channel","金融");
		session.setAttribute("content", al);
		return "Main_finance";
	}
	
	@RequestMapping("/Main_entertainment")
	public String Main_entertainment(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws IOException {
		ArrayList<article> al=service.getall("entertainment");
		session.setAttribute("channel","文娱");
		session.setAttribute("content", al);
		return "Main_entertainment";
	}
	
	@RequestMapping("/Main_car")
	public String Main_car(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws IOException {
		ArrayList<article> al=service.getall("car");
		session.setAttribute("channel","汽车");
		session.setAttribute("content", al);
		return "Main_car";
	}
	
	@RequestMapping("/Main_travel")
	public String Main_travel(HttpServletResponse response,
			HttpServletRequest request,HttpSession session) throws IOException {
		ArrayList<article> al=service.getall("travel");
		session.setAttribute("channel","旅游");
		session.setAttribute("content", al);
		return "Main_travel";
	}
}
