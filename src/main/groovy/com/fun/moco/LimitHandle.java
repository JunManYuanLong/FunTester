package com.fun.moco;

import com.fun.utils.Time;
import com.github.dreamhead.moco.HttpRequest;
import com.github.dreamhead.moco.MocoConfig;
import com.github.dreamhead.moco.ResponseHandler;
import com.github.dreamhead.moco.handler.AbstractResponseHandler;
import com.github.dreamhead.moco.internal.SessionContext;
import com.github.dreamhead.moco.model.MessageContent;
import com.google.common.base.Function;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 循环的responsehandle
 */
@SuppressWarnings("all")
public class LimitHandle extends AbstractResponseHandler {


    private final ResponseHandler limit;

    private final ResponseHandler unlimit;

    private Map<String, Long> tatal = new ConcurrentHashMap<>();

    private int interval;

    private LimitHandle(final ResponseHandler limit, final ResponseHandler unLimit) {
        this.limit = limit;
        this.unlimit = unLimit;
    }

    public static ResponseHandler newSeq(final ResponseHandler limit, final ResponseHandler unLimit) {
        return new LimitHandle(limit,unLimit);
    }

    @Override
    public void writeToResponse(final SessionContext context) {
        HttpRequest request = (HttpRequest) context.getRequest();
        String uri = request.getUri();
        MessageContent content = request.getContent();
        (limited(uri + content) ? limit : unlimit).writeToResponse(context);
    }

    @Override
    public ResponseHandler apply(final MocoConfig config) {
        if (config.isFor(MocoConfig.RESPONSE_ID)) {
            return super.apply(config);
        }
        return new LimitHandle(limit,unlimit);
    }

    private Function<ResponseHandler, ResponseHandler> applyConfig(final MocoConfig config) {
        return new Function<ResponseHandler, ResponseHandler>() {
            @Override
            public ResponseHandler apply(final ResponseHandler input) {
                return input.apply(config);
            }
        };
    }

    public boolean limited(String info) {
        long fresh = Time.getTimeStamp();
        Long old = tatal.containsKey(info) ? tatal.get(info) : 0L;
        tatal.put(info, fresh);
        return fresh - old > interval;
    }
}
