package com.moxi.mogublog.xo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moxi.mogublog.utils.StringUtils;
import com.moxi.mogublog.xo.entity.Blog;
import com.moxi.mogublog.xo.entity.BlogSort;
import com.moxi.mogublog.xo.entity.Tag;
import com.moxi.mogublog.xo.mapper.BlogMapper;
import com.moxi.mogublog.xo.mapper.BlogSortMapper;
import com.moxi.mogublog.xo.mapper.TagMapper;
import com.moxi.mogublog.xo.service.BlogService;
import com.moxi.mougblog.base.enums.EPublish;
import com.moxi.mougblog.base.enums.EStatus;
import com.moxi.mougblog.base.global.BaseSQLConf;
import com.moxi.mougblog.base.serviceImpl.SuperServiceImpl;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author xuzhixiang
 * @since 2018-09-08
 */
@Service
public class BlogServiceImpl extends SuperServiceImpl<BlogMapper, Blog> implements BlogService {
	
	@Autowired
	TagMapper tagMapper;
	
	@Autowired
	BlogSortMapper blogSortMapper;
	
	@Autowired
	BlogMapper blogMapper;
	
	@Override
	public List<Blog> setTagByBlogList(List<Blog> list) {
		for(Blog item : list) {
			if(item != null) {
				setTagByBlog(item);	
			}			
		}
		return list;
	}

	@Override
	public Blog setTagByBlog(Blog blog) {
		String tagUid = blog.getTagUid();
		if(!StringUtils.isEmpty(tagUid)) {			
			String uids[] = tagUid.split(",");
			List<Tag> tagList = new ArrayList<Tag>();
			for(String uid : uids) {
				Tag  tag = tagMapper.selectById(uid);
				if(tag != null && tag.getStatus() != EStatus.DISABLED) {
					tagList.add(tag);						
				}
			}
			blog.setTagList(tagList);
		}
		return blog;
	}

	@Override
	public Blog setSortByBlog(Blog blog) {
		
		if(blog != null && !StringUtils.isEmpty(blog.getBlogSortUid())) {
			BlogSort blogSort = blogSortMapper.selectById(blog.getBlogSortUid());
			blog.setBlogSort(blogSort);
		}		
		return blog;
	}

	@Override
	public List<Blog> getBlogListByLevel(Integer level) {
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BaseSQLConf.LEVEL, level);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		
		List<Blog> list = blogMapper.selectList(queryWrapper);		
		return list;
	}

	@Override
	public IPage<Blog> getBlogPageByLevel(Page<Blog> page, Integer level) {
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BaseSQLConf.LEVEL, level);
		queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		
		//因为首页并不需要显示内容，所以需要排除掉内容字段		
//		queryWrapper.excludeColumns(Blog.class, "content");
		queryWrapper.select(Blog.class, i-> !i.getProperty().equals("content"));
		
		return blogMapper.selectPage(page, queryWrapper);
	}

	@Override
	public Integer getBlogCount(Integer status) {
		QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(BaseSQLConf.STATUS, EStatus.ENABLE);
		queryWrapper.eq(BaseSQLConf.IS_PUBLISH, EPublish.PUBLISH);
		
		return blogMapper.selectCount(queryWrapper);
	}

	@Override
	public List<Map<String, Object>> getBlogCountByTag() {
		
		List<Map<String, Object>> blogCoutByTagMap = blogMapper.getBlogCountByTag();
		
		Map<String, Integer> tagMap = new HashMap<String, Integer>();

		for(Map<String, Object> item : blogCoutByTagMap) {
			String tagUid = String.valueOf(item.get("tag_uid"));
			// java.lang.Number是Integer,Long的父类
			Number num = (Number)item.get("count");
			Integer count = num.intValue();
			
			//如果只有一个UID的情况
			if(tagUid.length() == 32) {
				
				//如果没有这个内容的话，就设置
				if(tagMap.get(tagUid) == null) {
					tagMap.put(tagUid, count);
				} else {
					Integer tempCount = tagMap.get(tagUid) + count;
					tagMap.put(tagUid, tempCount);
				}
				
			} else {				
				//如果长度大于32，说明含有多个UID
				if(StringUtils.isNotEmpty(tagUid)) {
					List<String> strList = StringUtils.changeStringToString(tagUid, ",");
					for(String strItem : strList) {
						if(tagMap.get(strItem) == null) {
							tagMap.put(strItem, count);
						} else {
							Integer tempCount = tagMap.get(strItem) + count;
							tagMap.put(strItem, tempCount);
						}
					}	
				}				
			}			
		}
		
		//把查询到的Tag放到Map中
		Set<String> tagUids = tagMap.keySet();
		Collection<Tag> tagCollection = new ArrayList<>();

		if(tagUids.size() > 0) {
			tagCollection = tagMapper.selectBatchIds(tagUids);
		}

		Map<String, String> tagEntityMap = new HashMap<>();
		for(Tag tag : tagCollection) {
			if(StringUtils.isNotEmpty(tag.getContent())) {
				tagEntityMap.put(tag.getUid(), tag.getContent());	
			}			
		}
		
		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();		
		for(Map.Entry<String, Integer> entry : tagMap.entrySet()){
		    
			String tagUid = entry.getKey();
						
			if(tagEntityMap.get(tagUid) != null) {
				String tagName = tagEntityMap.get(tagUid);
				Integer count = entry.getValue();
				
				Map<String, Object> itemResultMap = new HashMap<>();
				itemResultMap.put("tagUid", tagUid);
				itemResultMap.put("name", tagName);
				itemResultMap.put("value", count);
				resultMap.add(itemResultMap);
			}					   		  
		}
		
		return resultMap;
		
	}

	@Override
	public List<Map<String, Object>> getBlogCountByBlogSort() {

		List<Map<String, Object>> blogCoutByBlogSortMap = blogMapper.getBlogCountByBlogSort();

		Map<String, Integer> blogSortMap = new HashMap<>();

		for(Map<String, Object> item : blogCoutByBlogSortMap) {

			String blogSortUid = String.valueOf(item.get("blog_sort_uid"));
			// java.lang.Number是Integer,Long的父类
			Number num = (Number)item.get("count");
			Integer count = 0;
			if(num != null) {
				count = num.intValue();
			}

			blogSortMap.put(blogSortUid, count);
		}

		//把查询到的BlogSort放到Map中
		Set<String> blogSortUids = blogSortMap.keySet();
		Collection<BlogSort> blogSortCollection = new ArrayList<>();

		if(blogSortUids.size() > 0) {
			blogSortCollection = blogSortMapper.selectBatchIds(blogSortUids);
		}

		Map<String, String> blogSortEntityMap = new HashMap<>();
		for(BlogSort blogSort : blogSortCollection) {
			if(StringUtils.isNotEmpty(blogSort.getSortName())) {
				blogSortEntityMap.put(blogSort.getUid(), blogSort.getSortName());
			}
		}

		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		for(Map.Entry<String, Integer> entry : blogSortMap.entrySet()){

			String blogSortUid = entry.getKey();

			if(blogSortEntityMap.get(blogSortUid) != null) {
				String blogSortName = blogSortEntityMap.get(blogSortUid);
				Integer count = entry.getValue();

				Map<String, Object> itemResultMap = new HashMap<>();
				itemResultMap.put("blogSortUid", blogSortUid);
				itemResultMap.put("name", blogSortName);
				itemResultMap.put("value", count);
				resultMap.add(itemResultMap);
			}
		}

		return resultMap;

	}

	@Override
	public void setBlogCopyright(Blog blog) {
		
		//如果是原创的话
		if(blog.getIsOriginal().equals("1")) {
			String str = "本文为蘑菇博客原创文章, 转载无需和我联系, 但请注明来自蘑菇博客 http://www.moguit.cn";
			blog.setCopyright(str);
		} else {
			String str = "本着开源共享、共同学习的精神, 本文转载自 " + blog.getArticlesPart() + " , 版权归"+ blog.getAuthor() +"所有，如果侵权之处，请联系博主进行删除，谢谢~";
			blog.setCopyright(str);
		}		
	}

}
