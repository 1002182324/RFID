package com.codequery.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestProxy {
    public static void main(String[] args) {

        Proxy1 proxy1 = new Proxy1();
        proxy1.set("小明");
        String s = proxy1.get();
        System.out.println(s);
        Iproxy o = (Iproxy) Proxy.newProxyInstance(Iproxy.class.getClassLoader(), proxy1.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("-----------------------");
                if (method.getName().equals("get")) {
                    String ss = "呵呵";
                    System.out.println(method.getName());
                    // Object obj = method.invoke(proxy1, ss);
                    return ss;
                }
                Object obj = method.invoke(proxy1, args);
                return obj;
            }

        });
        System.out.println(o.get());
    }
}
