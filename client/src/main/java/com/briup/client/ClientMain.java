package com.briup.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;

import java.util.Collection;

public class ClientMain implements ConfigurationAware {
    static ClientImpl client;
    static GatherImpl gather;
    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        client = (ClientImpl) configuration.getClient();
        gather = (GatherImpl) configuration.getGather();
    }
    public static void main(String[] args) {

        System.out.println("客户端开启");
        try {
            Collection<Environment> c = gather.gather();
            client.send(c);
            System.out.println("客户端发送");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("发送完毕");
    }


}
