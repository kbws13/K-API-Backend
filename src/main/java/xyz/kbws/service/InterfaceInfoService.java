package xyz.kbws.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import xyz.kbws.model.dto.user.UserQueryRequest;
import xyz.kbws.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.kbws.model.entity.User;

/**
* @author hsy
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-07-09 20:56:35
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest queryRequest);

}
