package com.briup.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.Impl.ConfigurationImpl;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.util.Log;

import java.util.Collection;

public class ClientMain {
    private static ClientImpl client;
    private static GatherImpl gather;
    private static Log log;


    public static void main(String[] args) throws Exception {
        Configuration instance = ConfigurationImpl.getInstance();
        Client client =  instance.getClient();
        Gather gather = instance.getGather();
        Log log = instance.getLogger();
        log.info("客户端开启");
        Collection<Environment> c = null;
        try {
            c = gather.gather();
            client.send(c);
            log.info("客户端发送");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("发送完毕");
    }


}
