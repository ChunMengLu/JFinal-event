package com.demo.event;

import com.jfinal.kit.JsonKit;
import net.dreamlu.event.core.EventListener;

public class BlogListener {

    @EventListener
    public void onSave(BlogSaveEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonSave1:"  + JsonKit.toJson(event));
    }

    @EventListener(async = true)
    public void onSaveLog(BlogSaveEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonSaveLog1:"  + JsonKit.toJson(event));
    }

    @EventListener
    public void onUpdate(BlogUpdateEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonUpdate1:"  + JsonKit.toJson(event));
    }

    @EventListener(async = true)
    public void onUpdateLog(BlogUpdateEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonUpdateLog1:"  + JsonKit.toJson(event));
    }

    @EventListener
    public void onDelete(BlogDeleteEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonDelete1:"  + JsonKit.toJson(event));
    }

    @EventListener(async = true)
    public void onDeleteLog(BlogDeleteEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonDeleteLog1:"  + JsonKit.toJson(event));
    }

}
