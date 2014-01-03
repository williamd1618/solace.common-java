package com.solace.data;

public interface IDaoManager {

	@SuppressWarnings("unchecked")
	public abstract <ID, T> T get(Class<?> _class);

}