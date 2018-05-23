package com.aethercoder.filter;

import com.aethercoder.constants.CommonConstants;
import com.aethercoder.util.BCryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * Created by hepengfei on 2018/5/11.
 */
@Component
@RefreshScope
public class SignFilter extends ZuulFilter {
    private String appHeader = CommonConstants.HEADER_APP_TOKEN_KEY;

    private String tokenHeader = CommonConstants.HEADER_WEB_TOKEN_KEY;

    private String timestampHeader = CommonConstants.HEADER_TIMESTAMP_KEY;

    private String signHeader = CommonConstants.HEADER_SIGN_KEY;

    private static Logger logger = LoggerFactory.getLogger(SignFilter.class);

//    @Value("${no_sign_path}")
//    private List<String> noSignApi;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 101;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        PathMatcher matcher = new AntPathMatcher();
//        for (String path : noSignApi) {
//            if (matcher.match(path, request.getRequestURI())) {
//                return false;
//            }
//        }
        if (matcher.match("/adminAccount/login", request.getRequestURI())) {
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        Long timestamp = Long.valueOf(request.getHeader(timestampHeader));
        Date apiDate = new Date(timestamp);
        Calendar calMin = Calendar.getInstance();
        Calendar calMax = Calendar.getInstance();
        calMin.add(Calendar.HOUR, -25);
        calMax.add(Calendar.HOUR, 25);

        if (apiDate.before(calMin.getTime()) || apiDate.after(calMax.getTime())) {
            throw new RuntimeException("illegal call");
        }

        String sign = request.getHeader(signHeader);
        String token = request.getHeader(tokenHeader);
        String randNum = request.getHeader(CommonConstants.HEADER_SIGN_RANDOM);
        if (token == null) {
            token = request.getHeader(appHeader);
        }
        if (token == null) {
            token = "";
        }
        String url = request.getRequestURL().toString();
        Map<String, List<String>> queryParams = ctx.getRequestQueryParams();
        Set<String> keys = queryParams.keySet();
        List<String> paraList = new ArrayList<>();
        for (String key : keys) {
            List<String> paraValues = queryParams.get(key);
            for (String paraValue : paraValues) {
                paraList.add(key + "=" + paraValue);
            }
        }

        String queryString = StringUtils.collectionToDelimitedString(paraList, "&");

        if (!queryString.equals("")) {
            queryString = "?" + queryString;
        }
        String plainSign ;
        if(request.getRequestURI().contains("/order/")) {
            plainSign = timestamp + CommonConstants.API_SIGN_SALT2_ORDER + url + queryString + CommonConstants.API_SIGN_SALT_ORDER + randNum + token;
        }else {
            plainSign = timestamp + CommonConstants.API_SIGN_SALT2 + url + queryString + CommonConstants.API_SIGN_SALT + randNum + token;
        }
        if (!BCryptUtil.checkMatch(plainSign, sign)) {
            logger.error("plainSign: " + plainSign + "==========md5Sign: md5Sign" + "==========md5parameter: " + sign);
            throw new RuntimeException("illegal call");
        }
//        Object redisSign = redisTemplate.opsForValue().get(CommonConstants.REDIS_KEY_SIGN + sign);
//        if (redisSign != null) {
//            throw new RuntimeException("duplicate call");
//        }
//        redisTemplate.opsForValue().set(CommonConstants.REDIS_KEY_SIGN + sign, sign);
//        redisTemplate.expire(CommonConstants.REDIS_KEY_SIGN + sign, 1, TimeUnit.HOURS);
        return null;
    }
}
