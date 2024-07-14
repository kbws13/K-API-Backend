package xyz.kbws.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.constant.CommonConstant;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import xyz.kbws.model.entity.InterfaceInfo;
import xyz.kbws.model.entity.User;
import xyz.kbws.service.InterfaceInfoService;
import xyz.kbws.mapper.InterfaceInfoMapper;
import org.springframework.stereotype.Service;
import xyz.kbws.utils.SqlUtils;

/**
* @author hsy
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-07-09 20:56:35
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Override
    public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest queryRequest) {
        if (queryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = queryRequest.getId();
        String name = queryRequest.getName();
        String description = queryRequest.getDescription();
        String url = queryRequest.getUrl();
        String requestParams = queryRequest.getRequestParams();
        String requestHeader = queryRequest.getRequestHeader();
        String responseHeader = queryRequest.getResponseHeader();
        Integer status = queryRequest.getStatus();
        String method = queryRequest.getMethod();
        Long userId = queryRequest.getUserId();
        String sortField = queryRequest.getSortField();
        String sortOrder = queryRequest.getSortOrder();
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        queryWrapper.like(StrUtil.isNotBlank(description), "description", description);
        queryWrapper.like(StrUtil.isNotBlank(url), "url", url);
        queryWrapper.like(StrUtil.isNotBlank(requestParams), "requestParams", requestParams);
        queryWrapper.like(StrUtil.isNotBlank(requestHeader), "requestHeader", requestHeader);
        queryWrapper.like(StrUtil.isNotBlank(responseHeader), "responseHeader", responseHeader);
        queryWrapper.like(StrUtil.isNotBlank(method), "method", method);
        queryWrapper.eq(status != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




