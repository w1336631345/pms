package com.kry.pms.manager.api;

import com.kry.pms.base.Constants;
import com.kry.pms.base.HttpResponse;
import com.kry.pms.base.PageRequest;
import com.kry.pms.service.org.EmployeeService;
import com.kry.pms.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class BaseController<T> {
    @Autowired
    EmployeeService employeeService;

    public String getCurrentUserId() {
        return getUserId();
    }

    public String getUserId() {
        return "1";
    }


    public HttpResponse<T> getDefaultResponse() {
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
        if (params.containsKey(Constants.KEY_ORDER)) {
            pr.setOrderBy(Arrays.asList(params.get(Constants.KEY_ORDER)));
        }
        if (params.containsKey(Constants.KEY_SHORT_ASC)) {
            pr.setAsc(Boolean.valueOf(params.get(Constants.KEY_SHORT_ASC)[0]));
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
            if (key.startsWith("page")||key.equals("order")||key.equals("asc")||key.equals("desc")) {
                continue;
            }
            if (key.contains("_")) {
                // ???????????????????????????????????????
                Field sf = null;
                Method sm = null;
                String[] keys = key.split("_");
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
                        } else if (f.getType().equals(Boolean.class)) {
                            m.invoke(t, Boolean.parseBoolean(params.get(key)[0]));
                        } else if (f.getType().equals(LocalDate.class)) {
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

    public Map<String, Object> parse2Map(HttpServletRequest request) {
        PageRequest<T> pr = new PageRequest<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, String[]> params = request.getParameterMap();
        String[] val = null;
        for (String key : params.keySet()) {
            val = params.get(key);
            if (val != null) {
                if(val.length>1){
                    data.put(key+"s",Arrays.asList(val));
                }else{
                    data.put(key,val[0]);
                }
            }
        }
        data.put("user_id",getCurrentUserId());
        return data;
    }

    public PageRequest<Map<String, Object>> parse2CommonPageRequest(HttpServletRequest request) {
        PageRequest<Map<String, Object>> pr = new PageRequest<>();
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey(Constants.KEY_PAGE_SIZE)) {
            pr.setPageSize(Integer.valueOf(params.get(Constants.KEY_PAGE_SIZE)[0]));
        }
        if (params.containsKey(Constants.KEY_PAGE_NUM)) {
            pr.setPageNum(Integer.valueOf(params.get(Constants.KEY_PAGE_NUM)[0]) - 1);
        }
        if (params.containsKey(Constants.KEY_ORDER)) {
            pr.setOrderBy(Arrays.asList(params.get(Constants.KEY_ORDER)));
        }
        if (params.containsKey(Constants.KEY_SHORT_ASC)) {
            pr.setAsc(Boolean.valueOf(params.get(Constants.KEY_SHORT_ASC)[0]));
        }
        pr.setExb((parse2Map(request)));
        return pr;
    }
}
