package com.demo.event;

import com.demo.common.model.Blog;

public class BlogSaveEvent {
    private Blog blog;

    public BlogSaveEvent(Blog blog) {
        this.blog = blog;
    }

    public Blog getBlog() {
        return blog;
    }
}
