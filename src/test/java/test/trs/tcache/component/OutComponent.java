package test.trs.tcache.component;

import com.trs.web.restful.RestfulJsonHelper;
import org.mockito.Mockito;
import test.trs.tcache.component.VO.UserVO;

import java.time.LocalDateTime;
import java.util.*;

/**
 *
 * @description:
 * @author: liu.zhengwei
 * @create: 2018/10/22 14:01
 */
public class OutComponent{

    public Map<String, Object> getValue(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put("value",key);
        map.put("this",key+"_value");
        return map;
    }

    public RestfulJsonHelper getResult(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put("value",key);
        map.put("this",key+"_value");
        RestfulJsonHelper restfulJsonHelper = RestfulJsonHelper.BaseOn(RestfulJsonHelper.STATUS_CODE.OK);
        restfulJsonHelper.addPageNum(1).addPageSize(1).addData(map);
        System.out.println(restfulJsonHelper.toJson());
        return restfulJsonHelper;
    }

    public UserVO getUser() {
        UserVO userVO = Mockito.mock(UserVO.class);
        return userVO;
    }

    public List<UserVO> getList(String... name) {
        List<UserVO> userVOList = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            UserVO userVO = new UserVO();
            userVO.setId(new Random(10).nextLong());
            userVO.setCreateTime(LocalDateTime.now());
            userVO.setEmail(name[i] + "@test.com");
            userVO.setLoginTime(LocalDateTime.now());
            userVO.setNickName(name[i]);
            userVO.setUsername(name[i]);
            userVO.setStatus(1);
            userVO.setPassword("12312321");
            userVOList.add(userVO);
        }
        return userVOList;
    }

}
