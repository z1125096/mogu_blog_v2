package com.moxi.mogublog.xo.entity;

import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.moxi.mougblog.base.entity.SuperEntity;
import lombok.Data;

/**
 * <p>
 * 博客表
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Data
@TableName("t_blog")
public class Blog extends SuperEntity<Blog> {

    private static final long serialVersionUID = 1L;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客简介
     * updateStrategy = FieldStrategy.IGNORED ：表示更新时候忽略非空判断
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String summary;

    /**
     * 博客内容
     */
    private String content;

    /**
     * 标签uid
     */
    private String tagUid;
    
    /**
     * 博客分类UID
     */
    private String blogSortUid;

    /**
     * 博客点击数
     */
    private Integer clickCount;

    /**
     * 博客收藏数
     */
    private Integer collectCount;

    /**
     * 标题图片UID
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String fileUid;
    
    /**
     * 管理员UID
     */
    private String adminUid;
    
    /**
     * 是否发布
     */
    private String isPublish;
    
    /**
     * 是否原创
     */
    private String isOriginal;
    
    /**
     * 如果原创，作者为管理员名
     */
    private String author;

    /**
     * 文章出处
     */
    private String articlesPart;
    
    /**
     *	推荐级别，用于首页推荐
     *	0：正常
     *	1：一级推荐(轮播图)
     *	2：二级推荐(top)
     *	3：三级推荐 ()
     *	4：四级 推荐 (特别推荐)
     */
    private Integer level;

    // 以下字段不存入数据库，封装为了方便使用

    /**
	 * 标签,一篇博客对应多个标签
     */
    @TableField(exist = false)
    private List<Tag> tagList;

	/**
	 * 标题图
	 */
	@TableField(exist = false)
    private List<String> photoList;

	/**
	 * 博客分类
	 */
	@TableField(exist = false)
    private BlogSort blogSort;

	/**
	 * 点赞数
	 */
	@TableField(exist = false)
    private Integer praiseCount;

	/**
	 * 版权申明
	 */
	@TableField(exist = false)
    private String copyright;
}
