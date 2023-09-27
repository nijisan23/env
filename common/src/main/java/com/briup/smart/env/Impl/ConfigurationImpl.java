package com.briup.smart.env.Impl;

import com.briup.smart.env.Configuration;
import com.briup.smart.env.client.Client;
import com.briup.smart.env.client.Gather;
import com.briup.smart.env.server.DBStore;
import com.briup.smart.env.server.Server;
import com.briup.smart.env.support.ConfigurationAware;
import com.briup.smart.env.support.PropertiesAware;
import com.briup.smart.env.util.Backup;
import com.briup.smart.env.util.Log;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ConfigurationImpl implements Configuration {
    Map<String,Object> map=new HashMap<>();

    private ConfigurationImpl() throws Exception {
        String file="src/main/resources/conf.xml";
        SAXReader saxReader = new SAXReader();
        Document read = saxReader.read(file);
        Element root = read.getRootElement();

        List<Element> elements = root.elements();
        for (Element element : elements) {
            Class<?> aClass = Class.forName(element.attributeValue("class"));
            Object o = aClass.newInstance();
            if (o instanceof PropertiesAware){
                PropertiesAware o1 = (PropertiesAware) o;
                List<Element> elements1 = element.elements();
                Properties properties = new Properties();
                for (Element o2 : elements1) {
                    properties.setProperty(o2.getName(),o2.getText());
                }
                o1.init(properties);
            }
            if (o instanceof ConfigurationAware){
                ConfigurationAware o1 = (ConfigurationAware) o;
                o1.setConfiguration(this);
            }
        map.put(element.getName(),o);

        }
    }


    public static Configuration getInstance() throws Exception {
        return new ConfigurationImpl();
    }


    @Override
    public Log getLogger() throws Exception {
        return (Log)map.get("logger");
    }

    @Override
    public Server getServer() throws Exception {
        return (Server)map.get("server");
    }

    @Override
    public Client getClient() throws Exception {
        return (Client)map.get("client");
    }

    @Override
    public DBStore getDbStore() throws Exception {
        return (DBStore)map.get("dbStore");
    }

    @Override
    public Gather getGather() throws Exception {
        return (Gather)map.get("gather");
    }

    @Override
    public Backup getBackup() throws Exception {
        return (Backup)map.get("backup");
    }
}
