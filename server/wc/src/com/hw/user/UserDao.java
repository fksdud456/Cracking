package com.hw.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hw.frame.Dao;
import com.hw.mapper.UserMapper;
import com.hw.vo.User;

@Repository("userDao")
public class UserDao implements Dao<User, String> {

	@Autowired
	UserMapper mapper;
	
	@Override
	public void insert(User t) {
		mapper.insert(t);
	}
	
	@Override
	public User select(User t) {
		return mapper.select(t);
	}

	@Override
	public List<User> selectAll() {
		return mapper.selectAll();
	}

}
