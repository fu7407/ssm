package com.zzf.ssm.redis;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.zzf.ssm.entitys.User;

@Component("userRedisTakes")
public class UserRedisTakes implements RedisBaiseTakes<String, String, User> {

	@Resource(name = "redisTemplate")
	private RedisTemplate redisTemplate;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void add(String key, String value) {
		if (redisTemplate == null) {
			logger.info("redisTemplate 实例化失败");
			return;
		} else {
			redisTemplate.opsForValue().set(key, value);
		}
	}

	@Override
	public void addObj(String objectKey, String key, User object) {
		if (redisTemplate == null) {
			logger.info("redisTemplate 实例化失败");
			return;
		} else {
			redisTemplate.opsForHash().put(objectKey, key, object);
		}
	}

	@Override
	public void delete(String key) {

	}

	@Override
	public void delete(List<String> listKeys) {

	}

	@Override
	public void deletObj(String objecyKey, String key) {

	}

	@Override
	public void update(String key, String value) {

	}

	@Override
	public void updateObj(String objectKey, String key, User object) {

	}

	@Override
	public String get(String key) {
		String value = (String) redisTemplate.opsForValue().get(key);
		return value;
	}

	@Override
	public User getObj(String objectKey, String key) {
		User seeUser = (User) redisTemplate.opsForHash().get(objectKey, key);
		return seeUser;
	}
}
