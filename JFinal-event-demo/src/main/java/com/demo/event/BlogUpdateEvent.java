package com.demo.event;

import com.demo.common.model.Blog;

/**
 * 博文更新事件
 *
 * @author L.cm
 */
public class BlogUpdateEvent {
    private Blog blog;

    public BlogUpdateEvent(Blog blog) {
        this.blog = blog;
    }

    public Blog getBlog() {
        return blog;
    }
}
