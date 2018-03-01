package com.zoctan.api.controller;

import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.model.User;
import com.zoctan.api.service.UserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Zoctan on 2018/02/17.
 */
@Api(value = "用户角色接口")
@RestController
@RequestMapping("/user/role")
public class UserRoleController {
    @Resource
    private UserRoleService userRoleService;

    @PreAuthorize("hasAuthority('role:update')")
    @ApiOperation(value = "更新用户角色")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PutMapping
    public Result updateUserRole(@RequestBody final User user) {
        this.userRoleService.updateUserRole(user);
        return ResultGenerator.genOkResult();
    }
}
