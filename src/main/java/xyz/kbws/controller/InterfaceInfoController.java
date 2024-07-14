package xyz.kbws.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.kbws.annotation.AuthCheck;
import xyz.kbws.client.KApiClient;
import xyz.kbws.common.*;
import xyz.kbws.constant.UserConstant;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.exception.ThrowUtils;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import xyz.kbws.model.entity.InterfaceInfo;
import xyz.kbws.model.entity.User;
import xyz.kbws.model.enums.InterfaceInfoStatusEnum;
import xyz.kbws.service.InterfaceInfoService;
import xyz.kbws.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author kbws
 * @date 2024/7/9
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/interfaceInfo")
public class InterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private KApiClient kApiClient;

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    @AuthCheck
    public BaseResponse<String> addInterface(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        interfaceInfoService.save(interfaceInfo);
        return ResultUtils.success("添加成功");
    }

    @ApiOperation(value = "更新")
    @PostMapping("/update")
    @AuthCheck
    public BaseResponse<String> updateInterface(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest,
                                                HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoUpdateRequest.getId());
        BeanUtil.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 仅本人或管理员可修改
        User user = userService.getLoginUser(request);
        if (!interfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success("修改成功");
    }

    @ApiOperation(value = "查询")
    @PostMapping("/list")
    public BaseResponse<Page<InterfaceInfo>> listInterface(@RequestBody InterfaceInfoQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size),
                interfaceInfoService.getQueryWrapper(queryRequest));
        return ResultUtils.success(interfaceInfoPage);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    @AuthCheck
    public BaseResponse<String> deleteInterface(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        interfaceInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success("删除成功");
    }

    @ApiOperation(value = "发布接口")
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onlineInterface(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断接口是否可以调用
        xyz.kbws.model.User user = new xyz.kbws.model.User();
        user.setUserName("kbws");
        String userName = kApiClient.getUserNameByPost(user);
        if (StrUtil.isBlank(userName)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(interfaceInfo.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "下线接口")
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> offlineInterface(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(idRequest.getId());
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(interfaceInfo.getId());
        interfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }
}
