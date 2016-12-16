package com.jason.imports.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.imports.filter.ImportFilter;
import com.jason.imports.service.ImportService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public abstract class AbstractImportService<T,K> implements ImportService<T,K> {


    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImportService.class);

    private InputStream inputStream;

    private List<ImportFilter<T>> importFilters;

    private Class<? extends T> objectClass;

    private Map<String,Field> fieldMap;

    /**
     * 执行插入操作
     * @param obj
     * @return
     */
    public int doInsertData(T obj){
        if(!doFilter(obj)){
            LOGGER.info("数据插入拦截，插入失败，data:{}", JSON.toJSONString(obj));
            return 0;
        }
        return insertData(obj);
    }

    public K init(String filePath) throws IOException{
        this.inputStream = initInputStream(filePath);
        return doParseStream(this.inputStream);
    }

    @Override
    public boolean doFilter(T obj) {
        if(CollectionUtils.isEmpty(importFilters)){
            return true;
        }
        //这里校验类型
        try{
            for (ImportFilter filter : importFilters){
                if(!filter.doFilter(obj)){
                    return false;
                }
            }
        }catch (ClassCastException e){
            LOGGER.error("拦截器泛型数据类型与导入数据类型不一致！",e);
            return false;
        }
        return true;
    }

    public void setImportFilters(List<ImportFilter<T>> importFilters) {
        this.importFilters = importFilters;
    }

    public Class<? extends T> getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class<? extends T> objectClass) {
        this.objectClass = objectClass;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }
}
