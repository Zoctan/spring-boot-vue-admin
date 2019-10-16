package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.dto.RoleWithPermission;
import com.zoctan.api.dto.RoleWithResource;
import com.zoctan.api.entity.Role;
import com.zoctan.api.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

/**
 * @author Zoctan
 * @date 2018/06/09
 */
@RestController
@RequestMapping("/role")
public class RoleController {
  @Resource private RoleService roleService;

  @PreAuthorize("hasAuthority('role:add')")
  @PostMapping
  public Result add(@RequestBody final RoleWithPermission role, final Principal principal) {
    this.roleService.save(role);
    return ResultGenerator.genOkResult();
  }

  @PreAuthorize("hasAuthority('role:delete')")
  @DeleteMapping("/{id}")
  public Result delete(@PathVariable final Long id, final Principal principal) {
    final Role dbRole = this.roleService.getById(id);
    if (dbRole == null) {
      return ResultGenerator.genFailedResult("角色不存在");
    }
    this.roleService.deleteById(id);
    return ResultGenerator.genOkResult();
  }

  @PreAuthorize("hasAuthority('role:update')")
  @PutMapping
  public Result update(@RequestBody final RoleWithPermission role, final Principal principal) {
    final Role dbRole = this.roleService.getById(role.getId());
    if (dbRole == null) {
      return ResultGenerator.genFailedResult("角色不存在");
    }
    this.roleService.update(role);
    return ResultGenerator.genOkResult();
  }

  @PreAuthorize("hasAuthority('role:list')")
  @GetMapping("/permission")
  public Result listWithPermission(
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "0") final Integer size) {
    PageHelper.startPage(page, size);
    final List<RoleWithResource> list = this.roleService.listRoleWithPermission();
    final PageInfo<RoleWithResource> pageInfo = new PageInfo<>(list);
    return ResultGenerator.genOkResult(pageInfo);
  }

  @GetMapping
  public Result list(
      @RequestParam(defaultValue = "0") final Integer page,
      @RequestParam(defaultValue = "0") final Integer size) {
    PageHelper.startPage(page, size);
    final List<Role> list = this.roleService.listAll();
    final PageInfo<Role> pageInfo = new PageInfo<>(list);
    return ResultGenerator.genOkResult(pageInfo);
  }
}
