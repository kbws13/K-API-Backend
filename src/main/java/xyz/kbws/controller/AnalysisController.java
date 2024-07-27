package xyz.kbws.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.kbws.annotation.AuthCheck;
import xyz.kbws.common.BaseResponse;
import xyz.kbws.common.ErrorCode;
import xyz.kbws.common.ResultUtils;
import xyz.kbws.constant.UserConstant;
import xyz.kbws.exception.BusinessException;
import xyz.kbws.mapper.UserInterfaceInfoMapper;
import xyz.kbws.model.entity.InterfaceInfo;
import xyz.kbws.model.entity.UserInterfaceInfo;
import xyz.kbws.model.vo.InterfaceInfoVO;
import xyz.kbws.service.InterfaceInfoService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author kbws
 * @date 2024/7/27
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = userInterfaceInfoList.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);

    }
}
