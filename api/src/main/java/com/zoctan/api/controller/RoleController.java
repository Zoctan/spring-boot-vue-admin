package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.model.Role;
import com.zoctan.api.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @PreAuthorize("hasAuthority('role:add')")
    @PostMapping
    public Result add(@RequestBody final Role role) {
        this.roleService.save(role);
        return ResultGenerator.genOkResult();
    }

    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {
        this.roleService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping
    public Result update(@RequestBody final Role role) {
        this.roleService.update(role);
        return ResultGenerator.genOkResult();
    }

    @PreAuthorize("hasAuthority('role:list')")
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        PageHelper.startPage(page, size);
        final List<Role> list = this.roleService.findAllRoleWithPermission();
        final PageInfo<Role> pageInfo = new PageInfo<>(list);
        pageInfo.setTotal(list.size());
        return ResultGenerator.genOkResult(pageInfo);
    }
}
