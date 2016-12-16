package com.jason.imports.filter;

import com.jason.imports.domain.UserInfo;

/**
 * Created by yuchangcun on 2016/9/1.
 */
public class UserInfoFilter implements ImportFilter<UserInfo>{

    @Override
    public boolean doFilter(UserInfo object) {
//        if("Jason".equals(object.getUserName())){
//            return false;
//        }
        return true;
    }
}
