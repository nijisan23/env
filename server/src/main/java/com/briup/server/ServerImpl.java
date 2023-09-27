package com.briup.server;

import com.briup.smart.env.entity.Environment;
import com.briup.smart.env.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerImpl implements Server {
    private boolean stop=false;
    @Override
    public void receive() throws Exception {
        int port=8888;
        ServerSocket ss = new ServerSocket(port);
        while (!stop){
            Socket accept = ss.accept();
//            new ThreadPoolExecutor(3,5,1, TimeUnit.MINUTES,ArrayBlockingQueue,);
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
        ss.close();

    }

    @Override
    public void shutdown() throws Exception {
        stop=true;
        shutdown();
    }
}
