package com.zzf.ssm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zzf.ssm.dao.IRedPacketDao;
import com.zzf.ssm.dao.IUserRedPacketDao;
import com.zzf.ssm.entitys.RedPacket;
import com.zzf.ssm.entitys.UserRedPacket;
import com.zzf.ssm.service.IRedisRedPacketService;
import com.zzf.ssm.service.IUserRedPacketService;

import redis.clients.jedis.Jedis;

@Service
public class UserRedPacketServiceimpl implements IUserRedPacketService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IUserRedPacketDao iUserRedPacketDao;

	@Autowired
	private IRedPacketDao iRedPacketDao;

	private static final int FAILED = 0;

	/**
	 * 普通方式，高并发有超发现象
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int grapRedPacket(Long redPacketId, Long userId) {
		logger.info("redPacketId=" + redPacketId + ",userId=" + userId);
		// 获取红包信息
		RedPacket redPacket = iRedPacketDao.getRedPacket(redPacketId);
		// 当前小红包库存大于0
		if (redPacket.getStock() > 0) {
			iRedPacketDao.decreaseRedPacket(redPacketId);
			// 生成抢红包信息
			UserRedPacket userRedPacket = new UserRedPacket();
			userRedPacket.setRedPacketId(redPacketId);
			userRedPacket.setUserId(userId);
			userRedPacket.setAmount(redPacket.getUnitAmount());
			userRedPacket.setNote("抢红包" + redPacketId);
			// 插入抢红包信息
			int result = iUserRedPacketDao.grapRedPacket(userRedPacket);
			return result;
		}
		return FAILED;
	}

	/**
	 * 悲观锁方式，性能下降，速度慢
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int grapRedPacketForUpdate(Long redPacketId, Long userId) {
		logger.info("redPacketId=" + redPacketId + ",userId=" + userId);
		// 获取红包信息
		RedPacket redPacket = iRedPacketDao.getRedPacketForUpdate(redPacketId);// 悲观锁方式
		// 当前小红包库存大于0
		if (redPacket.getStock() > 0) {
			iRedPacketDao.decreaseRedPacket(redPacketId);
			// 生成抢红包信息
			UserRedPacket userRedPacket = new UserRedPacket();
			userRedPacket.setRedPacketId(redPacketId);
			userRedPacket.setUserId(userId);
			userRedPacket.setAmount(redPacket.getUnitAmount());
			userRedPacket.setNote("抢红包" + redPacketId);
			// 插入抢红包信息
			int result = iUserRedPacketDao.grapRedPacket(userRedPacket);
			return result;
		}
		return FAILED;
	}

	/**
	 * 乐观锁，时间跟普通方式差不多，存在大量失败请求(可引入重入机制)，
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int grapRedPacketForVersion(Long redPacketId, Long userId) {
		logger.info("redPacketId=" + redPacketId + ",userId=" + userId);
		// 获取红包信息
		RedPacket redPacket = iRedPacketDao.getRedPacket(redPacketId);
		// 当前小红包库存大于0
		if (redPacket.getStock() > 0) {
			int update = iRedPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
			if (update == 0) {
				return FAILED;
			}
			// 生成抢红包信息
			UserRedPacket userRedPacket = new UserRedPacket();
			userRedPacket.setRedPacketId(redPacketId);
			userRedPacket.setUserId(userId);
			userRedPacket.setAmount(redPacket.getUnitAmount());
			userRedPacket.setNote("抢红包" + redPacketId);
			// 插入抢红包信息
			int result = iUserRedPacketDao.grapRedPacket(userRedPacket);
			return result;
		}
		return FAILED;
	}

	/**
	 * 乐观锁重入机制(时间戳方式)
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int grapRedPacketForVersion2(Long redPacketId, Long userId) {
		logger.info("redPacketId=" + redPacketId + ",userId=" + userId);
		long start = System.currentTimeMillis();
		while (true) {
			long end = System.currentTimeMillis();
			if (end - start > 100) {
				return FAILED;
			}
			// 获取红包信息
			RedPacket redPacket = iRedPacketDao.getRedPacket(redPacketId);
			// 当前小红包库存大于0
			if (redPacket.getStock() > 0) {
				int update = iRedPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
				if (update == 0) {
					continue;
				}
				// 生成抢红包信息
				UserRedPacket userRedPacket = new UserRedPacket();
				userRedPacket.setRedPacketId(redPacketId);
				userRedPacket.setUserId(userId);
				userRedPacket.setAmount(redPacket.getUnitAmount());
				userRedPacket.setNote("抢红包" + redPacketId);
				// 插入抢红包信息
				int result = iUserRedPacketDao.grapRedPacket(userRedPacket);
				return result;
			} else {
				return FAILED;
			}
		}
	}

	/**
	 * 乐观锁重入机制(重试次数方式)
	 */
	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int grapRedPacketForVersion3(Long redPacketId, Long userId) {
		logger.info("redPacketId=" + redPacketId + ",userId=" + userId);
		for (int i = 0; i < 3; i++) {
			// 获取红包信息
			RedPacket redPacket = iRedPacketDao.getRedPacket(redPacketId);
			// 当前小红包库存大于0
			if (redPacket.getStock() > 0) {
				int update = iRedPacketDao.decreaseRedPacketForVersion(redPacketId, redPacket.getVersion());
				if (update == 0) {
					continue;
				}
				// 生成抢红包信息
				UserRedPacket userRedPacket = new UserRedPacket();
				userRedPacket.setRedPacketId(redPacketId);
				userRedPacket.setUserId(userId);
				userRedPacket.setAmount(redPacket.getUnitAmount());
				userRedPacket.setNote("抢红包" + redPacketId);
				// 插入抢红包信息
				int result = iUserRedPacketDao.grapRedPacket(userRedPacket);
				return result;
			} else {
				return FAILED;
			}
		}
		return FAILED;
	}

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private IRedisRedPacketService redisRedPacketService;

	// Lua 脚本
	String script = "local listKey = 'red_packet_list＿'..KEYS[1] \n" // 缓存抢红包列表信息列表key
			+ "local redPacket = 'red_packet_'..KEYS[1] \n "// 当前被抢红包 key
			+ "local stock = tonumber(redis.call('hget', redPacket,'stock')) \n"// 获取当前红包库存
			+ "if stock <= 0 then return 0 end \n" // 没有库存，返回为 0
			+ "stock = stock-1 \n" // 库存减 1
			+ "redis.call('hset', redPacket, 'stock', tostring(stock)) \n"// 保存当前库存
			+ "redis.call('rpush', listKey, ARGV[1]) \n" // 往链表中加入当前红包信息
			+ "if stock == 0 then return 2 end \n" // 如果是最后一个红包，则返回2表示抢红包已经结束，需要将列表中的数据保存到数据库中
			+ "return 1 \n";// 如果并非最后一个红包，则返回 l，表示抢红包成功

	// 在缓存 Lua 脚本后，使用该变量保存 Redis 返回的 32 位的 SHAl 编码，使用它去执行缓存的Lua 脚本
	String shal = null;

	@Override
	public Long grapRedPacketByRedis(Long redPacketid, Long userid) {
		Long result = null;
		// 获取底层 Redis 操作对象
		Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
		try {
			// 如果脚本没有加载过 ， 那么进行加载，这样就会返回一个 shal 编码
			if (shal == null) {
				shal = jedis.scriptLoad(script);
			}
			String args = userid + "_" + System.currentTimeMillis();
			// 执行脚本，返回结果
			result = (Long) jedis.evalsha(shal, 1, redPacketid + "", args);
			// 返回 2 时为最后一个红包，此时将抢红包信息通过异步保存到数据库 中
			if (result == 2) {
				// 获取单个小红包金额
				String unitAmountStr = jedis.hget("red_packet_" + redPacketid, "unit_amount");
				// 触发保存数据库操作
				Double unitAmount = Double.parseDouble(unitAmountStr);
				logger.info("thread_ name=" + Thread.currentThread().getName());
				redisRedPacketService.saveUserRedPacketByRedis(redPacketid, unitAmount);
			}
		} finally {
			// 确保jedis顺利关闭
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
		return result;
	}

	@Override
	public void initRedis(Long redPacketid) {
		// 获取底层 Redis 操作对象
		Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
		try {
			jedis.hset("red_packet_" + redPacketid, "stock", "100");
			jedis.hset("red_packet_" + redPacketid, "unit_amount", "1000");
		} finally {
			// 确保jedis顺利关闭
			if (jedis != null && jedis.isConnected()) {
				jedis.close();
			}
		}
	}

}
