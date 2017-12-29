package com.noo.core.utils;

import android.text.TextUtils;

import com.noo.core.app.VdpApplication;
import com.noo.core.log.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能说明：本地数据存储工具，用于{@link android.content.SharedPreferences}存储<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/5/4<br/>
 * @since 1.0
 */
public class PrefsHelper {

    private SharedPreferencesUtil prefs;

    public static final String DEFAULT_NAME_SPACE = "common";

    private static final Map<String, PrefsHelper> PREF_MAPS = new HashMap<>();

    /**
     * 获取指定命名空间的{@link PrefsHelper}对象
     *
     * @param namespace 命名空间
     * @return {@link PrefsHelper}
     */
    public static final synchronized PrefsHelper get(String namespace) {
        PrefsHelper prefsHelper = null;

        if (TextUtils.isEmpty(namespace)) {
            namespace = DEFAULT_NAME_SPACE;
        }

        if (PREF_MAPS.containsKey(namespace)) {
            prefsHelper = PREF_MAPS.get(namespace);
        }

        if (prefsHelper == null) {
            prefsHelper = new PrefsHelper();
            prefsHelper.prefs = SharedPreferencesUtil.getInstance(VdpApplication.getInstance(), namespace);
            PREF_MAPS.put(namespace, prefsHelper);
        }

        return prefsHelper;
    }

    public static final PrefsHelper get() {
        return get(null);
    }

    private PrefsHelper() {
    }

    private String getUidKey(String key) {
        return getUid() + "_" + key;
    }

    /**
     * 存储键值对数值
     *
     * @param key       键
     * @param value     对象值
     * @param multiUser 是否支持多用户
     * @return {@link Boolean}
     */
    public boolean put(String key, Object value, boolean multiUser) {
        if (!TextUtils.isEmpty(key) && value != null) {
            String originalKey = key;
            if (multiUser) {
                key = getUidKey(key);
            }
            if (value instanceof Long) {
                return prefs.setParam(key, (Long) value);
            } else if (value instanceof Integer) {
                return prefs.setParam(key, (Integer) value);
            } else if (value instanceof Boolean) {
                return prefs.setParam(key, (Boolean) value);
            } else if (value instanceof String) {
                return prefs.setParam(key, String.valueOf(value));
            } else {
                return putToJson(originalKey, value, multiUser);
            }
        }
        return false;
    }

    /**
     * 通过指定的键获取对应对象值
     *
     * @param key       键
     * @param defValue  默认值
     * @param <T>       返回对象接收
     * @param multiUser 是否支持多用户
     * @return T
     */
    public <T> T get(String key, Object defValue, boolean multiUser) {
        if (!TextUtils.isEmpty(key) && defValue != null) {
            String originalKey = key;
            if (multiUser) {
                key = getUidKey(key);
            }
            Object param = null;
            if (defValue instanceof Long) {
                param = prefs.getParam(key, (Long) defValue);
            } else if (defValue instanceof Integer) {
                param = prefs.getParam(key, (Integer) defValue);
            } else if (defValue instanceof Boolean) {
                param = prefs.getParam(key, (Boolean) defValue);
            } else if (defValue instanceof String) {
                param = prefs.getParam(key, (String) defValue);
            } else {
                param = getFromJson(originalKey, multiUser);
            }
            return (T) param;
        }
        return null;
    }

    /**
     * 存储键值对数值
     *
     * @param key   键
     * @param value 对象值
     * @return {@link Boolean}
     */
    public boolean put(String key, Object value) {
        return put(key, value, false);
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
        return get(key, defValue, false);
    }

    /**
     * 获取用户UID
     *
     * @return {@link String}
     */
    private String getUid() {
        //TODO 用户系统中的UID
        return "";
    }

    /**
     * 将指定的对象以json的方式存储
     *
     * @param key  键
     * @param data 数据对象
     */
    public synchronized boolean putToJson(String key, Object data) {
        try {
            if (!TextUtils.isEmpty(key) && data != null) {
                return prefs.setParam(key, JsonUtils.toJsonString(data));
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return false;
    }

    /**
     * 将指定的对象以json的方式存储
     *
     * @param key       键
     * @param data      数据对象
     * @param multiUser 是否支持多用户
     */
    public synchronized boolean putToJson(String key, Object data, boolean multiUser) {
        if (!TextUtils.isEmpty(key)) {
            if (multiUser) {
                key = getUidKey(key);
            }
            return putToJson(key, data);
        }
        return false;
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
                String json = prefs.getParam(key, "");
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
     * @param multiUser 是否与多用户相关
     * @param <T>       指定类型对象
     * @return 指定类型接收的对象
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T getFromJson(String key, boolean multiUser) {
        if (!TextUtils.isEmpty(key)) {
            if (multiUser) {
                key = getUidKey(key);
            }
            return getFromJson(key);
        }

        return null;
    }

}
