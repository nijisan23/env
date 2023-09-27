package com.briup.client;

import com.briup.smart.env.entity.Environment;
import org.junit.Test;

import java.util.Collection;

public class GatherImplTest {

    @Test
    public void gather() throws Exception {
        Collection<Environment> gather = new GatherImpl().gather();
//        Collection<Environment> gather2 = new GatherImpl().gather();
//        Collection<Environment> gather3 = new GatherImpl().gather();

        System.out.println(gather.stream().filter((x) -> x.getSensorAddress().equals("16")).count()/2);
//        System.out.println(gather2.stream().filter((x) -> x.getSensorAddress().equals("256")).count());
//        System.out.println(gather3.stream().filter((x) -> x.getSensorAddress().equals("1280")).count());
//        gather.forEach(System.out::println);
    }
}