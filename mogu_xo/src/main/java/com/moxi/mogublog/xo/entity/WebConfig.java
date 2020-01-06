package com.moxi.mogublog.xo.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 网站配置表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018年11月11日14:54:12
 */
@Data
@TableName("t_web_config")
public class WebConfig extends SuperEntity<WebConfig> {

    private static final long serialVersionUID = 1L;


    /**
     * 网站Logo
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String logo;

    /**
     * 网站名称
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String name;

    /**
     * 标题
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String title;

    /**
     * 描述
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String summary;

    /**
     * 关键字
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String keyword;

    /**
     * 作者
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String author;

    /**
     * 备案号
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String recordNum;

    /**
     * 支付宝收款码FileId
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String aliPay;

    /**
     * 微信收款码FileId
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String weixinPay;

    /**
     * 是否开启评论(0:否， 1:是)
     */
    private String startComment;

    // 以下字段不存入数据库，封装为了方便使用

    /**
     * 标题图
     */
    @TableField(exist = false)
    private List<String> photoList;

    /**
     * 支付宝付款码
     */
    @TableField(exist = false)
    private String aliPayPhoto;

    /**
     * 微信付款码
     */
    @TableField(exist = false)
    private String weixinPayPhoto;

}
