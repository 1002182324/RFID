package com.codequery.proxy;

public class Proxy1 implements Iproxy {
    String persion;

    @Override
    public void set(String name) {
        persion = name;
    }

    @Override
    public String get() {

        return this.persion;
    }
}
