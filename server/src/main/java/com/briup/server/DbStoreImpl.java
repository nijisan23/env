package com.briup.server;


import com.briup.server.util.JDBCUtil;
import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;


public class DbStoreImpl implements DBStore, ConfigurationAware, PropertiesAware {
    // 维护日志对象
    private Log log;
    // 定义一个批处理的量
//    private int batchSize;
    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log = configuration.getLogger();
    }

    @Override
    public void init(Properties properties) throws Exception {
//        batchSize = Integer.parseInt(properties.getProperty("batch-size"));
    }
    @Override
    public void saveDB(Collection<Environment> collection) throws Exception {
        // 获取jdbc连接对象
        Connection conn = JDBCUtil.getDruidConnection();
        // 定义一个语句对象
        PreparedStatement pst = null;
        // 定义一个前一天的变量
        int preDay = 0;
        // 定义一个统计个数的变量
        int count = 0;
        // 1、集合遍历，取出每一个对象
        for (Environment environment : collection) {
            // 2、根据时间中的天数，插入到不同的表里
            // Calendar 或者 使用java.time包
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(environment.getGatherDate());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            int day = new Date(environment.getGatherDate().getTime()).toLocalDate().getDayOfMonth();

            // System.out.println(day); // 测试的时候，可以临时打包client引入测试

            // 根据天数来构建插入语句
            String sql = "insert into env_detail_"+day+" values(?,?,?,?,?,?,?,?,?,?)";
            // 控制pst的数量，最多只有31个
            if(pst == null) {
                pst = conn.prepareStatement(sql);
            }else{
                if (preDay != day) {
                    pst.executeBatch();
                    pst.clearBatch();
                    pst = conn.prepareStatement(sql);
                    preDay = day;
                }
            }
            // 加入数据
            pst.setString(1,environment.getName());
            pst.setString(2,environment.getSrcId());
            pst.setString(3,environment.getDesId());
            pst.setString(4,environment.getDevId());
            pst.setString(5,environment.getSensorAddress());
            pst.setInt(6,environment.getCount());
            pst.setString(7,environment.getCmd());
            pst.setFloat(8,environment.getData());
            pst.setInt(9,environment.getStatus());
            pst.setTimestamp(10,environment.getGatherDate());
            // 4、批处理
            pst.addBatch();
            count++;
            if(count % 2000 == 0){
                pst.executeBatch();
                pst.clearBatch();
            }
        }
        if(pst != null && count % 2000 !=0) {
            pst.executeBatch();
            pst.clearBatch();
        }

        System.out.println("入库成功");
    }
}
