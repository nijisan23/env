package com.briup.server;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerImpl implements Server, PropertiesAware, ConfigurationAware {
    private int port;
    private boolean stop=false;
    private DBStore dbStore;

    @Override
    public void setConfiguration(Configuration configuration) throws Exception {
        dbStore=  configuration.getDbStore();
    }

    @Override
    public void init(Properties properties) throws Exception {
        port= Integer.parseInt(properties.getProperty("server-port"));
    }
    @Override
    public void receive() throws Exception {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 5, 5, TimeUnit.MINUTES,
//                                                new ArrayBlockingQueue<>(5),
//                                                Thread::new,
//                                                new ThreadPoolExecutor.DiscardPolicy());
        Socket accept = new ServerSocket(port).accept();
        while (!stop){
/*            threadPoolExecutor.execute(()->{
                try {
                    Object o = new ObjectInputStream(accept.getInputStream()).readObject();
                    Collection<Environment> env=new ArrayList<>();
                    if (o instanceof Collection){
                        Collection<?> c= (Collection<?>) o;
                        for (Object e : c) {
                            if (e instanceof Environment){
                                env.add((Environment) e);
                            }
                        }
                    }
                    dbStore.saveDB(env);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });*/
            new Thread(()->{
                try {
                    Object o = new ObjectInputStream(accept.getInputStream()).readObject();
                    Collection<Environment> env=new ArrayList<>();
                    if (o instanceof Collection){
                        Collection<?> c= (Collection<?>) o;
                        for (Object e : c) {
                            if (e instanceof Environment){
                                env.add((Environment) e);
                            }
                        }
                    }
                    DbStoreImpl dbStore = new DbStoreImpl();
                    dbStore.saveDB(env);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        accept.close();

    }

    @Override
    public void shutdown() throws Exception {
        stop=true;
        shutdown();
    }
}
