package com.briup.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.Impl.ConfigurationImpl;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Log;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Properties;



public class ClientImpl implements Client, PropertiesAware, ConfigurationAware {
    private String ip;
    private int port;
    private Log log;

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        log = configuration.getLogger();
    }

    @Override
    public void init(Properties properties) throws Exception {
        ip = properties.getProperty("client-host");
        port = Integer.parseInt(properties.getProperty("client-port"));
    }

    @Override
    public void send(Collection<Environment> collection) throws Exception {
        Socket socket = new Socket(ip, port);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(collection);
        log.info(LocalDateTime.now()+"");
        log.info(collection.toString());
        oos.flush();
        oos.close();
        socket.close();
    }
}
