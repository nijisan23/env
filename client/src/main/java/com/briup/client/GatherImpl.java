package com.briup.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;


public class GatherImpl implements Gather, PropertiesAware {

    @Override
    public void init(Properties properties) throws Exception {
       String file = (String) properties.get("gather-file-path");
       String backup= (String) properties.get("gather-backup-file-path");
    }


    @Override
    public Collection<Environment> gather() throws Exception {
        InputStream in = GatherImpl.class.getClassLoader().getResourceAsStream("data-file");
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String s = null;
        Collection<Environment> collection = new ArrayList<>();
        while ((s = br.readLine()) != null) {
            add(s, collection);
        }
        br.close();
        return collection;
    }

    private static void add(String s, Collection<Environment> collection) {
        String[] split = s.split("[|]");
        String srcId = split[0];
        String desId = split[1];
        String devId = split[2];
        String sensorAddress = split[3];
        int count = Integer.parseInt(split[4]);
        String cmd = split[5];
        int status = Integer.parseInt(split[7]);
        Timestamp gatherDate = new Timestamp(Long.parseLong(split[8]));
        String name = getName(sensorAddress);
        Environment env = new Environment(name, srcId, desId, devId, sensorAddress, count, cmd, status, -999L, gatherDate);
        String dataS = split[6];
        if ("16".equals(name)) {
            Environment env2 = new Environment(name, srcId, desId, devId, sensorAddress, count, cmd, status, -999L, gatherDate);
            env2.setName("温度");
            env2.setData(((Integer.parseInt(dataS.substring(0, 4), 16)) * (0.00268127F)) - 46.85F);
            collection.add(env2);
            env.setName("湿度");
            env.setData(((Integer.parseInt(dataS.substring(4, 8), 16)) * 0.00190735F) - 6);
        } else {
            env.setData(Integer.parseInt(dataS.substring(0, 4), 16));
        }
        collection.add(env);
    }

    private static String getName(String sensorAddress) {
        switch (sensorAddress) {
            case "16": return "16";
            case "256": return "光照强度";
            case "1280": return "二氧化碳";
        }
        return null;
    }
}
