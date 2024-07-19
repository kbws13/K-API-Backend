package xyz.kbws.service.impl.inner;

import org.apache.dubbo.config.annotation.DubboService;
import xyz.kbws.service.InnerUserInterfaceInfoService;
import xyz.kbws.service.UserInterfaceInfoService;

import javax.annotation.Resource;

/**
 * @author kbws
 * @date 2024/7/19
 * @description:
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
