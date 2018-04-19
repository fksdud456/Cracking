package com.hw.mapper;
import java.util.List;
import com.hw.vo.User;

public interface UserMapper {
	public void insert(User u);
	public User select(User u);
	public List<User> selectAll();
}
