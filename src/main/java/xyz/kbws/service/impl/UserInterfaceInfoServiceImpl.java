package xyz.kbws.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.constant.CommonConstant;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.mapper.UserInterfaceInfoMapper;
import xyz.kbws.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import xyz.kbws.model.entity.UserInterfaceInfo;
import xyz.kbws.service.UserInterfaceInfoService;
import xyz.kbws.utils.SqlUtils;

/**
* @author hsy
* @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
* @createDate 2024-07-14 17:08:16
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        // 判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        // updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum=leftNum-1, totalNum=totalNum+1");
        return this.update(updateWrapper);
    }

    @Override
    public QueryWrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = queryRequest.getId();
        Long userId = queryRequest.getUserId();
        Long interfaceId = queryRequest.getInterfaceId();
        Integer totalNum = queryRequest.getTotalNum();
        Integer leftNum = queryRequest.getLeftNum();
        Integer status = queryRequest.getStatus();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();

        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(interfaceId != null, "interfaceId", interfaceId);
        queryWrapper.eq(totalNum != null, "totalNum", totalNum);
        queryWrapper.eq(leftNum != null, "leftNum", leftNum);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




