package com.zzf.ssm.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzf.ssm.service.IUserRedPacketService;

@Controller
@RequestMapping(value = "/userRedPacket")
public class UserRedPacketController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserRedPacketService iUserRedPacketService;

	/**
	 * 普通方式，高并发有超发现象
	 * @param redPacketId
	 * @param userId
	 * @return
	 */
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

	/**
	 * 需要先在redis上先执行如下命令(redPacketid用具体传进来的参数替换)：
	 * hset red packet_redPacketid stock 20000
	 * hset red_packet_redPacketid unit_amount 10
	 * @param redPacketid
	 * @param userid
	 * @return
	 */
	@RequestMapping(value = "/grapRedPacketByRedis")
	@ResponseBody
	public Map<String, Object> grapRedPacketByRedis(Long redPacketid, Long userid) {
		Long result = iUserRedPacketService.grapRedPacketByRedis(redPacketid, userid);
		Map<String, Object> map = new HashMap<String, Object>();
		boolean flag = result > 0;
		map.put("success", flag);
		map.put("message", flag ? "抢红包成功" : "抢红包失败");
		return map;
	}

}
