package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {
    //    @Override
//    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//        try{
//            Result result = invoker.invoke(invocation);
//            return result;
//        }catch (Exception e){
//            throw e;
//        }
//
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        URL url = invoker.getUrl();
        String methodName = invocation.getMethodName();

        System.out.println(2 + "   " + methodName);
        int max = invoker.getUrl().getMethodParameter(methodName, Constants.ACTIVES_KEY, 0);
        RpcStatus count = RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName());
//        System.out.println(url +" " + invocation.getArguments() + " ")
        if (!count.beginCount(url, methodName, max)) {
            long timeout = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.TIMEOUT_KEY, 0);
            long start = System.currentTimeMillis();
            long remain = timeout;
            synchronized (count) {
                while (!count.beginCount(url, methodName, max)) {
                    try {
                        count.wait(remain);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    long elapsed = System.currentTimeMillis() - start;
                    remain = timeout - elapsed;
                    if (remain <= 0) {
                        throw new RpcException("Waiting concurrent invoke timeout in client-side for service:  "
                                + invoker.getInterface().getName() + ", method: "
                                + invocation.getMethodName() + ", elapsed: " + elapsed
                                + ", timeout: " + timeout + ". concurrent invokes: " + count.getActive()
                                + ". max concurrent invoke limit: " + max);
                    }
                }
            }
        }

        boolean isSuccess = true;
        long begin = System.currentTimeMillis();
        try {
            return invoker.invoke(invocation);
        } catch (RuntimeException t) {
            isSuccess = false;
            throw t;
        } finally {
            count.endCount(url, methodName, System.currentTimeMillis() - begin, isSuccess);
            System.out.println(3);
            if (max > 0) {
                synchronized (count) {
                    count.notifyAll();
                }
            }
        }
    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }
}
