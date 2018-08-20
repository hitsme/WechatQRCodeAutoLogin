package com.hitsme.wechatqrcodeautologin.bean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "login_url")
public class LoginUrlObj {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String loginurl;
    @DatabaseField
    private Date update;

    @DatabaseField
    private boolean isLogin;
    public LoginUrlObj() {

    }

    public LoginUrlObj(int id, String loginurl, Date update) {
        super();
        this.id = id;
        this.loginurl = loginurl;
        this.update = update;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginurl() {
        return loginurl;
    }

    public void setLoginurl(String loginurl) {
        this.loginurl = loginurl;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public boolean getIsLogin(){return isLogin;}

    public void setIsLogin(boolean isLogin){this.isLogin=isLogin;}

    @Override
    public String toString() {
        return "*******" + loginurl + " "  + "*******";
    }
}
