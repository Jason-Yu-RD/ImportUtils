package com.jason.imports.service;

import com.jason.imports.exceptions.AppException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public interface ImportService<T,K> {

    /**
     * 通过路径获取输入流
     * @param filePath
     * @return
     */
    public InputStream initInputStream(String filePath) throws IOException;

    /**
     * 执行解析操作
     * @param is
     * @return 通过流创建的文档
     */
    public K doParseStream(InputStream is) throws IOException;

    /**
     * 导入文件
     * @param filePath
     */
    public void doImport(String filePath,Class<?extends T> clazz) throws IOException,AppException;


    /**
     * 插入解析数据
     * @param obj
     * @return
     */
    public int insertData(T obj);

    /**
     * 执行拦截
     * @return
     */
    public boolean doFilter(T obj);
}
