package xyz.kbws.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kbws.model.entity.InterfaceInfo;

/**
* @author hsy
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-09 20:56:35
*/
public interface InnerInterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验接口是否存在
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
