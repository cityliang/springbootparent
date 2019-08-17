package com.city.sprinbboot.database2code.com.xjs.common.activemqMessage;


import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import com.city.sprinbboot.database2code.com.farsunset.cim.sdk.server.model.Message;

@Service
public class ActivemqUtilService {
    @Resource(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;


    /**
     * 向默认队列发送消息
     */
    public void sendMessage(final Message bean) {
        //判断是否启动了mq，如果没有启动，就不推送，防止报错
        try {
            ConnectionFactory factory = jmsTemplate.getConnectionFactory();
            Connection conn = factory.createConnection();
            if (conn == null) {
                return;
            }
        } catch (JMSException e) {
            return;
        }
        jmsTemplate.send(new MessageCreator() {
            @Override
            public javax.jms.Message createMessage(Session session) throws JMSException {
                ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
                msg.setObject((Serializable) bean);
                return msg;
            }
        });

    }
}
