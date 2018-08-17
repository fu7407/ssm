package com.zzf.ssm.dao;

import org.springframework.stereotype.Repository;

import com.zzf.ssm.entitys.RedPacket;

@Repository
public interface IRedPacketDao {

	/**
	 * 获取红包信息
	 * @param id 红包id
	 * @return 红包具体信息
	 */
	public RedPacket getRedPacket(Long id);

	/**
	 * 获取红包信息
	 * @param id 红包id
	 * @return 红包具体信息
	 */
	public RedPacket getRedPacketForUpdate(Long id);

	/**
	 * 扣减抢红包数
	 * @param id 红包id
	 * @return 更新记录条数
	 */
	public int decreaseRedPacket(Long id);

	/**
	 * 扣减抢红包数
	 * @param id 红包id
	 * @return 更新记录条数
	 */
	public int decreaseRedPacketForVersion(Long id, Integer version);

}
