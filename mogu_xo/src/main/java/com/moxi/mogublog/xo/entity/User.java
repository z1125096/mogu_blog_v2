package com.moxi.mogublog.xo.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.moxi.mougblog.base.entity.SuperEntity;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-04
 */
@Data
@TableName("t_user")
public class User extends SuperEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;
    
    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 性别(1:男2:女)
     */
    private String gender;

    /**
     * 个人头像
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 出生年月日
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 手机
     */
    private String mobile;
    
    /**
     * QQ号
     */
    private String qqNumber;
    
    /**
     * 微信号
     */
    private String weChat;
    
    /**
     * 职业
     */
    private String occupation;   
    
    /**
     * 自我简介最多150字
     */
    private String summary;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 最后登录时间
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    
    // 以下字段不存入数据库

	/**
	 * 用户头像
	 */
	@TableField(exist = false)
    private List<String> photoList;
    
    /**
     * 验证码
     */
    //@TableField(exist = false)
    private String validCode;

}
