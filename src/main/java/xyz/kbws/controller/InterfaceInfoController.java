package xyz.kbws.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.kbws.annotation.AuthCheck;
import xyz.kbws.common.BaseResponse;
import xyz.kbws.common.DeleteRequest;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.common.ResultUtils;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.exception.ThrowUtils;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import xyz.kbws.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import xyz.kbws.model.entity.InterfaceInfo;
import xyz.kbws.service.InterfaceInfoService;

import javax.annotation.Resource;

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
    private InterfaceInfoService interfaceInfoService;

    @ApiOperation(value = "添加")
    @PostMapping("/add")
    @AuthCheck
    public BaseResponse<String> addInterface(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        interfaceInfoService.save(interfaceInfo);
        return ResultUtils.success("添加成功");
    }

    @ApiOperation(value = "更新")
    @PostMapping("/update")
    @AuthCheck
    public BaseResponse<String> updateInterface(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoUpdateRequest.getId());
        BeanUtil.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
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
}
