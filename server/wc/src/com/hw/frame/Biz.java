package com.hw.frame;

import java.util.List;

public interface Biz<T, S> {
	public void register(T t);
	public T login(T t);
	public List<T> selectAll();
}
