package net.dreamlu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自己实现的ConcurrentMultiMap 重复key的map，使用监听的type，取出所有的监听器
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年6月25日下午8:36:17
 */
public class ConcurrentMultiMap<K, V> {

	private transient final ConcurrentMap<K, List<V>> map;

	public ConcurrentMultiMap() {
		map = new ConcurrentHashMap<K, List<V>>();
	}

	List<V> createlist() {
		return new ArrayList<V>();
	}

	/**
	 * put to ArrayListMultimap
	 * @param key 键
	 * @param value 值
	 * @return boolean
	 */
	public boolean put(K key, V value) {
		List<V> list = map.get(key);
		if (list == null) {
			list = createlist();
			if (list.add(value)) {
				map.put(key, list);
				return true;
			} else {
				throw new AssertionError("New list violated the list spec");
			}
		} else if (list.add(value)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * get List by key
	 * @param key 键
	 * @return List
	 */
	public List<V> get(K key) {
		List<V> list = map.get(key);
		if (list == null) {
			list = createlist();
		}
		return list;
	}

	/**
	 * clear ConcurrentMultiMap
	 */
	public void clear() {
		map.clear();
	}

}
