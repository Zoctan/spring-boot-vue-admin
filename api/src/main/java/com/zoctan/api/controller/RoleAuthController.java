package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.model.RoleAuth;
import com.zoctan.api.service.RoleAuthService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Zoctan on 2018/02/17.
 */
@RestController
@RequestMapping("/role/auth")
public class RoleAuthController {
    @Resource
    private RoleAuthService roleAuthService;

    @PostMapping
    public Result add(@RequestBody final RoleAuth roleAuth) {
        this.roleAuthService.save(roleAuth);
        return ResultGenerator.genOkResult();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Integer id) {
        this.roleAuthService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @PutMapping
    public Result update(@RequestBody final RoleAuth roleAuth) {
        this.roleAuthService.update(roleAuth);
        return ResultGenerator.genOkResult();
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable final Integer id) {
        final RoleAuth roleAuth = this.roleAuthService.findById(id);
        return ResultGenerator.genOkResult(roleAuth);
    }

    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        PageHelper.startPage(page, size);
        final List<RoleAuth> list = this.roleAuthService.findAll();
        final PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
