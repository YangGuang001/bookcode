package org.smart4j.framework.bean;

import org.smart4j.framework.util.PropsUtil.CastUtil;

import java.util.Map;

/**
 * Created by yz on 2017/7/19.
 */
public class Param {
    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public long getLong(String name) {
        return CastUtil.castLong(paramMap.get(name));
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }
}
