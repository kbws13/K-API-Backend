package xyz.kbws.service.impl.inner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.dubbo.config.annotation.DubboService;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.mapper.ApiKeyMapper;
import xyz.kbws.mapper.UserMapper;
import xyz.kbws.model.entity.ApiKey;
import xyz.kbws.model.entity.User;
import xyz.kbws.model.vo.UserKey;
import xyz.kbws.service.InnerUserService;

import javax.annotation.Resource;

/**
 * @author kbws
 * @date 2024/7/19
 * @description:
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private ApiKeyMapper apiKeyMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public UserKey getInvokeUser(String accessKey) {
        if (StrUtil.isAllBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<ApiKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        queryWrapper.eq("isAbandon", 0);
        ApiKey apiKey = apiKeyMapper.selectOne(queryWrapper);
        if (apiKey == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userMapper.selectById(apiKey.getUserId());
        UserKey userKey = new UserKey();
        BeanUtil.copyProperties(user, userKey);
        userKey.setAccessKey(apiKey.getAccessKey());
        userKey.setSecretKey(apiKey.getSecretKey());
        return userKey;
    }
}
