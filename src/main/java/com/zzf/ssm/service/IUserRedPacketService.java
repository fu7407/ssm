package com.zzf.ssm.service;

public interface IUserRedPacketService {

	/**
	 * 保存抢红包信息
	 * @param redPacketId 红包编号
	 * @param userId 抢红包用户编号
	 * @return  影响记录数
	 */
	public int grapRedPacket(Long redPacketId, Long userId);

	/**
	 * 保存抢红包信息
	 * @param redPacketId 红包编号
	 * @param userId 抢红包用户编号
	 * @return  影响记录数
	 */
	public int grapRedPacketForUpdate(Long redPacketId, Long userId);

	/**
	 * 保存抢红包信息
	 * @param redPacketId 红包编号
	 * @param userId 抢红包用户编号
	 * @return  影响记录数
	 */
	public int grapRedPacketForVersion(Long redPacketId, Long userId);

	/**
	 * 保存抢红包信息
	 * @param redPacketId 红包编号
	 * @param userId 抢红包用户编号
	 * @return  影响记录数
	 */
	public int grapRedPacketForVersion2(Long redPacketId, Long userId);

	/**
	 * 保存抢红包信息
	 * @param redPacketId 红包编号
	 * @param userId 抢红包用户编号
	 * @return  影响记录数
	 */
	public int grapRedPacketForVersion3(Long redPacketId, Long userId);

	/**
	 * 通过 Redis 实现抢红包
	 * @param redPacketid 红包编号
	 * @param userid 用户编号
	 * @return 0 －没有库存，失败   1 一成功，且不是最后一个红包   2 一成功，且是最后一个红包
	 */
	public Long grapRedPacketByRedis(Long redPacketid, Long userid);
}
