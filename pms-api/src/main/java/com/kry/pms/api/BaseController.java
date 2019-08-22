package com.kry.pms.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.util.StringUtil;


public class BaseController<T> {
	
	public String getCurrentUserId() {
		return "";
	}
	public String getEmployeeId() {
		return "";
	}
	public String getCurrentHotleCode() {
		return "";
	}
	public HttpResponse<T> getDefaultResponse(){
		return new HttpResponse<T>();
	}
	public PageRequest<T> parse2PageRequest(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException {
		PageRequest<T> pr = new PageRequest<>();
		Map<String, String[]> params = request.getParameterMap();
		if (params.containsKey(Constants.KEY_PAGE_SIZE)) {
			pr.setPageSize(Integer.valueOf(params.get(Constants.KEY_PAGE_SIZE)[0]));
		}
		if (params.containsKey(Constants.KEY_PAGE_NUM)) {
			pr.setPageNum(Integer.valueOf(params.get(Constants.KEY_PAGE_NUM)[0]) - 1);
		}
		pr.setExb(parse2Exb(params));
		return pr;
	}

	private T parse2Exb(Map<String, String[]> params) throws InstantiationException, IllegalAccessException {
		Class<T> cls = null;
		Type type = getClass().getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] p = ((ParameterizedType) type).getActualTypeArguments();
			cls = (Class<T>) p[0];
		}
		T t = cls.newInstance();
		Field f = null;
		Method m = null;
		Method g = null;
		for (String key : params.keySet()) {
			if (key.startsWith("page")) {
				continue;
			}
			if (key.contains(".")) {
				// 如果带。表示为传入子类参数
				Field sf = null;
				Method sm = null;
				String[] keys = key.split("\\.");
				if (keys.length != 2) {
					continue;
				} else {
					try {
						f = cls.getDeclaredField(keys[0]);
						m = cls.getDeclaredMethod(Constants.KEY_METHOD_SET_PRE + StringUtil.captureName(keys[0]),
								f.getType());
						g = cls.getDeclaredMethod(Constants.KEY_METHOD_GET_PRE + StringUtil.captureName(keys[0]));
						Object obj = g.invoke(t);
						if (obj == null) {
							obj = f.getType().newInstance();
							m.invoke(t, obj);
						}
						if ("id".equals(keys[1])) {
							sf = f.getType().getSuperclass().getDeclaredField(keys[1]);
							sm = f.getType().getSuperclass().getDeclaredMethod(
									Constants.KEY_METHOD_SET_PRE + StringUtil.captureName(keys[1]), sf.getType());
							if (sf.getType().equals(Integer.class)) {
								sm.invoke(obj, Integer.parseInt(params.get(key)[0]));
							} else if (sf.getType().equals(String.class)) {
								sm.invoke(obj, params.get(key)[0]);
							}
						} else {
							sf = f.getType().getDeclaredField(keys[1]);
							sm = f.getType().getDeclaredMethod(
									Constants.KEY_METHOD_SET_PRE + StringUtil.captureName(keys[1]), sf.getType());
							if (sf.getType().equals(Integer.class)) {
								sm.invoke(obj, Integer.parseInt(params.get(key)[0]));
							} else if (sf.getType().equals(String.class)) {
								sm.invoke(obj, params.get(key)[0]);
							}
						}
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					if ("id".equals(key)) {
						f = cls.getDeclaredField(key);
						m = cls.getDeclaredMethod(Constants.KEY_METHOD_SET_PRE + StringUtil.captureName(key),
								f.getType());
						if (f.getType().equals(Integer.class)) {
							m.invoke(t, Integer.parseInt(params.get(key)[0]));
						} else if (f.getType().equals(String.class)) {
							m.invoke(t, params.get(key)[0]);
						}
					} else {
						f = cls.getSuperclass().getDeclaredField(key);
						m = cls.getSuperclass().getDeclaredMethod(
								Constants.KEY_METHOD_SET_PRE + StringUtil.captureName(key), f.getType());
						if (f.getType().equals(Integer.class)) {
							m.invoke(t, Integer.parseInt(params.get(key)[0]));
						} else if (f.getType().equals(String.class)) {
							m.invoke(t, params.get(key)[0]);
						}
					}

				} catch (NoSuchFieldException e) {
					try {
						f = cls.getDeclaredField(key);
						m = cls.getDeclaredMethod(
								Constants.KEY_METHOD_SET_PRE + StringUtil.captureName(key), f.getType());
						if (f.getType().equals(Integer.class)) {
							m.invoke(t, Integer.parseInt(params.get(key)[0]));
						} else if (f.getType().equals(String.class)) {
							m.invoke(t, params.get(key)[0]);
						}else if(f.getType().equals(Boolean.class)) {
							m.invoke(t, Boolean.parseBoolean(params.get(key)[0]));
						}else if(f.getType().equals(LocalDate.class)) {
							m.invoke(t, LocalDate.parse(params.get(key)[0]));
						}
					} catch (NoSuchFieldException | SecurityException e1) {
						e1.printStackTrace();
					} catch (NoSuchMethodException e1) {
						e1.printStackTrace();
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
					} catch (IllegalArgumentException e1) {
						e1.printStackTrace();
					} catch (InvocationTargetException e1) {
						e1.printStackTrace();
					}
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return t;
	}

}
