package com.fun.moco.support;

import com.fun.utils.Time;
import com.github.dreamhead.moco.HttpRequest;
import com.github.dreamhead.moco.MocoConfig;
import com.github.dreamhead.moco.ResponseHandler;
import com.github.dreamhead.moco.handler.AbstractResponseHandler;
import com.github.dreamhead.moco.internal.SessionContext;
import com.google.common.base.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 循环的responsehandle
 */
@SuppressWarnings("all")
public class LimitHandler extends AbstractResponseHandler {


    private final ResponseHandler limit;

    private final ResponseHandler unlimit;

    private Map<String, Long> total = new ConcurrentHashMap<>();

    private int interval;

    private LimitHandler(final ResponseHandler limit, final ResponseHandler unLimit, int interval) {
        this.limit = limit;
        this.unlimit = unLimit;
        this.interval = interval;
    }

    public static ResponseHandler newSeq(final ResponseHandler limit, final ResponseHandler unLimit, int interval) {
        return new LimitHandler(limit, unLimit, interval);
    }

    /**
     * 返回响应
     *
     * @param context
     */
    @Override
    public void writeToResponse(final SessionContext context) {
        HttpRequest request = (HttpRequest) context.getRequest();
        String uri = request.getUri();
        (limited(uri ) ? limit : unlimit).writeToResponse(context);
    }

    @Override
    public ResponseHandler apply(final MocoConfig config) {
        if (config.isFor(MocoConfig.RESPONSE_ID)) {
            return super.apply(config);
        }
        return new LimitHandler(limit, unlimit, interval);
    }

    private Function<ResponseHandler, ResponseHandler> applyConfig(final MocoConfig config) {
        return new Function<ResponseHandler, ResponseHandler>() {
            @Override
            public ResponseHandler apply(final ResponseHandler input) {
                return input.apply(config);
            }
        };
    }

    /**
     * 判断是否被限制
     * <p>
     * 通过记录每一次响应的时间戳，判断两次请求间隔达到limit目的
     * </p>
     *
     * @param info
     * @return
     */
    public boolean limited(String info) {
        long fresh = Time.getTimeStamp();
        long old = total.containsKey(info) ? total.get(info) : 0L;
        total.put(info, fresh);
        return fresh - old > interval;
    }
}
