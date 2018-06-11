package com.zzf.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzf.ssm.dao.IUserDao;
import com.zzf.ssm.entitys.User;
import com.zzf.ssm.service.IUserService;

@Service("userService")
public class IUserServiceImpl implements IUserService {

	@Autowired
	public IUserDao udao;

	@Override
	public User getUserById(int id) {
		return udao.selectByPrimaryKey(id);
	}

}
