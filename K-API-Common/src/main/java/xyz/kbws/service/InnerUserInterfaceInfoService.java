package xyz.kbws.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kbws.model.entity.UserInterfaceInfo;

/**
* @author hsy
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
* @createDate 2024-07-14 17:08:16
*/
public interface InnerUserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
