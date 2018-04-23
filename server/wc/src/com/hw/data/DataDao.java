package com.hw.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hw.frame.Dao;
import com.hw.mapper.DataMapper;
import com.hw.vo.Data;

@Repository("dataDao")
public class DataDao  implements Dao<Data, String>  {
	@Autowired
	DataMapper mapper;

	@Override
	public Data select(String s) {
		// TODO Auto-generated method stub
		return mapper.select(s);
	}

	@Override
	public List<Data> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public void insert(Data t) {
		mapper.insert(t);		
	}

	@Override
	public void update(Data t) {
		mapper.update(t);
	}

	@Override
	public List<Data> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAll() {
		mapper.updateAll();
		
	}


	

}
