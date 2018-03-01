package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.model.RolePermission;
import com.zoctan.api.service.RolePermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/17.
 */
@RestController
@RequestMapping("/role/permission")
public class RolePermissionController {
    @Resource
    private RolePermissionService rolePermissionService;

    @PostMapping
    public Result add(@RequestBody final RolePermission rolePermission) {
        this.rolePermissionService.save(rolePermission);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Integer id) {
        this.rolePermissionService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PutMapping
    public Result update(@RequestBody final RolePermission rolePermission) {
        this.rolePermissionService.update(rolePermission);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable final Integer id) {
        final RolePermission rolePermission = this.rolePermissionService.findById(id);
        return ResultGenerator.genOkResult(rolePermission);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        PageHelper.startPage(page, size);
        final List<RolePermission> list = this.rolePermissionService.findAll();
        final PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
