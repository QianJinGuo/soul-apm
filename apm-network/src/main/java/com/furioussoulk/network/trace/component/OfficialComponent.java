package com.furioussoulk.network.trace.component;

public class OfficialComponent implements Component {
    private int id;
    private String name;

    public OfficialComponent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
