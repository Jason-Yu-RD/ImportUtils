package com.jason.imports.filter;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public interface ImportFilter<T> {

    public boolean doFilter(T object);

}
