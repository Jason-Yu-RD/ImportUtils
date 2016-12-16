package com.jason.imports.service.impl;

import com.alibaba.fastjson.JSON;
import com.jason.imports.domain.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * Created by yuchangcun on 2016/9/1.
 */
public class ImportPOIServiceImpl extends ImportPOIService<UserInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportPOIService.class);

    public static BufferedWriter writer = null;

    public static String UPDATE_INVESTOR = "UPDATE investor SET investor_email = '{email}',investor_phone = '{phone}' WHERE investor_pin = '{pin}';\r\n";
    public static String UPDATE_PROJECT = "UPDATE sponsor SET sponsor_email = '{email}',sponsor_phone = '{phone}' WHERE sponsor_pin = '{pin}';\r\n";
    static{
         try{
             writer = new BufferedWriter(
                     new FileWriter("E:\\Data\\java\\project.sql"));
         }catch(Exception ex){

         }
    }
    @Override
    public int insertData(UserInfo obj) {
        LOGGER.info("obj:{}", JSON.toJSONString(obj));
//        //TODO 执行数据插入操作

        String sql = UPDATE_PROJECT.replace("{email}",obj.getEmail()).replace("{phone}",obj.getPhone()).replace("{pin}",obj.getPin());
        LOGGER.info("obj:{}", sql);
//        if(null == obj || StringUtils.isBlank(obj.getTime())){
//            return 0;
//        }
//        String sql = "UPDATE project_sub SET finance_confirm_time = '"+obj.getTime()+"' WHERE project_id = "+obj.getProjectId()+";\r\n";
        try{
            writer.write(sql);
        }catch(Exception ex){

        }
        return 0;
    }
}
