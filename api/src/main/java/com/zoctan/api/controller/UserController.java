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
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value = "用户注册")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping
    public Result register(@RequestBody @Valid final User user,
                           final BindingResult bindingResult) {
        // {"username":"123456", "password":"123456", "email": "123456@qq.com"}
        if (bindingResult.hasErrors()) {
            final String msg = bindingResult.getFieldError().getDefaultMessage();
            return ResultGenerator.genFailedResult(msg);
        } else {
            this.userService.save(user);
            return this.getToken(user);
        }
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @ApiOperation(value = "根据Id删除用户")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable final Long id) {
        this.userService.deleteById(id);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "验证用户密码")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping("/password")
    public Result validatePassword(@RequestBody final User user) {
        final User oldUser = this.userService.findById(user.getId());
        final boolean isValidate = this.userService.verifyPassword(user.getPassword(), oldUser.getPassword());
        return ResultGenerator.genOkResult(isValidate);
    }

    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PutMapping
    public Result update(@RequestBody final User user) {
        this.userService.update(user);
        return this.getToken(this.userService.findById(user.getId()));
    }

    @ApiOperation(value = "根据Id获取用户信息")
    @ApiImplicitParam(name = "id", value = "用户Id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public Result detail(@PathVariable final Long id) {
        final User user = this.userService.findById(id);
        return ResultGenerator.genOkResult(user);
    }

    /**
     * AuthenticationPrincipal 注解可以获得当前用户
     */
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
    @GetMapping
    public Result list(@RequestParam(defaultValue = "0") final Integer page,
                       @RequestParam(defaultValue = "0") final Integer size) {
        PageHelper.startPage(page, size);
        final List<User> list = this.userService.findAllUserWithRole();
        //noinspection unchecked
        final PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genOkResult(pageInfo);
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    @PostMapping("/login")
    public Result login(@RequestBody final User user) {
        // {"username":"admin", "password":"admin123"}
        // {"email":"admin@qq.com", "password":"admin123"}
        if (user.getUsername() == null && user.getEmail() == null) {
            return ResultGenerator.genFailedResult("username or email empty");
        }
        if (user.getPassword() == null) {
            return ResultGenerator.genFailedResult("password empty");
        }
        // 用户名登录
        User dbUser = null;
        if (user.getUsername() != null) {
            dbUser = this.userService.findBy("username", user.getUsername());
            if (dbUser == null) {
                return ResultGenerator.genFailedResult("username error");
            }
        }
        // 邮箱登录
        if (user.getEmail() != null) {
            dbUser = this.userService.findBy("email", user.getEmail());
            if (dbUser == null) {
                return ResultGenerator.genFailedResult("email error");
            }
            user.setUsername(dbUser.getUsername());
        }
        // 验证密码
        //noinspection ConstantConditions
        if (!this.userService.verifyPassword(user.getPassword(), dbUser.getPassword())) {
            return ResultGenerator.genFailedResult("password error");
        }
        // 更新登录时间
        this.userService.updateLoginTime(user.getUsername());
        return this.getToken(user);
    }

    @ApiOperation(value = "用户注销")
    @GetMapping("/logout")
    public Result logout(@AuthenticationPrincipal final UserDetails userDetails) {
        this.jwtUtil.invalidRedisStore(userDetails.getUsername());
        return ResultGenerator.genOkResult();
    }

    /**
     * 获得 token
     */
    private Result getToken(final User user) {
        final String username = user.getUsername();
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        final String token = this.jwtUtil.sign(username, userDetails.getAuthorities());
        return ResultGenerator.genOkResult(token);
    }
}
