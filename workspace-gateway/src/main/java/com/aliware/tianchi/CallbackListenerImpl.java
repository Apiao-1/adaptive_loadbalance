package com.aliware.tianchi;

import org.apache.dubbo.common.json.JSON;
import org.apache.dubbo.common.json.JSONObject;
import org.apache.dubbo.config.spring.status.DataSourceStatusChecker;
import org.apache.dubbo.rpc.RpcStatus;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.protocol.dubbo.status.ThreadPoolStatusChecker;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {

    private final Map<Integer, ConcurrentHashMap> serverStatusMap = new ConcurrentHashMap<>();

    @Override
    public void receiveServerMsg(String msg) {
//        ThreadPoolStatusChecker threadPoolStatusChecker = new ThreadPoolStatusChecker();
//        Runtime.getRuntime().availableProcessors();
//        RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).getActive()
//        DataSourceStatusChecker dataSourceStatusChecker = new DataSourceStatusChecker();
//        System.out.println(threadPoolStatusChecker.check().getMessage());
//        System.out.println("receive msg from server :" + msg + threadPoolStatusChecker.check() + "________________" + dataSourceStatusChecker.check().getMessage()
//        System.out.println("receive msg from server :" + msg);

        parseMessage(msg);
        printServerStatusMap();
    }

    private void parseMessage(String str) {
        String[] params = str.split(",");
        String[] param;
        System.out.println(params.length);
        ConcurrentHashMap<String, Integer> paramsMap = new ConcurrentHashMap<>();
        for (int i = 1; i < params.length; i++) {
            param = params[i].trim().split(":");
            if (i == params.length - 1) {
                serverStatusMap.put(Integer.valueOf(param[1].trim()), paramsMap);
                return;
            }
            paramsMap.put(param[0], Integer.valueOf(param[1]));
        }
    }

    private void printServerStatusMap() {
        if (serverStatusMap == null || serverStatusMap.isEmpty()) System.out.println("serverStatusMap is empty");
        for (Map.Entry<Integer, ConcurrentHashMap> entry : serverStatusMap.entrySet()) {
            System.out.println("key(port): " + entry.getKey() + " Value: " + entry.getValue());
        }
    }
}

