package com.furioussoulk.apm.collector.core.module;

public class BaseModuleA extends Module {
    @Override public String name() {
        return "BaseA";
    }

    @Override public Class<? extends Service>[] services() {
        return new Class[] {ServiceABusiness1.class, ServiceABusiness2.class};
    }

    public interface ServiceABusiness1 extends Service {
        void print();
    }

    public interface ServiceABusiness2 extends Service {

    }
}
