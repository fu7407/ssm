package com.zzf.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zzf.ssm.dao.IRedPacketDao;
import com.zzf.ssm.entitys.RedPacket;
import com.zzf.ssm.service.IRedPacketService;

@Service
public class RedPacketServiceimpl implements IRedPacketService {

	@Autowired
	private IRedPacketDao iRedPacketDao;

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public RedPacket getRedPacket(Long id) {
		return iRedPacketDao.getRedPacket(id);
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public int decreaseRedPacket(Long id) {
		return iRedPacketDao.decreaseRedPacket(id);
	}

}
