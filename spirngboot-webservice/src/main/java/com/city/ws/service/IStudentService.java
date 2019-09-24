package com.city.ws.service;

import com.city.ws.model.Student;
import com.city.ws.model.WsConst;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * 创建服务接口
 *
 * @author oKong
 */
@WebService(targetNamespace = WsConst.NAMESPACE_URI)
public interface IStudentService {

    @WebMethod
        //声明暴露服务的方法，可以不写
    List<Student> getStudentInfo();
}
