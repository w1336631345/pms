package com.kry.pms.service.util;

import com.kry.pms.model.annotation.PropertyMsg;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class BeanChangeUtil {
    public String contrastObj(Object oldBean, Object newBean) {
        // 创建字符串拼接对象
        StringBuilder str = new StringBuilder();
        // 转换为传入的泛型T
        Object pojo1 =  oldBean;
        Object pojo2 =  newBean;
        // 通过反射获取类的Class对象
        Class clazz = pojo1.getClass();
        // 获取类型及字段属性
        Field[] fields = clazz.getDeclaredFields();
        return jdk8Before(fields, pojo1, pojo2, str,clazz);
//        return jdk8OrAfter(fields, pojo1, pojo2, str,clazz);
    }

    // jdk8 普通循环方式
    public String jdk8Before(Field[] fields,Object pojo1,Object pojo2,StringBuilder str,Class clazz){
        String strName01 = clazz.getName();// 获取完整类名
        String strName02 = clazz.getSimpleName();// 直接获取类名

        int i = 1;
        try {
            for (Field field : fields) {
                System.out.println(field.getName());
                System.out.println(field.getGenericType());//打印该类的所有属性类型
                PropertyMsg p = field.getAnnotation(PropertyMsg.class);
                if(field.isAnnotationPresent(PropertyMsg.class)){
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    // 获取对应属性值
                    Method getMethod = pd.getReadMethod();
                    Object o1 = getMethod.invoke(pojo1);
                    Object o2 = getMethod.invoke(pojo2);
                    if (o1 == null || o2 == null) {
                        continue;
                    }
                    if (!o1.toString().equals(o2.toString())) {
                        str.append(i + "、" + field.getAnnotation(PropertyMsg.class).value() + ":" + "修改前=>" + o1 + ",修改后=>" + o2 + "\n");
                        i++;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    // lambda表达式，表达式内部的变量都是final修饰，需要传入需要传入final类型的数组
    public String jdk8OrAfter(Field[] fields, Object pojo1, Object pojo2, StringBuilder str, Class clazz){
        final int[] i = {1};
        Arrays.asList(fields).forEach(f -> {
            if(f.isAnnotationPresent(PropertyMsg.class)){
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(f.getName(), clazz);
                    // 获取对应属性值
                    Method getMethod = pd.getReadMethod();
                    Object o1 = getMethod.invoke(pojo1);
                    Object o2 = getMethod.invoke(pojo2);
                    if (o1 == null || o2 == null) {
                        return;
                    }
                    if (!o1.toString().equals(o2.toString())) {
                        str.append(i[0] + "、" + f.getAnnotation(PropertyMsg.class).value() + ":" + "修改前=>" + o1 + "\t修改后=>" + o2 + "\n");
                        i[0]++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return str.toString();
    }
}
