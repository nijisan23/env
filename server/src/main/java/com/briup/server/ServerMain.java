package com.briup.server;

import javax.sql.DataSource;

public class ServerMain {
    public static void main(String[] args) {
        System.out.println("服务器开启");
        ServerImpl server = new ServerImpl();
        try {
            server.receive();
            System.out.println("服务器接收");
            server.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("接收完毕");

    }
}
