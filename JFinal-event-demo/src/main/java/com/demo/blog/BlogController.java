package com.demo.blog;

import com.demo.event.BlogDeleteEvent;
import com.demo.event.BlogSaveEvent;
import com.demo.event.BlogUpdateEvent;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.demo.common.model.Blog;
import net.dreamlu.event.EventKit;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 *
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
@Before(BlogInterceptor.class)
public class BlogController extends Controller {

	@Inject
	BlogService service;

	public void index() {
		setAttr("blogPage", service.paginate(getParaToInt(0, 1), 10));
		render("blog.html");
	}

	public void add() {
	}

	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void save() {
		Blog blog = getBean(Blog.class);
		blog.save();
		EventKit.post(new BlogSaveEvent(blog));
		redirect("/blog");
	}

	public void edit() {
		setAttr("blog", service.findById(getParaToInt()));
	}

	/**
	 * save 与 update 的业务逻辑在实际应用中也应该放在 serivce 之中，
	 * 并要对数据进正确性进行验证，在此仅为了偷懒
	 */
	@Before(BlogValidator.class)
	public void update() {
		Blog blog = getBean(Blog.class);
		blog.update();
		EventKit.post(new BlogUpdateEvent(blog));
		redirect("/blog");
	}

	public void delete() {
		Integer blogId = getParaToInt();
		service.deleteById(getParaToInt());
		EventKit.post(new BlogDeleteEvent(blogId));
		redirect("/blog");
	}
}


