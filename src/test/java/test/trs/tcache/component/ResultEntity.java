package test.trs.tcache.component;

import com.trs.common.context.CommonContext;
import com.trs.common.utils.JsonUtils;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2018/10/22 14:08
 */
@Data
public class ResultEntity {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap();

    public String toJson() {
        return JsonUtils.toOptionalJson(map).get();
    }


}
