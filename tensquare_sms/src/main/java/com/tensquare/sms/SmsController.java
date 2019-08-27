package com.tensquare.sms;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsController {
    @Value("${aliyun.sms.template_code}")
    private String template_code;
    @Value("${aliyun.sms.sign_name}")
    private String sign_name;
    @Autowired
    private SmsUtil smsUtil;
    @RabbitHandler
    public void sendsms(Map map) throws ClientException {
        System.out.println("手机号："+map.get("mobile"));
        System.out.println("验证码："+map.get("code"));
        //{"code",111}
        String mobile=(String)map.get("mobile");
        //smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+map.get("code")+"\"}");
    }
}
