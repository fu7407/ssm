package com.zzf.ssm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zzf.ssm.dao.IRedPacketDao;
import com.zzf.ssm.dao.IUserRedPacketDao;
import com.zzf.ssm.entitys.RedPacket;
import com.zzf.ssm.entitys.UserRedPacket;
import com.zzf.ssm.service.IUserRedPacketService;

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

}
