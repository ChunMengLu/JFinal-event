package com.demo.event;

public class BlogDeleteEvent {
    private final Integer blogId;

    public BlogDeleteEvent(Integer blogId) {
        this.blogId = blogId;
    }

    public Integer getBlogId() {
        return blogId;
    }
}
