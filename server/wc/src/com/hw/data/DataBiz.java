package com.hw.data;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hw.frame.Biz;
import com.hw.frame.Dao;
import com.hw.vo.Data;

@Service("dataBiz")
public class DataBiz implements Biz<Data,String> {
	@Resource(name="dataDao")
	Dao<Data, String> dao;

	@Override
	public Data select(String s) {
		// TODO Auto-generated method stub
		return dao.select(s);
	}

	@Override
	public List<Data> selectAll() {
		// TODO Auto-generated method stub
		return dao.selectAll();
	}

	@Override
	public void insert(Data t) {
		dao.insert(t);		
	}

	@Override
	public void update(Data t) {
		dao.update(t);		
	}

	@Override
	public List<Data> find() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAll() {
		dao.updateAll();
		
	}

	
}
