package com.briup.client;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

public class ClientImpl implements Client, PropertiesAware {
    String ip;
    int port ;


    @Override
    public void init(Properties properties) throws Exception {
        ip= (String) properties.get("client-host");
        port= (int) properties.get("client-port");
    }

    @Override
    public void send(Collection<Environment> collection) throws Exception {
        Socket socket = new Socket(ip,port);
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(collection);
        oos.flush();
        oos.close();
        socket.close();
    }
}
