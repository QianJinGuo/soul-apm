package com.furioussoulk.apm.collector.core.module;

public class BaseModuleB extends Module {
    @Override public String name() {
        return "BaseB";
    }

    @Override public Class<? extends Service>[] services() {
        return new Class[] {BaseModuleB.ServiceBBusiness1.class, BaseModuleB.ServiceBBusiness2.class};
    }

    public interface ServiceBBusiness1 extends Service {

    }

    public interface ServiceBBusiness2 extends Service {

    }
}
