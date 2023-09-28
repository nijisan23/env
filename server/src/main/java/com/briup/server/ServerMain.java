package com.briup.server;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.Impl.ConfigurationImpl;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.util.Log;


public class ServerMain  {
    public static void main(String[] args) throws Exception {
        Server server =  ConfigurationImpl.getInstance().getServer();
        Log log = ConfigurationImpl.getInstance().getLogger();
        log.info("服务器开启");
        try {
            server.receive();
            log.info("服务器接收");
            server.shutdown();
            log.info("服务器关闭");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("接收完毕");
    }
}
