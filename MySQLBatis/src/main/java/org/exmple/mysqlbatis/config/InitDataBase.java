package org.exmple.mysqlbatis.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 初始化java数据库
 *
 * @author mind
 * @date 2020/8/3 13:45
 */
@Component
@Slf4j
public class InitDataBase {

    public void init() throws SQLException{
        // 创建ScriptRunner，用于执行SQL脚本，如果想要初始化后创建数据库，那么一开始在此处初始化的时候需要连接一个已经存在的数据。
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/trydb", "root","123456");){
            log.info("正在初始化sql脚本........");
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setErrorLogWriter(null);
            runner.setLogWriter(null);
            // 执行SQL脚本
            runner.runScript(Resources.getResourceAsReader("database/schema.sql"));
            log.info("初始化完成!");
        }catch (Exception e){
            log.error("init sql failed!",e);
        }
    }

}
