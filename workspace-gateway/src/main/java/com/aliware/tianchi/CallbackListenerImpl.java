package com.aliware.tianchi;

import org.apache.dubbo.config.spring.status.DataSourceStatusChecker;
import org.apache.dubbo.rpc.RpcStatus;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.protocol.dubbo.status.ThreadPoolStatusChecker;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 *
 */
public class CallbackListenerImpl implements CallbackListener {

    @Override
    public void receiveServerMsg(String msg) {
//        ThreadPoolStatusChecker threadPoolStatusChecker = new ThreadPoolStatusChecker();
//        Runtime.getRuntime().availableProcessors();
//        RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).getActive()
//        DataSourceStatusChecker dataSourceStatusChecker = new DataSourceStatusChecker();
//        System.out.println(threadPoolStatusChecker.check().getMessage());
//        System.out.println("receive msg from server :" + msg + threadPoolStatusChecker.check() + "________________" + dataSourceStatusChecker.check().getMessage()
        System.out.println("receive msg from server :" + msg);
    }

}
