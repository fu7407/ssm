package com.zzf.ssm.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zzf.ssm.entitys.UserRedPacket;

@Repository
public interface IUserRedPacketDao {

	/**
	 * 插入抢红包信息
	 * @param UserRedPacket 抢红包信息
	 * @return 影响记录数
	 */
	public int grapRedPacket(UserRedPacket userRedPacket);

	/**
	 * 批量插入抢红包信息
	 * @param list 
	 * @return 影响记录数
	 */
	public int batchGrapRedPacket(List<UserRedPacket> list);

}
