package com.zoctan.api.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zoctan.api.core.jwt.JwtUtil;
import com.zoctan.api.core.response.Result;
import com.zoctan.api.core.response.ResultGenerator;
import com.zoctan.api.model.User;
import com.zoctan.api.service.UserService;
import com.zoctan.api.service.impl.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(value = "用户接口")
@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserController(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation(value = "用户注册", notes = "根据传过来的user信息来注册用户")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping
    public Result register(@RequestBody @Valid final User user,
                           final BindingResult bindingResult) {
        // {"username":"123456", "password":"123456", "email": "123456@qq.com"}
        if (bindingResult.hasErrors()) {
            final String msg = bindingResult.getFieldError().getDefaultMessage();
            return ResultGenerator.genFailedResult(msg);
        } else {
            // 保存后密码加密了，而登录需要没加密的密码
            final String rawPassword = user.getPassword();
            this.userService.save(user);
            user.setPassword(rawPassword);
            return this.getToken(user);
        }
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {
        this.userService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "更新用户信息", notes = "根据传过来的user信息来更新用户详细信息")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PutMapping
    public Result update(@RequestBody final User user) {
        this.userService.update(user);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "获取Id用户信息")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(@PathVariable final Long id) {
        final User user = this.userService.findById(id);
        return ResultGenerator.genOkResult(user);
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/info")
    public Result info(@AuthenticationPrincipal final UserDetails userDetails) {
        final User user = this.userService.findDetailByUsername(userDetails.getUsername());
        return ResultGenerator.genOkResult(user);
    }

    @PreAuthorize("hasAuthority('user:list')")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页号", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "页数", dataType = "Integer")
    })
    @Cacheable(value = "userList")
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        log.info("no cache => find in database");
        PageHelper.startPage(page, size);
        final List<User> list;
        list = this.userService.findAllUserWithRole();
        final PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public Result login(@RequestBody @Valid final User user,
                        final BindingResult bindingResult) throws NullPointerException {
        // {"username":"admin", "password":"123456"}
        if (bindingResult.hasErrors()) {
            final String msg = bindingResult.getFieldError().getDefaultMessage();
            return ResultGenerator.genFailedResult(msg);
        } else {
            return this.getToken(user);
        }
    }

    /**
     * @AuthenticationPrincipal 该注解可以获得当前用户
     */
    @ApiOperation(value = "用户注销")
    @GetMapping("/logout")
    public Result logout(@AuthenticationPrincipal final UserDetails userDetails) {
        this.jwtUtil.invalidRedisStore(userDetails.getUsername());
        return ResultGenerator.genOkResult();
    }

    private Result getToken(final User user) {
        final String username = user.getUsername();
        final String password = user.getPassword();
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        if (this.userService.verifyPassword(password, userDetails.getPassword())) {
            // 更新登录时间
            this.userService.updateLoginTime(username);
            final String token = this.jwtUtil.sign(username, userDetails.getAuthorities());
            return ResultGenerator.genOkResult(token);
        } else {
            return ResultGenerator.genFailedResult("密码错误");
        }
    }
}
