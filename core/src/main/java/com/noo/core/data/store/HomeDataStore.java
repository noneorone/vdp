package com.noo.core.data.store;

import com.noo.core.data.store.base.DataStore;
import com.noo.core.data.store.base.StoreType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 功能说明：主页数据存储<br/>
 *
 * @author Mars.Wong(noneorone@yeah.net) at 2017/11/30<br/>
 * @since 1.0
 */
public class HomeDataStore extends DataStore {

    public enum Type implements StoreType {
        ALL(HashMap.class),
        GRID(HashMap.class),
        DYNAMIC(HashMap.class),

        REPORT_ITEM(ArrayList.class),
        DYNAMIC_REPORT(HashMap.class),
        DYNAMIC_NEWEST(HashMap.class),
        HOT_APP(ArrayList.class);

        private Class<?> classType;

        Type(Class<?> classType) {
            this.classType = classType;
        }

        @Override
        public Class<?> getClassType() {
            return classType;
        }

        @Override
        public String getName() {
            return this.name();
        }
    }

    private static final HomeDataStore dataStore = new HomeDataStore();

    public static HomeDataStore instance() {
        return dataStore;
    }

    private HomeDataStore() {
    }

}
