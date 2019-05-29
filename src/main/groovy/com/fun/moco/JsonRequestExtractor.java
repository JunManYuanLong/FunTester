package com.fun.moco;

import com.github.dreamhead.moco.HttpRequest;
import com.github.dreamhead.moco.HttpRequestExtractor;
import com.github.dreamhead.moco.RequestExtractor;
import com.google.common.base.Optional;
import net.sf.json.JSONObject;

import static com.github.dreamhead.moco.util.Preconditions.checkNotNullOrEmpty;
import static com.google.common.base.Optional.fromNullable;

/**
 * json数据格式参数值的获取
 */
public class JsonRequestExtractor extends HttpRequestExtractor<String[]> {

    private final String param;

    public JsonRequestExtractor(final String param) {
        this.param = param;
    }

    @Override
    protected Optional<String[]> doExtract(HttpRequest request) {
        try {
            String s = request.getContent().toString();
            String value = JSONObject.fromObject(s).getString(param);
            return fromNullable(new String[]{value});
        } catch (Exception e) {
            return fromNullable(new String[]{""});
        }
    }

    /**
     * 获取参数的value
     *
     * @param param
     * @return
     */
    public static RequestExtractor<String[]> queryJson(final String param) {
        return new JsonRequestExtractor(checkNotNullOrEmpty(param, "参数不能为空！"));
    }
}
