package com.aethercoder.filter;

import com.aethercoder.constants.CommonConstants;
import com.aethercoder.util.AESUtil;
import com.aethercoder.util.BeanUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

/**
 * Created by hepengfei on 2018/5/11.
 */
@Component
public class BodyEncodeFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 102;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
            InputStream in = ctx.getResponseDataStream();
            int responseCode = ctx.getResponseStatusCode();
            if (in != null && responseCode < 400) {

                String bodyStr = IOUtils.toString(in, CommonConstants.CHARACTER_ENCODE);
                String result = AESUtil.encrypt(bodyStr, CommonConstants.AES_KEY);
                Map<String, String> map = new HashMap<>();
                map.put("result", result);
                ctx.setResponseBody(BeanUtils.objectToJson(map));
            }
        } catch(IOException ioe) {
            throw new RuntimeException("error decrypt body");
        }

        return null;
    }
}
