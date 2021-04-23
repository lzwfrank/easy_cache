package test.trs.tcache.component.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: 测试用用户相关信息
 * @author: liu.zhengwei
 * @create: 2020/12/07 19:32
 */
@Data
public class UserVO {

    private Long id;

    private String username;

    private String password;

    private String icon;

    private String email;

    private String nickName;

    private String note;

    private LocalDateTime createTime;

    private LocalDateTime loginTime;

    private Integer status;

    public UserVO() {
    }

    public UserVO(Long id, String username, String password, String icon, String email, String nickName, String note, LocalDateTime createTime, LocalDateTime loginTime, Integer status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.icon = icon;
        this.email = email;
        this.nickName = nickName;
        this.note = note;
        this.createTime = createTime;
        this.loginTime = loginTime;
        this.status = status;
    }
}
