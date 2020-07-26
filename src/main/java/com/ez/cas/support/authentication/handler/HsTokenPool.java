package com.ez.cas.support.authentication.handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jiasheng.api.utils.JiashengUtil;
import com.jiasheng.api.utils.SsoUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

public class HsTokenPool {
    private Cache<String, HttpServletRequest> tokenPool;

    public HsTokenPool(){
        tokenPool = CacheBuilder.newBuilder()
                .expireAfterAccess(3600, TimeUnit.SECONDS)
                .expireAfterWrite(3600,TimeUnit.SECONDS).build();
    }

    public void put(String userId,HttpServletRequest request){
        this.tokenPool.put(userId,request);
    }

    public String token(String userId){
        HttpServletRequest request = tokenPool.getIfPresent(userId);
        return SsoUtil.getAccessTokenInSession(request);
    }
    public String tenantId(String userId){
        HttpServletRequest request = tokenPool.getIfPresent(userId);
        return SsoUtil.getTenantId(request);
    }
}
