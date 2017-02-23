package com.cxdb.transation;

public interface EasyTransaction<T> {
	T excute(Object... condition) throws Exception;
}