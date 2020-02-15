package net.dreamlu.event.plugin.icons;

import com.intellij.openapi.util.IconLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.swing.*;

/**
 * 小图标
 *
 * @author L.cm
 */
@Getter
@RequiredArgsConstructor
public enum EventIcons {
	/**
	 * 监听器图标，采用 JFinal logo 的蓝色
	 */
	LISTENER(IconLoader.getIcon("/icons/listener.svg")),
	/**
	 * 发布图标，采用 JFinal logo 的蓝色
	 */
	PUBLISHER(IconLoader.getIcon("/icons/publisher.svg"));

	private final Icon icon;
}
