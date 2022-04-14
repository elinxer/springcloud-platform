package com.elinxer.springcloud.platform.mqtt.broker.iotdevice.utils;

import com.elinxer.springcloud.platform.mqtt.broker.iotdevice.body.LoginoutProtoc;

/**
 * @description
 * @author elinx
 * @create 2021-10-09 10:32
 **/




public class ProtocMsgBuild {

    /**
     * 登入登出
     * @param code
     * @param msg
     * @param seq
     * @param businessType
     * @return
     */
    public static LoginoutProtoc.Loginout loginout(Integer status, Integer code, String msg , Integer seq, Integer businessType){
        LoginoutProtoc.Loginout.Builder loginoutBuild = LoginoutProtoc.Loginout.newBuilder();
        loginoutBuild.setBusinessType(businessType);
        loginoutBuild.setVersion(1);
        LoginoutProtoc.Loginout.LoginoutResponse.Builder responseBuilid = LoginoutProtoc.Loginout.LoginoutResponse.newBuilder();
        responseBuilid.setSequenceId(seq);
        responseBuilid.setLoginoutResult(LoginoutProtoc.Loginout.CommonResult.newBuilder().setStatus(status).setCode(code).setMsg(msg).build());
        loginoutBuild.setLoginoutResponse(responseBuilid);
        return loginoutBuild.build();
    }
}