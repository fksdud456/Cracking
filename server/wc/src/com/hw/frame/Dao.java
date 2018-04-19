package com.hw.frame;

import java.util.List;

public interface Dao<T, S> {
	public void insert(T t);
	public T select(T t);
	public List<T> selectAll();
}
