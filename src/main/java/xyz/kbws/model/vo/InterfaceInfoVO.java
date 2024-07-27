package xyz.kbws.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.kbws.model.entity.InterfaceInfo;

/**
 * @author kbws
 * @date 2024/7/27
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoVO extends InterfaceInfo {
    /**
     * 调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;

}
