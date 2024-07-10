package xyz.kbws.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kbws
 * @date 2024/7/9
 * @description:
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态：0-关闭，1-开启
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    private static final long serialVersionUID = -1839048740417273147L;
}
