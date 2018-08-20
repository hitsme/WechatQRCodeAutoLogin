package com.hitsme.wechatqrcodeautologin.dao;

import java.sql.SQLException;
import java.util.List;

import com.hitsme.wechatqrcodeautologin.bean.LoginUrlObj;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class LoginUrlDao extends BaseDaoImpl<LoginUrlObj, String> implements
        LoginUrlDaoInterface {
    Dao<LoginUrlObj, String> accountDao;

    public LoginUrlDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource,  LoginUrlObj.class);
    }

}
