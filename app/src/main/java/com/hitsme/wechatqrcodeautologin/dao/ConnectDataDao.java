package com.hitsme.wechatqrcodeautologin.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.hitsme.wechatqrcodeautologin.bean.LoginUrlObj;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ConnectDataDao {
    public static void main(String[] args) throws SQLException {

        Dao<LoginUrlObj, String> accountDao;
            accountDao = DaoManager.createDao(ConnectMySql("jdbc:mysql://localhost/mysql"), LoginUrlObj.class);
            // create table
            TableUtils.createTableIfNotExists(ConnectMySql("jdbc:mysql://localhost/mysql"), LoginUrlObj.class);

            LoginUrlObj cp = new LoginUrlObj();
            cp.setLoginurl("dd1");
            cp.setUpdate(new Date());
            cp.setIsLogin(true);

            // save objects to DB
            accountDao.create(cp);

            // retrieve all objects from DB
            List<LoginUrlObj> list = accountDao.queryBuilder().orderBy("id", false).query();
            System.out.println("*******List of objects saved in DB*******");
            for (LoginUrlObj cellPhone : list) {
                System.out.println(cellPhone);
            }
        LoginUrlObj lst = accountDao.queryBuilder().where().eq("islogin",false).queryForFirst();
            System.out.print(lst==null);
    }
    public static ConnectionSource ConnectSqlServer2008(String databaseUrl) {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            ((JdbcConnectionSource) connectionSource).setUsername("sa");
            ((JdbcConnectionSource) connectionSource)
                    .setPassword("xxx");
           // LoginUrlDao cd = new LoginUrlDao(connectionSource);
           // cd.performDBOperations(connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionSource;
    }

    public static ConnectionSource ConnectDerby(String databaseUrl) {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            ((JdbcConnectionSource) connectionSource).setUsername("root");
            ((JdbcConnectionSource) connectionSource)
                    .setPassword("xxx");
          //  LoginUrlDao cd = new LoginUrlDao(connectionSource);
          //  cd.performDBOperations(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionSource;
    }

    public static ConnectionSource ConnectMySql(String databaseUrl) {
        ConnectionSource connectionSource=null;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            ((JdbcConnectionSource) connectionSource).setUsername("root");
            ((JdbcConnectionSource) connectionSource)
                    .setPassword("password");
          //  LoginUrlDao cd = new LoginUrlDao(connectionSource);
           // cd.performDBOperations(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionSource;
    }

    public static ConnectionSource ConnectSqlite(String databaseUrl) {
        // String connectionString = "jdbc:sqlite:data.db";
        // String databaseUrl = "jdbc:mysql://localhost/itcast";
        ConnectionSource connectionSource=null;
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl);
            ((JdbcConnectionSource) connectionSource).setUsername("root");
            ((JdbcConnectionSource) connectionSource)
                    .setPassword("xxx");
         //   LoginUrlDao cd = new LoginUrlDao(connectionSource);
           // cd.performDBOperations(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connectionSource;
    }
}
