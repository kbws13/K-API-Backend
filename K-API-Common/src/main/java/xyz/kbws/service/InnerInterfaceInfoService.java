package xyz.kbws.service;

import xyz.kbws.model.entity.InterfaceInfo;

/**
* @author hsy
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-09 20:56:35
*/
public interface InnerInterfaceInfoService{

    /**
     * 校验接口是否存在
     * @param path 请求路径
     * @param method 请求方法
     * @return InterfaceInfo
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
