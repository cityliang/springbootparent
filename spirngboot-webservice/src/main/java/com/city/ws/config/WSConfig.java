package com.city.ws.config;

import com.city.ws.service.impl.IStudentServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WSConfig {

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    //JAX-WS发布
//    @Bean
//    public Endpoint commonService() {
//        EndpointImpl endpoint = new EndpointImpl(springBus(), commonService);
//        endpoint.publish("/commonService");
//        return endpoint;
//    }


    /*
     * 发布endpoint
     */
    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), new IStudentServiceImpl());
        endpoint.publish("/studentService");//发布地址
        return endpoint;
    }
}
