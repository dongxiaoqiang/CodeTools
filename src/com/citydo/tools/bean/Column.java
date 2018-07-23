package com.citydo.tools.bean;

import java.util.HashMap;
import java.util.Map;

public class Column {
    private String name;
    private String javaName;
    private String type;
    private String comment;
    private boolean isIndex;
    private String javaType;
    private boolean isAutoIncr;
    private boolean isKey;
    private static Map<String, String> typeMap = new HashMap();

    static {
        typeMap.put("int", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("date", "Date");
        typeMap.put("datetime", "Timestamp");
        typeMap.put("timestamp", "Timestamp");
        typeMap.put("decimal", "BigDecimal");
        typeMap.put("double", "BigDecimal");
    }

    public Column() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        String[] split = name.split("_");
        String str = "";
        boolean first = true;
        String[] var8 = split;
        int var7 = split.length;

        for(int var6 = 0; var6 < var7; ++var6) {
            String tmp = var8[var6];
            tmp = tmp.toLowerCase();
            if (first) {
                first = false;
                str = str + tmp.substring(0, 1).toLowerCase();
            } else {
                str = str + tmp.substring(0, 1).toUpperCase();
            }

            str = str + tmp.substring(1);
        }

        this.setJavaName(str);
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
        if (typeMap.containsKey(type)) {
            this.setJavaType((String)typeMap.get(type));
        } else {
            this.setJavaType("String");
        }

    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isIndex() {
        return this.isIndex;
    }

    public void setIndex(boolean isIndex) {
        this.isIndex = isIndex;
    }

    public String getJavaType() {
        return this.javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaName() {
        return this.javaName;
    }

    public void setJavaName(String javaName) {
        this.javaName = javaName;
    }

    public boolean isAutoIncr() {
        return this.isAutoIncr;
    }

    public void setAutoIncr(boolean isAutoIncr) {
        this.isAutoIncr = isAutoIncr;
    }

    public boolean isKey() {
        return this.isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }
}
