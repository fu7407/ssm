package com.zzf.ssm.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zzf.ssm.dao.IRedPacketDao;
import com.zzf.ssm.dao.IUserRedPacketDao;
import com.zzf.ssm.entitys.UserRedPacket;
import com.zzf.ssm.service.IRedisRedPacketService;

@Service
public class RedisRedPacketServiceimpl implements IRedisRedPacketService {

	private static final String PREFIX = "red_packet_list_";
	// 每次取出 1000 条，避免一次取出消耗太多内存
	private static final int TIME_SIZE = 1000;

	@Autowired
	private RedisTemplate redisTemplate; // RedisTemplate

	@Autowired
	private IUserRedPacketDao iUserRedPacketDao;

	@Autowired
	private IRedPacketDao iRedPacketDao;

	// 开启新线程运行
	@Override
	@Async
	public void saveUserRedPacketByRedis(Long redPacketid, Double unitAmount) {
		System.out.println("开始保存数据");
		Long start = System.currentTimeMillis();
		// 获取列表操作对象
		BoundListOperations ops = redisTemplate.boundListOps(PREFIX + redPacketid);
		Long size = ops.size();
		Long times = size % TIME_SIZE == 0 ? size / TIME_SIZE : size / TIME_SIZE + 1;
		int count = 0;
		List<UserRedPacket> userRedPacketList = new ArrayList<UserRedPacket>(TIME_SIZE);
		for (int i = 0; i < times; i++) {
			// 获取至多 TIME_SIZE 个抢红包信息
			List useridList = null;
			if (i == 0) {
				useridList = ops.range(i * TIME_SIZE, (i + 1) * TIME_SIZE);
			} else {
				useridList = ops.range(i * TIME_SIZE + 1, (i + 1) * TIME_SIZE);
			}
			userRedPacketList.clear();
			// 保存红包信息
			for (int j = 0; j < useridList.size(); j++) {
				String args = useridList.get(j).toString();
				String[] arr = args.split("一");
				String useridStr = arr[0];
				String timeStr = arr[1];
				Long userid = Long.parseLong(useridStr);
				Long time = Long.parseLong(timeStr);
				// 生成抢红包信息
				UserRedPacket userRedPacket = new UserRedPacket();
				userRedPacket.setRedPacketId(redPacketid);
				userRedPacket.setUserId(userid);
				userRedPacket.setAmount(unitAmount);
				userRedPacket.setGrabTime(new Timestamp(time));
				userRedPacket.setNote("抢红包" + redPacketid);
				userRedPacketList.add(userRedPacket);
			}
			// 插入抢红包信息
			count += executeBatch(userRedPacketList);
		}
		// 删除 Redis 列表
		redisTemplate.delete(PREFIX + redPacketid);
		Long end = System.currentTimeMillis();
		System.err.println("保存数据结束 ， 耗时 " + (end - start) + "毫秒，共 " + count + "条记录被保存。 ");
	}

	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	private int executeBatch(List<UserRedPacket> userRedPacketList) {
		iRedPacketDao.batchDecreaseRedPacket(userRedPacketList);
		iUserRedPacketDao.batchGrapRedPacket(userRedPacketList);
		return 0;
	}

}
