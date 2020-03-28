package com.demo.event;

import com.jfinal.kit.JsonKit;
import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.EventListener;

public class BlogEventListener {

	@EventListener
	public void allEvent() {
		System.out.println("allEvent");
	}

	@EventListener
	public void allEvent(Object event) {
		System.out.println("allEvent:\t" + event);
	}

    @EventListener(value = ApplicationEvent.class)
    public void onSave(BlogSaveEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonSave:"  + JsonKit.toJson(event));
    }

    @EventListener(async = true)
    public void onSaveLog(BlogSaveEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonSaveLog:" + JsonKit.toJson(event));
    }

    @EventListener
    public void onUpdate(BlogUpdateEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonUpdate:"  + JsonKit.toJson(event));
    }

    @EventListener(async = true)
    public void onUpdateLog(BlogUpdateEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonUpdateLog:"  + JsonKit.toJson(event));
    }

    @EventListener
    public void onDelete(BlogDeleteEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonDelete:"  + JsonKit.toJson(event));
    }

    @EventListener(async = true)
    public void onDeleteLog(BlogDeleteEvent event) {
        System.out.println(Thread.currentThread().getName() + "\tonDeleteLog:"  + JsonKit.toJson(event));
    }

}
