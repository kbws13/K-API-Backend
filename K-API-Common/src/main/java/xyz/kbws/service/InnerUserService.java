package xyz.kbws.service;

import xyz.kbws.model.vo.UserKey;


/**
 * 用户服务
 */
public interface InnerUserService{

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey
     * @return
     */
    UserKey getInvokeUser(String accessKey);

}
