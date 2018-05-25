package com.aethercoder.filter;

import com.aethercoder.constants.CommonConstants;
import com.aethercoder.util.AESUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * Created by hepengfei on 2018/5/11.
 */
@Component
public class BodyDecodeFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return PRE_TYPE;
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
        final byte[] reqBodyBytes;
        try {
            InputStream in = (InputStream) ctx.get("requestEntity");
            if (in == null) {
                in = ctx.getRequest().getInputStream();
            }


            String bodyStr = IOUtils.toString(in, CommonConstants.CHARACTER_ENCODE);
            String result = AESUtil.decrypt(bodyStr, CommonConstants.AES_KEY);
            reqBodyBytes = result.getBytes();
        } catch(IOException ioe) {
            throw new RuntimeException("error decrypt body");
        }

        ctx.setRequest(new HttpServletRequestWrapper(ctx.getRequest()) {
            @Override
            public ServletInputStream getInputStream() throws IOException {
                return new ServletInputStreamWrapper(reqBodyBytes);
            }

            @Override
            public int getContentLength() {
                return reqBodyBytes.length;
            }

            @Override
            public long getContentLengthLong() {
                return reqBodyBytes.length;
            }
        });

        return null;
    }
}
