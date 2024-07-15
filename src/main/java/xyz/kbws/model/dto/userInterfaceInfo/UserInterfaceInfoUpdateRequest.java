package xyz.kbws.model.dto.userInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kbws
 * @date 2024/7/9
 * @description:
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {

    private static final long serialVersionUID = -9097343705072808777L;

    private Long id;

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


}
