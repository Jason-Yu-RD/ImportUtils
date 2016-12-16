package com.jason.imports.action;

import com.jason.imports.domain.UserInfo;
import com.jason.imports.service.impl.ImportPOIServiceImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public class MainAction {

    public static void main(String[] args) {
        try{
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
            ImportPOIServiceImpl service = (ImportPOIServiceImpl)context.getBean("importPOIService");
            service.doImport("E:\\Data\\java\\project.xls", UserInfo.class);
            ImportPOIServiceImpl.writer.flush();
            ImportPOIServiceImpl.writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
