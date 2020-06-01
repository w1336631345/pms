package com.kry.pms.component.aop;

import com.kry.pms.model.annotation.PropertyMsg;
import com.kry.pms.model.annotation.UpdateAnnotation;
import com.kry.pms.model.persistence.PersistenceModel;
import com.kry.pms.model.persistence.log.UpdateLog;
import com.kry.pms.service.log.UpdateLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class UpdateLogAop {

    @Autowired
    UpdateLogService updateLogService;

    private static final String NAME_METHOD_FIND_BY_ID = "findById";
    private static final String NAME_METHOD_FIND_BY_ID_LOG = "logFindById";
    private static final String NAME_METHOD_GET_ID = "getId";

    @Pointcut("@annotation(com.kry.pms.model.annotation.UpdateAnnotation)")
    public void oldUpdateNew(){

    }

    @Around(value = "oldUpdateNew()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取注解
        UpdateAnnotation updateAnnotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(UpdateAnnotation.class);
        String fieldvalue = updateAnnotation.value();
        String fieldName =  updateAnnotation.name();//注释传入的字段说明
        String fieldType =  updateAnnotation.type();//注释传入的字段说明

        Object newObj = joinPoint.getArgs()[0];
        Object oldObj = findOldData(joinPoint.getTarget(), newObj);
        // 通过反射获取类的Class对象
        Class clazz = newObj.getClass();
        Object resultValue = getObjectValue(newObj, clazz, fieldvalue);//获取注解想要查询字段的值
        Object hotelCode = getObjectValue(newObj,clazz, "hotelCode");
        String value = null;
        String hCode = null;
        String code = null;
        if(resultValue != null){
            value = resultValue.toString();
        }
        if(hotelCode != null){
            hCode = hotelCode.toString();
        }
        if("GO".equals(fieldType)){//主单的记录日志
            Object account = getObjectValue(newObj, clazz, "account");//获取账号
            if(account != null){
                Class clazzAccount = account.getClass();
                Object accountCode = getObjectValue(account, clazzAccount, "code");
                if(accountCode != null){
                    code = accountCode.toString();
                }
            }
        }
        // 获取类型及字段属性
        Field[] fields = clazz.getDeclaredFields();
        String sStr = jdk8Before(fields, oldObj, newObj, clazz, fieldName, value, hCode, fieldType, code);
        Object proceed = joinPoint.proceed();
        return proceed;
    }

    private Object findOldData(Object target, Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method method = target.getClass().getMethod(NAME_METHOD_FIND_BY_ID, String.class);
        Method method = target.getClass().getMethod(NAME_METHOD_FIND_BY_ID_LOG, String.class);
        if(method == null){
            method = target.getClass().getMethod(NAME_METHOD_FIND_BY_ID, String.class);
        }
        Method getIdMethod = object.getClass().getMethod(NAME_METHOD_GET_ID);
        String id = getIdMethod.invoke(object).toString();
        return method.invoke(target, id);
    }

    // jdk8 普通循环方式
    public String jdk8Before(Field[] fields,Object pojo1,Object pojo2, Class clazz, String name, String value, String hotelCode, String type, String code){
        try {
            for (Field field : fields) {
                if(field.isAnnotationPresent(PropertyMsg.class)){
                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    // 获取对应属性值
                    Method getMethod = pd.getReadMethod();
                    Object o1 = getMethod.invoke(pojo1);
                    Object o2 = getMethod.invoke(pojo2);
                    if (o1 == null || o2 == null) {
                        continue;
                    }
                    //判断属性是否是对象类型，并调用子对象中的关键字段记录
                    if(field.getType().getSuperclass() != null && (PersistenceModel.class).equals(field.getType().getSuperclass())){
                        System.out.println("这个属性是对象类型");
                        // 通过反射获取类的Class对象
                        Class childclazz = o2.getClass();
                        // 获取类型及字段属性
                        Field[] childfields = childclazz.getDeclaredFields();
                        for(Field f : childfields){
                            if(f.isAnnotationPresent(PropertyMsg.class)){
                                PropertyDescriptor pde = new PropertyDescriptor(f.getName(), childclazz);
                                Method getMethod1 = pde.getReadMethod();
                                Method getMethod2 = pde.getReadMethod();
                                Object s1 = getMethod1.invoke(o1);
                                Object s2 = getMethod2.invoke(o2);
                                if (s1 == null || s2 == null) {
                                    continue;
                                }else {
                                    if (!s1.toString().equals(s2.toString())) {
                                        UpdateLog updateLog = new UpdateLog();
                                        updateLog.setProduct(f.getAnnotation(PropertyMsg.class).value());
                                        updateLog.setProductType(type);
                                        updateLog.setProductName(name);
                                        updateLog.setProductValue(value);
                                        updateLog.setOldValue(s1.toString());
                                        updateLog.setNewValue(s2.toString());
                                        updateLog.setHotelCode(hotelCode);
                                        updateLog.setIdentifier(code);
                                        updateLogService.add(updateLog);
                                    }
                                }
                            }
                        }
                    }else {
                        if (!o1.toString().equals(o2.toString())) {
                            UpdateLog updateLog = new UpdateLog();
                            updateLog.setProduct(field.getAnnotation(PropertyMsg.class).value());
                            updateLog.setProductType(type);
                            updateLog.setProductName(name);
                            updateLog.setProductValue(value);
                            updateLog.setOldValue(o1.toString());
                            updateLog.setNewValue(o2.toString());
                            updateLog.setHotelCode(hotelCode);
                            updateLog.setIdentifier(code);
                            updateLogService.add(updateLog);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public Object getObjectValue(Object object,Class clazz, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String upperChar = name.substring(0,1).toUpperCase();//首字母转换成大写
        String anotherStr = name.substring(1);
        String methodName = "get" + upperChar + anotherStr;//转换成方法名称如：getHotelCode
        Method method = clazz.getMethod(methodName, new Class[]{});
        Object resultValue = method.invoke(object);
        return resultValue;
    }
}
