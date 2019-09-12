package com.kry.pms.api;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.Entity;

import org.reflections.Reflections;

import com.kry.pms.model.persistence.goods.Product;
import com.kry.pms.model.persistence.goods.ProductCategory;
import com.kry.pms.model.persistence.goods.SetMeal;
import com.kry.pms.model.persistence.goods.SetMealItem;
import com.kry.pms.model.persistence.marketing.RoomPriceScheme;
import com.kry.pms.model.persistence.marketing.RoomPriceSchemeItem;
import com.kry.pms.util.Freemarker;

public class CodeCreater {

	public static final String PATH_BASE_DAO = "D:\\workspace\\kry\\pms\\pms-dao\\src\\main\\java\\com\\kry\\pms\\dao";
	public static final String PATH_BASE_SERVICE = "D:\\workspace\\kry\\pms\\pms-service\\src\\main\\java\\com\\kry\\pms\\service";
	public static final String PATH_BASE_CONTROLLER = "D:\\workspace\\kry\\pms\\pms-api\\src\\main\\java\\com\\kry\\pms\\api";
	public static final String FTL_PATH = "";
	public static final String PATH_BASE_VUE = "D:\\kry\\pms-web\\src\\views";
	public static final String PATH_BASE_API = "D:\\kry\\pms-web\\src\\api";

	public static void main(String[] args) {

		ArrayList<Class> clazzs = new ArrayList<>();
//		clazzs.add(RoomTypeQuantity.class);
//		clazzs.add(RoomStatusQuantity.class);
//		clazzs.add(Product.class);
//		clazzs.add(ProductCategory.class);
		clazzs.add(SetMeal.class);
		clazzs.add(SetMealItem.class);
		for(Class cls : clazzs) {
			createCodeForClass(cls);
		}
		
	}

	private static void createCodeForClass(Class cls) {
		HashMap<String, Object> data = new HashMap<>();
		data.put("sub_package", cls.getCanonicalName());
		Field fs[] = cls.getDeclaredFields();
		ArrayList<String> fss = new ArrayList<>();
		for (Field f : fs) {
			fss.add(f.getName());
		}
		data.put("fields", fss);
		String subPackage = cls.getCanonicalName().split("\\.")[5];
		String fullClassName = cls.getCanonicalName();
		String simpleClassName = cls.getSimpleName();
		String simpleClassNameLowFirst = simpleClassName.substring(0, 1).toLowerCase() + simpleClassName.substring(1);
		data.put("sub_package", subPackage);
		data.put("model_full_class_name", fullClassName);
		data.put("model_simple_class_name", simpleClassName);
		data.put("model_simple_class_name_low_first", simpleClassNameLowFirst);
		try {
			createDao(data, simpleClassName, subPackage);
			createImpl(data, simpleClassName, subPackage);
			createInterface(data, simpleClassName, subPackage);
			createController(data, simpleClassName, subPackage);
//			createApi(data, simpleClassName, subPackage);
//			createVue(data, simpleClassName, subPackage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createDao(HashMap<String, Object> data, String simpleClassName, String subPackage)
			throws Exception {
		Freemarker.printFile("dao.ftl", data, simpleClassName + "Dao.java", PATH_BASE_DAO + "\\" + subPackage + "\\",
				"C:\\Users\\Louis\\Desktop\\");
	}

	private static void createInterface(HashMap<String, Object> data, String simpleClassName, String subPackage)
			throws Exception {
		Freemarker.printFile("interface.ftl", data, simpleClassName + "Service.java",
				PATH_BASE_SERVICE + "\\" + subPackage + "\\", "C:\\Users\\Louis\\Desktop\\");
	}

	private static void createImpl(HashMap<String, Object> data, String simpleClassName, String subPackage)
			throws Exception {
		Freemarker.printFile("serviceImpl.ftl", data, simpleClassName + "ServiceImpl.java",
				PATH_BASE_SERVICE + "\\" + subPackage + "\\impl\\", "C:\\Users\\Louis\\Desktop\\");
	}

	private static void createController(HashMap<String, Object> data, String simpleClassName, String subPackage)
			throws Exception {
		Freemarker.printFile("controller.ftl", data, simpleClassName + "Controller.java",
				PATH_BASE_CONTROLLER + "\\" + subPackage + "\\", "C:\\Users\\Louis\\Desktop\\");
	}

	private static void createVue(HashMap<String, Object> data, String simpleClassName, String subPackage)
			throws Exception {
		Freemarker.printFile("vue.ftl", data, simpleClassName + ".vue", PATH_BASE_VUE + "\\" + subPackage + "\\",
				"C:\\Users\\Louis\\Desktop\\");
	}

	private static void createApi(HashMap<String, Object> data, String simpleClassName, String subPackage)
			throws Exception {
		Freemarker.printFile("api.ftl", data, simpleClassName + ".js", PATH_BASE_API + "\\" + subPackage + "\\",
				"C:\\Users\\Louis\\Desktop\\");
	}

	private static Set<Class<?>> getAllSubClass() {

		Reflections reflections = new Reflections("com.kry.pms.model.*");
		// 比如可以获取有Pay注解的class
		Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
		return classes;
	}

}
