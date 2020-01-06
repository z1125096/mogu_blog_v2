package com.moxi.mogublog.admin.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moxi.mogublog.admin.feign.PictureFeignClient;
import com.moxi.mogublog.admin.global.SQLConf;
import com.moxi.mogublog.admin.global.SysConf;
import com.moxi.mogublog.config.jwt.Audience;
import com.moxi.mogublog.config.jwt.JwtHelper;
import com.moxi.mogublog.utils.*;
import com.moxi.mogublog.xo.entity.Admin;
import com.moxi.mogublog.xo.entity.CategoryMenu;
import com.moxi.mogublog.xo.entity.Role;
import com.moxi.mogublog.xo.service.AdminService;
import com.moxi.mogublog.xo.service.CategoryMenuService;
import com.moxi.mogublog.xo.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 登录管理RestApi(为了更好地使用security放行把登录管理放在AuthRestApi中)
 * </p>
 *
 * @author limbo
 * @since 2018-10-14
 */
@RestController
@RequestMapping("/auth")
@Api(value = "登录管理RestApi", tags = {"loginRestApi"})
public class LoginRestApi {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CategoryMenuService categoryMenuService;

    @Autowired
    private Audience audience;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @Value(value = "${isRememberMeExpiresSecond}")
    private int longExpiresSecond;

    @Autowired
    private PictureFeignClient pictureFeignClient;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public String login(HttpServletRequest request,
                        @ApiParam(name = "username", value = "用户名或邮箱或手机号", required = false) @RequestParam(name = "username", required = false) String username,
                        @ApiParam(name = "password", value = "密码", required = false) @RequestParam(name = "password", required = false) String password,
                        @ApiParam(name = "isRememberMe", value = "是否记住账号密码", required = false) @RequestParam(name = "isRememberMe", required = false, defaultValue = "0") int isRememberMe) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultUtil.result(SysConf.ERROR, "账号或密码不能为空");
        }
        Boolean isEmail = CheckUtils.checkEmail(username);
        Boolean isMobile = CheckUtils.checkMobileNumber(username);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (isEmail) {
            queryWrapper.eq(SQLConf.EMAIL, username);
        } else if (isMobile) {
            queryWrapper.eq(SQLConf.MOBILE, username);
        } else {
            queryWrapper.eq(SQLConf.USER_NAME, username);
        }
        Admin admin = adminService.getOne(queryWrapper);
        if (admin == null) {
            return ResultUtil.result(SysConf.ERROR, "用户名或密码错误");
        }
        //验证密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(password, admin.getPassWord());
        if (!isPassword) {
            //密码错误，返回提示
            return ResultUtil.result(SysConf.ERROR, "用户名或密码错误");
        }

        List<String> roleUids = new ArrayList<>();
        roleUids.add(admin.getRoleUid());
        List<Role> roles = (List<Role>) roleService.listByIds(roleUids);
        String roleNames = null;
        for (Role role : roles) {
            roleNames += (role.getRoleName() + ",");
        }
        String roleName = roleNames.substring(0, roleNames.length() - 2);
        long expiration = isRememberMe == 1 ? longExpiresSecond : audience.getExpiresSecond();
        String jwtToken = jwtHelper.createJWT(admin.getUserName(),
                admin.getUid(),
                roleName.toString(),
                audience.getClientId(),
                audience.getName(),
                expiration * 1000,
                audience.getBase64Secret());
        String token = tokenHead + jwtToken;
        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.TOKEN, token);

        //进行登录相关操作
        Integer count = admin.getLoginCount() + 1;
        admin.setLoginCount(count);
        admin.setLastLoginIp(IpUtils.getIpAddr(request));
        admin.setLastLoginTime(new Date());
        admin.updateById();

        return ResultUtil.result(SysConf.SUCCESS, result);
    }

    @ApiOperation(value = "用户信息", notes = "用户信息", response = String.class)
    @GetMapping(value = "/info")
    public String info(HttpServletRequest request,
                       @ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {

        Map<String, Object> map = new HashMap<>();
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        map.put(SysConf.TOKEN, token);
        //获取图片
        if (StringUtils.isNotEmpty(admin.getAvatar())) {
            String pictureList = this.pictureFeignClient.getPicture(admin.getAvatar(), SysConf.FILE_SEGMENTATION);
            admin.setPhotoList(WebUtils.getPicture(pictureList));

            List<String> list = WebUtils.getPicture(pictureList);

            if (list.size() > 0) {
                map.put(SysConf.AVATAR, list.get(0));
            } else {
                map.put(SysConf.AVATAR, "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            }
        }


        //加载这些角色所能访问的菜单页面列表
        //1)获取该管理员所有角色
        List<String> roleUid = new ArrayList<>();
        roleUid.add(admin.getRoleUid());
        Collection<Role> roleList = roleService.listByIds(roleUid);

        map.put(SysConf.ROLES, roleList);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "获取当前用户的菜单", notes = "获取当前用户的菜单", response = String.class)
    @GetMapping(value = "/getMenu")
    public String getMenu(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());

        //加载这些角色所能访问的菜单页面列表
        //1)获取该管理员所有角色
        List<String> roleUid = new ArrayList<>();
        roleUid.add(admin.getRoleUid());
        Collection<Role> roleList = roleService.listByIds(roleUid);

        List<String> categoryMenuUids = new ArrayList<>();

        roleList.forEach(item -> {
            String caetgoryMenuUids = item.getCategoryMenuUids();
            String[] uids = caetgoryMenuUids.replace("[", "").replace("]", "").replace("\"", "").split(",");
            for (int a = 0; a < uids.length; a++) {
                categoryMenuUids.add(uids[a]);
            }

        });

        Collection<CategoryMenu> categoryMenuList = categoryMenuService.listByIds(categoryMenuUids);

        List<CategoryMenu> childCategoryMenuList = new ArrayList<>();
        List<String> parentCategoryMenuUids = new ArrayList<>();

        categoryMenuList.forEach(item -> {

            //选出所有的二级分类
            if (item.getMenuLevel() == 2) {

                if (StringUtils.isNotEmpty(item.getParentUid())) {
                    parentCategoryMenuUids.add(item.getParentUid());
                }
                childCategoryMenuList.add(item);
            }

        });

        Collection<CategoryMenu> parentCategoryMenuList = categoryMenuService.listByIds(parentCategoryMenuUids);
        List<CategoryMenu> list = new ArrayList<>(parentCategoryMenuList);
        //对parent进行排序
        Collections.sort(list);

        map.put(SysConf.PARENT_LIST, list);
        map.put(SysConf.SON_LIST, childCategoryMenuList);
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    public String logout(@ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {
        String destroyToken = null;
        return ResultUtil.result(SysConf.SUCCESS, destroyToken);
    }

}
