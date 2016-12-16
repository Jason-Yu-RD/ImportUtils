package com.jason.imports.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.imports.enums.ExceptionCode;
import com.jason.imports.exceptions.AppException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public abstract class ImportPOIService<T> extends AbstractImportService<T,HSSFWorkbook> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportPOIService.class);
    @Override
    public InputStream initInputStream(String filePath) throws IOException {
        File file = new File(filePath);
        return new FileInputStream(file);
    }

    @Override
    public HSSFWorkbook doParseStream(InputStream is) throws IOException {
        return new HSSFWorkbook(is);
    }

    @Override
    public void doImport(String filePath, Class<? extends T> clazz) throws IOException,AppException{
        HSSFWorkbook workbook = super.init(filePath);
        super.setObjectClass(clazz);
        super.setFieldMap(formatFieldMap(super.getObjectClass()));
        if(null == workbook){
            throw new AppException(ExceptionCode.PARSEERROR.getCode(),"HSSFWorkbook 解析对象为空");
        }
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            parseSingleSheet(workbook.getSheetAt(index));
        }
    }

    /**
     * 解析每个sheet
     * @param sheet
     */
    public void parseSingleSheet(HSSFSheet sheet){
        if(null == sheet || sheet.getLastRowNum() == 0){
            return;
        }
        Map<Integer,String> reflectMap = this.formatPropertyMap(sheet.getRow(sheet.getFirstRowNum()));
        if(null == reflectMap || CollectionUtils.isEmpty(reflectMap.keySet())){
            LOGGER.info("属性标题为空，终止本个sheet解析 sheet:{}",JSON.toJSONString(sheet));
            return;
        }
        for (int index = sheet.getFirstRowNum()+1; index <= sheet.getLastRowNum(); index++) {
            HSSFRow row = sheet.getRow(index);
            parseAndSaveSingleRow(row, reflectMap);
        }
    }

    /**
     * 解析单个Row
     * @param row
     * @param reflectMap
     * @return
     */
    public T parseSingleRow(HSSFRow row, Map reflectMap)throws AppException{
        T obj = null;
        boolean nullFlag = true;
        try{
            obj = super.getObjectClass().newInstance();
            MetaObject targetObject = SystemMetaObject.forObject(obj);

            for (short index = row.getFirstCellNum(); index <= reflectMap.size(); index++) {
                HSSFCell cell = row.getCell(index);
                if(null == cell || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                    continue;
                }
                nullFlag = false;
                String fieldName = (String)reflectMap.get(Integer.valueOf(index));
                if(StringUtils.isBlank(fieldName)){
                    LOGGER.info("第{}列对应的属性标题为空",index);
                    continue;
                }
                Field field = (Field)super.getFieldMap().get(fieldName);
                if(null == field){
                    LOGGER.error("标题属性列 ：{}对应的反射Field属性不存在", fieldName);
                    continue;
                }

                String cellValue = cell.getRichStringCellValue().toString();
                if(field.getType().equals(String.class)){
                    targetObject.setValue(fieldName,cellValue);
                }else{
                    targetObject.setValue(fieldName,JSON.parseObject(cellValue,field.getType()));
                }
            }
        }catch(Exception e){
            LOGGER.error("parseSingleRow error : row :{}", JSON.toJSONString(row),e);
            throw new AppException(ExceptionCode.PARSEERROR.getCode(),"当行数据解析失败");
        }
        return nullFlag ? null : obj;
    }

    /**
     * 解析并保存单个Row
     * @param row
     * @param reflectMap
     * @return
     */
    public int parseAndSaveSingleRow(HSSFRow row,Map<Integer,String> reflectMap){
        T obj = null;
        if(null == row){
            LOGGER.info("单个row为空，跳过本行row解析保存操作");
            return 0;
        }
        /*这里抓住异常，单行的数据解析失败不影响整体解析*/
        try{
            obj = parseSingleRow(row,reflectMap);
            if(null == obj){
                LOGGER.debug("解析ROW的对象为空 不插入 row：{}",JSON.toJSONString(row));
                return 0;
            }
            return super.doInsertData(obj);
        }catch(AppException ex){
            LOGGER.error("解析并保存单个Row出错 row:{} errCode:{} errMsg:{}", JSON.toJSONString(row),ex.getErrCode(),ex.getErrMsg());
        }catch (Exception e){
            LOGGER.error("解析并保存单个Row异常 row:{}", JSON.toJSONString(row),e);
        }
        return 0;
    }

    /**
     * 生成列号和属性的映射
     * @param headRow
     * @return
     */
    public Map<Integer,String> formatPropertyMap(HSSFRow headRow){
        if(null == headRow){
            return null;
        }
        Map<Integer,String> map = new HashMap<Integer, String>(headRow.getLastCellNum()+1);
        for (short index = headRow.getFirstCellNum() ; index < headRow.getLastCellNum(); index++) {
            HSSFCell cell = headRow.getCell(index);
            if(cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK){
                continue;
            }
            String value = cell.getRichStringCellValue().toString();
            if(StringUtils.isBlank(value)){
                continue;
            }
            map.put(Integer.valueOf(index),value);
        }
        return map;
    }

    /**
     * 获取属性名和字段的映射Map
     * @return
     */
    private Map<String,Field> formatFieldMap(Class clazz){
        if(null == clazz){
            return null;
        }
        Field[] fields = clazz.getDeclaredFields();
        Map<String,Field> fieldMap = new HashMap<String, Field>(fields.length);
        for (int index = 0; index < fields.length; index++) {
            fieldMap.put(fields[index].getName(),fields[index]);
        }
        return fieldMap;
    }
}
