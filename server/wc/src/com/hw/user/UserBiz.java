package com.hw.user;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hw.frame.Biz;
import com.hw.frame.Dao;
import com.hw.vo.User;

@Service("userBiz")
public class UserBiz implements Biz<User, String>{

	@Resource(name="userDao") //Libraries에 Apache Tomcat을 등록하지 않으면 오류가 날 수 있다.
	Dao<User, String> dao;
	
	public UserBiz() {
		dao = new UserDao();
	}
	
	@Transactional
	@Override
	public void register(User t) {
		dao.insert(t);
		//dao.insert(t);
	}
}
