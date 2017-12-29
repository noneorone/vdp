package com.noo.core.data.store.base;

import com.noo.core.log.Logger;
import com.noo.core.utils.PrefsHelper;

/**
 * 本地数据缓存基类
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/11/30<br/>
 * @since 1.0
 */

public abstract class DataStore implements IDataStore {

    protected String getNamespace() {
        return PrefsHelper.DEFAULT_NAME_SPACE;
    }

    protected boolean isSupportMultiUser() {
        return true;
    }

    private String getKey(StoreType type) {
        return getClass().getSimpleName() + "_" + type.getName();
    }

    @Override
    public void put(StoreType type, Object data) {
        PrefsHelper.get().put(getKey(type), data, isSupportMultiUser());
    }

    @Override
    public <E> E get(StoreType type) {
        return get(type, null);
    }

    public <E> E get(StoreType type, Object defValue) {
        try {
            if (defValue == null) {
                Class<?> classType = type.getClassType();
                if (String.class.equals(classType)) {
                    defValue = "";
                } else if (Boolean.class.equals(classType)) {
                    defValue = false;
                } else if (Number.class.equals(classType.getSuperclass())) {
                    defValue = 0;
                } else {
                    defValue = classType.newInstance();
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return PrefsHelper.get(getNamespace()).get(getKey(type), defValue, isSupportMultiUser());
    }

}
