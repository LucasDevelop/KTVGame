package com.lucas.frame.adapter.entity;

import java.io.Serializable;

public abstract class SectionEntity<T> implements Serializable {
    public boolean isHeader;
    public T t;
    public String header;
    public int count;
    public  int type;

    public SectionEntity(boolean isHeader, String header,int count,int type) {
        this.isHeader = isHeader;
        this.header = header;
        this.count = count;
        this.type = type;
        this.t = null;
    }

    public SectionEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.count = 0;
        this.type = 1;
        this.t = t;
    }
}
