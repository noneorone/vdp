package com.noo.core.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.noo.core.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 功能说明：本地数据存储工具，用于{@link android.content.SharedPreferences}存储<br/>
 * 作者：wangmeng on 2017/5/4 15:39<br/>
 * 邮箱：noneorone@yeah.net<br/>
 */
public class PrefsHelper {

    private SharedPreferences mPreferences;

    private static final Map<String, PrefsHelper> PREF_MAPS = new HashMap<>();

    /**
     * 获取指定命名空间的{@link PrefsHelper}对象
     *
     * @param name 命名空间
     * @return {@link PrefsHelper}
     */
    public static final synchronized PrefsHelper get(Context context, String name) {
        PrefsHelper prefsHelper = null;

        if (TextUtils.isEmpty(name)) {
            name = "common";
        }

        if (PREF_MAPS.containsKey(name)) {
            prefsHelper = PREF_MAPS.get(name);
        }

        if (prefsHelper == null) {
            prefsHelper = new PrefsHelper();
            prefsHelper.mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            PREF_MAPS.put(name, prefsHelper);
        }

        return prefsHelper;
    }

    public static final PrefsHelper get(Context context) {
        return get(context, null);
    }

    private PrefsHelper() {
    }

    /**
     * 存储键值对数值
     *
     * @param key   键
     * @param value 对象值
     * @return {@link Boolean}
     */
    public boolean put(String key, Object value) {
        if (value != null) {
            SharedPreferences.Editor editor = this.mPreferences.edit();
            if (value instanceof Long) {
                return editor.putLong(key, (Long) value).commit();
            } else if (value instanceof Integer) {
                return editor.putInt(key, (Integer) value).commit();
            } else if (value instanceof Boolean) {
                return editor.putBoolean(key, (Boolean) value).commit();
            } else if (value instanceof String) {
                return editor.putString(key, String.valueOf(value)).commit();
            }
        }
        return false;
    }


    /**
     * 通过指定的键获取对应对象值
     *
     * @param key      键
     * @param defValue 默认值
     * @param <T>      返回对象接收
     * @return T
     */
    public <T> T get(String key, Object defValue) {
        if (defValue != null) {
            Object param = null;
            if (defValue instanceof Long) {
                param = mPreferences.getLong(key, (Long) defValue);
            } else if (defValue instanceof Integer) {
                param = mPreferences.getInt(key, (Integer) defValue);
            } else if (defValue instanceof Boolean) {
                param = mPreferences.getBoolean(key, (Boolean) defValue);
            } else if (defValue instanceof String) {
                param = mPreferences.getString(key, (String) defValue);
            }
            return (T) param;
        }
        return null;
    }

    /**
     * 获取用户UID
     *
     * @return {@link String}
     */
    private String getUid() {
        String uid = UUID.randomUUID().toString();
        return uid;
    }

    /**
     * 将指定的对象以json的方式存储
     *
     * @param key  键
     * @param data 数据对象
     */
    public synchronized void putToJson(String key, Object data) {
        try {
            if (!TextUtils.isEmpty(key) && data != null) {
                put(key, JsonUtils.toJsonString(data));
            }
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 将指定的对象以json的方式存储
     *
     * @param key       键
     * @param data      数据对象
     * @param multiuser 是否支持多用户
     */
    public synchronized void putToJson(String key, Object data, boolean multiuser) {
        if (!TextUtils.isEmpty(key) && multiuser) {
            key = new StringBuffer(getUid()).append("_").append(key).toString();
        }
        putToJson(key, data);
    }

    /**
     * 获取指定key对应json转换的对象
     *
     * @param key 键
     * @param <T> 指定类型对象
     * @return 指定类型接收的对象
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T getFromJson(String key) {
        try {
            if (!TextUtils.isEmpty(key)) {
                String json = get(key, "");
                if (!TextUtils.isEmpty(json)) {
                    Object o = JsonUtils.parseObject(json);
                    if (o instanceof JSONArray) {
                        return (T) JsonUtils.praseJSONArray((JSONArray) o);
                    } else {
                        return (T) JsonUtils.praseJSONObject((JSONObject) o);
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        return null;
    }

    /**
     * 获取指定key对应json转换的对象
     *
     * @param key       键
     * @param multiuser 是否与多用户相关
     * @param <T>       指定类型对象
     * @return 指定类型接收的对象
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T getFromJson(String key, boolean multiuser) {
        if (!TextUtils.isEmpty(key) && multiuser) {
            key = new StringBuffer(getUid()).append("_").append(key).toString();
        }
        return getFromJson(key);
    }

}
