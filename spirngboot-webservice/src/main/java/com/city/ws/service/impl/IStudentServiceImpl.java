package com.city.ws.service.impl;

import com.city.ws.model.Student;
import com.city.ws.model.WsConst;
import com.city.ws.service.IStudentService;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(
        targetNamespace = WsConst.NAMESPACE_URI, //wsdl命名空间
        name = "authorPortType",                 //portType名称 客户端生成代码时 为接口名称
        serviceName = "authorService",           //服务name名称
        portName = "authorPortName",             //port名称
        endpointInterface = "com.city.ws.service.IStudentService")//指定发布webservcie的接口类，此类也需要接入@WebService注解
public class IStudentServiceImpl implements IStudentService {

    @Override
    public List<Student> getStudentInfo() {
        List<Student> stuList = new ArrayList<>();
        Student student = new Student();
        student.setAge(18);
        student.setScore(700);
        student.setName("小强");
        stuList.add(student);
        return stuList;

    }
}
