package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;
import org.apache.dubbo.rpc.protocol.dubbo.status.ThreadPoolStatusChecker;


import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

    private ThreadPoolStatusChecker threadPoolStatusChecker = new ThreadPoolStatusChecker();

    public CallbackServiceImpl() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
//                            entry.getValue().receiveServerMsg(System.getProperty("quota") + " " + new Date().toString());
                            entry.getValue().receiveServerMsg(threadPoolStatusChecker.check().getMessage());
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
//                            t1.printStackTrace();
                        }
                    }
                }
            }
        }, 0, 1000);
    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
        listeners.put(key, listener);
        listener.receiveServerMsg(new Date().toString()); // send notification for change
    }
}
