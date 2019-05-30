package com.fun.moco;


import com.fun.frame.SourceCode;
import com.github.dreamhead.moco.MocoConfig;
import com.github.dreamhead.moco.ResponseHandler;
import com.github.dreamhead.moco.handler.AbstractResponseHandler;
import com.github.dreamhead.moco.internal.SessionContext;
import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.ImmutableList.copyOf;


/**
 * 随机的responsehandle
 */
public class RandomHandler extends AbstractResponseHandler {
    private final ImmutableList<ResponseHandler> handlers;

    private RandomHandler(final Iterable<ResponseHandler> handlers) {
        this.handlers = copyOf(handlers);
    }

    @Override
    public void writeToResponse(final SessionContext context) {
        handlers.get(SourceCode.getRandomInt(handlers.size()-1)).writeToResponse(context);
    }

    @Override
    public ResponseHandler apply(final MocoConfig config) {
        if (config.isFor(MocoConfig.RESPONSE_ID)) {
            return super.apply(config);
        }

        FluentIterable<ResponseHandler> transformedResources = from(copyOf(handlers)).transform(applyConfig(config));
        return new RandomHandler(transformedResources.toList());
    }

    private Function<ResponseHandler, ResponseHandler> applyConfig(final MocoConfig config) {
        return new Function<ResponseHandler, ResponseHandler>() {
            @Override
            public ResponseHandler apply(final ResponseHandler input) {
                return input.apply(config);
            }
        };
    }
}
