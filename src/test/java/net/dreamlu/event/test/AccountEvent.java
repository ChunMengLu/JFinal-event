package net.dreamlu.event.test;

/**
 * 根据 source 进行判断的事件
 *
 * @author L.cm
 */
public class AccountEvent {
	private Integer id;
	private String name;
	private Integer age;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "AccountEvent{" +
			"id=" + id +
			", name='" + name + '\'' +
			", age=" + age +
			'}';
	}
}
