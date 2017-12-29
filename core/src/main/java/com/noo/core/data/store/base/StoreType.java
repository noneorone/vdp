package com.noo.core.data.store.base;

/**
 * 本地数据缓存类型定义
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/11/30<br/>
 * @since 1.0
 */
public interface StoreType {

    /**
     * 类型名称键
     *
     * @return
     */
    String getName();

    /**
     * 数据对象存储类型
     *
     * @return
     */
    Class<?> getClassType();

}
