package com.example.vic_sun.fsc;

import android.app.Application;

import com.bean.ApplicationUser;

/**
 * Created by vic-sun on 2017/7/28.
 */

public class MyApplication extends Application{
    public String appVersion = "v1.0";

    private ApplicationUser loginUser = new ApplicationUser();

    public ApplicationUser getUser(){
        return loginUser;
    }

    public void setUserLogin(ApplicationUser user){
        loginUser.setUser_id(user.getUser_id());
        loginUser.setUser_flag(user.getUser_flag());
        loginUser.setUser_pwd(user.getUser_pwd());
        loginUser.setAction(user.getAction());
    }

    public void userLogout(){
        loginUser = new ApplicationUser();
    }
}
