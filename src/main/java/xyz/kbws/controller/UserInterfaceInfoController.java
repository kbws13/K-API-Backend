package xyz.kbws.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.kbws.annotation.AuthCheck;
import xyz.kbws.client.KApiClient;
import xyz.kbws.common.BaseResponse;
import xyz.kbws.common.DeleteRequest;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.common.ResultUtils;
import xyz.kbws.constant.UserConstant;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.exception.ThrowUtils;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import xyz.kbws.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import xyz.kbws.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import xyz.kbws.model.entity.User;
import xyz.kbws.model.entity.UserInterfaceInfo;
import xyz.kbws.service.ApiKeyService;
import xyz.kbws.service.UserInterfaceInfoService;
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
@RequestMapping("/userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    private UserService userService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private ApiKeyService apiKeyService;

    @Resource
    private KApiClient kApiClient;

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> addInterface(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        userInterfaceInfoService.save(userInterfaceInfo);
        return ResultUtils.success("添加成功");
    }

    @ApiOperation(value = "根据id查询")
    @GetMapping("/get/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getInterfaceById(@PathVariable("id") Long id) {
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该接口不存在");
        }
        return ResultUtils.success(userInterfaceInfo);
    }

    @ApiOperation(value = "更新")
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> updateInterface(@RequestBody InterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                                HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(userInterfaceInfoUpdateRequest.getId());
        BeanUtil.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 仅本人或管理员可修改
        User user = userService.getLoginUser(request);
        if (!userInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success("修改成功");
    }

    @ApiOperation(value = "查询")
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listInterface(@RequestBody UserInterfaceInfoQueryRequest queryRequest) {
        long current = queryRequest.getCurrent();
        long size = queryRequest.getPageSize();
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size),
                userInterfaceInfoService.getQueryWrapper(queryRequest));
        return ResultUtils.success(userInterfaceInfoPage);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    @AuthCheck
    public BaseResponse<String> deleteInterface(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(deleteRequest.getId());
        ThrowUtils.throwIf(userInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        userInterfaceInfoService.removeById(deleteRequest.getId());
        return ResultUtils.success("删除成功");
    }

}
