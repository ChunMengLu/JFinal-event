package net.dreamlu.event.holder;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jfinal.core.Controller;

/**
 * 控制器Holder
 * 
 * @author L.cm
 */
public class CtrlHolder {
	protected final Controller ctrl;
	private final Map<String, String[]> paramMap;
	private final Map<String, String> headerMap;
	private final Map<String, Object> attrMap;
	private final Map<String, Object> sessionMap;
	private volatile boolean requestActive = true;
	
	public CtrlHolder(Controller ctrl) {
		this.ctrl = ctrl;
		HttpServletRequest request = ctrl.getRequest();
		this.paramMap = request.getParameterMap();
		this.attrMap = getAttrMap(request);
		this.headerMap = getHeaderMap(request);
		this.sessionMap = getSessionMap(request);
	}
	
	public boolean isRequestActive() {
		return requestActive;
	}
	
	public void requestCompleted() {
		this.requestActive = false;
	}
	
	public String getPara(String name) {
		String[] paras = paramMap.get(name);
		if (paras != null && paras.length > 0) {
			return paras[0];
		}
		return null;
	}
	
	public String getPara(String name, String defaultValue) {
		String result = getPara(name);
		return result != null && !"".equals(result) ? result : defaultValue;
	}
	
	public String[] getParaValues(String name) {
		return paramMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttr(String name) {
		return (T) attrMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttr(String name, T defaultValue) {
		T result = (T) attrMap.get(name);
		return result != null ? result : defaultValue;
	}
	
	public String getHeader(String name) {
		return headerMap.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(String key) {
		return (T) sessionMap.get(key);
	}
	
	public <T> T getSessionAttr(String key, T defaultValue) {
		T result = getSessionAttr(key);
		return result != null ? result : defaultValue;
	}
	
	public Map<String, String[]> getParamMap() {
		return paramMap;
	}
	
	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
	
	public Map<String, Object> getAttrMap() {
		return attrMap;
	}
	
	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}
	
	private Map<String, Object> getAttrMap(HttpServletRequest request) {
		Enumeration<String> attrs = request.getAttributeNames();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		while (attrs.hasMoreElements()) {
			String attr = (String) attrs.nextElement();
			attrMap.put(attr, request.getAttribute(attr));
		}
		return attrMap;
	}
	
	private Map<String, String> getHeaderMap(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaderNames();
		Map<String, String> headerMap = new HashMap<String, String>();
		while (headers.hasMoreElements()) {
			String header = (String) headers.nextElement();
			headerMap.put(header, request.getHeader(header));
		}
		return headerMap;
	}
	
	private Map<String, Object> getSessionMap(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return Collections.emptyMap();
		}
		Map<String, Object> sessionMap = new HashMap<String, Object>();
		Enumeration<String> sessionKeys = session.getAttributeNames();
		while (sessionKeys.hasMoreElements()) {
			String key = (String) sessionKeys.nextElement();
			sessionMap.put(key, session.getAttribute(key));
		}
		return sessionMap;
	}

}
