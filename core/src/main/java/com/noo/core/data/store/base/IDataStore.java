package com.noo.core.data.store.base;

/**
 * 本地数据缓存操作定义
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/11/30<br/>
 * @since 1.0
 */
public interface IDataStore {

    /**
     * 存入指定类型的本地对象
     *
     * @param type {@link StoreType}
     * @param data 数据对象
     */
    void put(StoreType type, Object data);

    /**
     * 根据指定类型取出对象
     *
     * @param <O>  数据对象类型
     * @param type {@link StoreType}
     * @return 数据对象
     */
    <O> O get(StoreType type) throws IllegalAccessException, InstantiationException;

}
