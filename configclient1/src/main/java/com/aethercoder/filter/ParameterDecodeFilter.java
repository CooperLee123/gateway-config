package com.aethercoder.filter;

import com.aethercoder.constants.CommonConstants;
import com.aethercoder.util.AESUtil;
import com.netflix.ribbon.proxy.annotation.Http;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * Created by hepengfei on 2018/5/10.
 */
@Component
public class ParameterDecodeFilter extends ZuulFilter{

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        ParameterRequestWrapper requestWrapper = new ParameterRequestWrapper(request);
        Map<String, List<String>> map = requestWrapper.handleRequestMap(request);
        ctx.setRequestQueryParams(map);
        return null;
    }
}
