package xyz.kbws.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kbws
 * @date 2024/7/19
 * @description:
 */
@Data
public class UserKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    /**
     * 用户账号
     */
    private String userAccount;
    /**
     * 用户密码
     */
    private String userPassword;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;
    /**
     * 用户简介
     */
    private String userProfile;
    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;
}
