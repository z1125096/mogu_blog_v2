package com.moxi.mogublog.xo.entity;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.util.Date;

/**
 * 用于建立solr索引实体类
 *
 * @author limboy
 * @create 2018-09-29 16:12
 */
@Data
public class SolrIndex {
    /**
     * 博客uid
     */
    @Field("id")
    private String id;

    /**
     * 图片Uid
     */
    @Field("file_uid")
    private String fileUid;

    /**
     * 博客标题
     */
    @Field("blog_title")
    private String title;

    /**
     * 博客简介
     */
    @Field("blog_summary")
    private String summary;

    /**
     * 标签名
     */
    @Field("blog_tag")
    private String tag;

    /**
     * 博客分类名
     */
    @Field("blog_sort")
    private String blogSort;

    /**
     * 博客标签UID
     */
    @Field("blog_tag_uid")
    private String blogTagUid;

    /**
     * 博客分类UID
     */
    @Field("blog_sort_uid")
    private String blogSortUid;

    /**
     * 如果原创，作者为管理员名
     */
    @Field("blog_author")
    private String author;

    /**
     * 文章更新时间
     */
    @Field("blog_updateTime")
    private Date updateTime;

    /**
     * 以下字段不存入solr索引库中
     * 标题图
     */
    @Field("photo_list")
    private String photoList;

}
