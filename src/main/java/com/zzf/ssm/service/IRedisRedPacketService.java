package com.zzf.ssm.service;

public interface IRedisRedPacketService {

	/**
	 * 保存redis抢红包列表
	 * @param redPacketid 抢红包编号
	 * @param unitAmount 红包金额
	 */
	public void saveUserRedPacketByRedis(Long redPacketid, Double unitAmount);
}
