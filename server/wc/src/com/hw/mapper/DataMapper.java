package com.hw.mapper;
import java.util.List;

import com.hw.vo.Data;

public interface DataMapper {	
	public void insert(Data s);
	public Data select(String s);
	public void updateAll();
	public void update(Data d);
	public List<Data> selectAll();
}
