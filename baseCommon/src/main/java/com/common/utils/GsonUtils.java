package com.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson Tools
 * 
 */
public class GsonUtils {

    private GsonUtils(){

    }

	private static Gson gsonByDateFormat;
    private static final Gson gson = new GsonBuilder().create();
    private static final Gson anotherGson = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * <pre>
     * JSON字符串转换为List数组, 提供两种方式(主要解决调用的容易程度)
     *     1. TypeToken<List<T>> token 参数转换
     *     2. Class<T> cls 方式转换
     * 
     * @param json
     * @return List<T>
     * 
     * <pre>
     */
    public static <T> List<T> convertList(String json, TypeToken<List<T>> token) {
        return gson.fromJson(json, token.getType());
    }

    /**
     * <pre>
     * Json格式转换,指定日期格式 由JSON字符串转化到制定类型T
     * 
     * @param json
     * @param cls
     * @return T
     * 
     * <pre>
     */
    public static <T> T convertObjByDateFormat(String dateFormat ,String json, Class<T> cls) {
    	gsonByDateFormat = new GsonBuilder().setDateFormat(dateFormat).create();
        return gsonByDateFormat.fromJson(json, cls);
    }

    public static <T> List<T> convertList(String json, Class<T> cls) {
        Type type = new TypeToken<List<JsonObject>>() {
        }.getType();
        List<JsonObject> jsonObjs = gson.fromJson(json, type);
        List<T> listOfT = new ArrayList<>();
        for (JsonObject jsonObj : jsonObjs) {
            listOfT.add(convertObj(jsonObj.toString(), cls));
        }
        return listOfT;
    }

    /**
     * <pre>
     * Json格式转换, 由JSON字符串转化到制定类型T
     * 
     * @param json
     * @param cls
     * @return T
     * 
     * <pre>
     */
    public static <T> T convertObj(String json, Class<T> cls) {
        return gson.fromJson(json, cls);
    }

    public static <T> T conver(String json, TypeToken<T> typeToken){
        return gson.fromJson(json,typeToken.getType());
    }


    /**
     * <pre>
     * java对象转化JSON
     * 
     * @return String
     * 
     * <pre>
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return "";
        }
        return gson.toJson(obj);
    }
    public static String anotherToJson(Object obj) {
    	if (obj == null) {
    		return "";
    	}
    	return anotherGson.toJson(obj);
    }

    public static String getJsonObjectAsString(JsonObject jsonObject, String name) {
        JsonElement jsonElement = jsonObject.get(name);
        return (jsonElement == null) ? null : jsonElement.getAsString();
    }

    public static JsonObject getJsonObjectChild(JsonObject jsonObject, String name) {
        JsonElement jsonElement = jsonObject.get(name);
        return (jsonElement == null) ? null : jsonElement.getAsJsonObject();
    }

    public static boolean getJsonObjectAsBoolean(JsonObject jsonObject, String name) {
        JsonElement jsonElement = jsonObject.get(name);
        return (jsonElement != null) && jsonElement.getAsBoolean();
    }
}
