package xyz.kbws.model.dto.userInterfaceInfo;

import lombok.Data;
import xyz.kbws.common.PageRequest;

import java.io.Serializable;

/**
 * @author kbws
 * @date 2024/7/9
 * @description:
 */
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    private Long id;

    /**
     * 调用用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常， 1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = -6283969200443207481L;
}
