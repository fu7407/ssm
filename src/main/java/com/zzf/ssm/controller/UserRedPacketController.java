package com.zzf.ssm.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzf.ssm.service.IUserRedPacketService;

@Controller
@RequestMapping(value = "/userRedPacket")
public class UserRedPacketController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserRedPacketService iUserRedPacketService;

	@RequestMapping(value = "/go", method = RequestMethod.GET)
	public String go() {
		return "test";
	}

	@RequestMapping(value = "/goforupdate", method = RequestMethod.GET)
	public String goforupdate() {
		return "testforupdate";
	}

	@RequestMapping(value = "/goforversion", method = RequestMethod.GET)
	public String goforversion() {
		return "testforversion";
	}

	@RequestMapping(value = "/goforversion2", method = RequestMethod.GET)
	public String goforversion2() {
		return "testforversion2";
	}

	@RequestMapping(value = "/goforversion3", method = RequestMethod.GET)
	public String goforversion3() {
		return "testforversion3";
	}

	@RequestMapping(value = "/grapRedPacket")
	@ResponseBody
	public Map<String, Object> grapRedPacket(Long redPacketId, Long userId) {
		int result = iUserRedPacketService.grapRedPacket(redPacketId, userId);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = result > 0;
		map.put("success", flag);
		map.put("message", flag ? "抢红包成功" : "抢红包失败");
		return map;
	}

	@RequestMapping(value = "/grapRedPacketForUpdate")
	@ResponseBody
	public Map<String, Object> grapRedPacketForUpdate(Long redPacketId, Long userId) {
		int result = iUserRedPacketService.grapRedPacketForUpdate(redPacketId, userId);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = result > 0;
		map.put("success", flag);
		map.put("message", flag ? "抢红包成功" : "抢红包失败");
		return map;
	}

	@RequestMapping(value = "/grapRedPacketForVersion")
	@ResponseBody
	public Map<String, Object> grapRedPacketForVersion(Long redPacketId, Long userId) {
		int result = iUserRedPacketService.grapRedPacketForVersion(redPacketId, userId);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = result > 0;
		map.put("success", flag);
		map.put("message", flag ? "抢红包成功" : "抢红包失败");
		return map;
	}

	@RequestMapping(value = "/grapRedPacketForVersion2")
	@ResponseBody
	public Map<String, Object> grapRedPacketForVersion2(Long redPacketId, Long userId) {
		int result = iUserRedPacketService.grapRedPacketForVersion2(redPacketId, userId);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = result > 0;
		map.put("success", flag);
		map.put("message", flag ? "抢红包成功" : "抢红包失败");
		return map;
	}

	@RequestMapping(value = "/grapRedPacketForVersion3")
	@ResponseBody
	public Map<String, Object> grapRedPacketForVersion3(Long redPacketId, Long userId) {
		int result = iUserRedPacketService.grapRedPacketForVersion3(redPacketId, userId);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = result > 0;
		map.put("success", flag);
		map.put("message", flag ? "抢红包成功" : "抢红包失败");
		return map;
	}

}
