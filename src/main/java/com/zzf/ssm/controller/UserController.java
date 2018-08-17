package com.zzf.ssm.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.zzf.ssm.entitys.User;
import com.zzf.ssm.redis.RedisBaiseTakes;
import com.zzf.ssm.service.IUserService;

@Controller
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserService userService;

	@RequestMapping(value = "/showname", method = RequestMethod.GET)
	public String showUserName(@RequestParam("uid") int uid, HttpServletRequest request, Model model) {
		User user = userService.getUserById(uid);
		logger.info("info:" + uid);
		if (user != null) {
			request.setAttribute("name", user.getUserName());
			model.addAttribute("mame", user.getUserName());
			return "showName";
		}
		request.setAttribute("error", "没有找到该用户！");
		return "error";
	}

	@Resource(name = "userRedisTakes")
	private RedisBaiseTakes userRedisTakes;

	@RequestMapping("/hello")
	public ModelAndView hello() {
		ModelAndView mv = new ModelAndView();
		System.out.println("hello see");
		userRedisTakes.add("hello1", "zxm");
		mv.setViewName("showName");
		return mv;
	}

	@RequestMapping("/gethello")
	public ModelAndView gethello() {
		ModelAndView mv = new ModelAndView();
		String hello = userRedisTakes.get("hello1");
		System.out.println("hello=" + hello);
		mv.setViewName("showName");
		return mv;
	}
}
