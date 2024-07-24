package xyz.kbws.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.mapper.UserInterfaceInfoMapper;
import xyz.kbws.model.entity.UserInterfaceInfo;
import xyz.kbws.service.InnerUserInterfaceInfoService;

import javax.annotation.Resource;

/**
 * @author kbws
 * @date 2024/7/19
 * @description:
 */
@Slf4j
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        log.info("增加调用次数");
        // 判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        // updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum=leftNum-1, totalNum=totalNum+1");
        return userInterfaceInfoMapper.update(null, updateWrapper) != 0;
    }
}
