package org.example.mybatis.type;

import java.sql.Types;
import java.util.HashMap;

public enum JdbcType {

    INTEGER(Types.INTEGER),

    FLOAT(Types.FLOAT),

    DOUBLE(Types.DOUBLE),

    DECIMAL(Types.DECIMAL),

    VARCHAR(Types.VARCHAR),

    TIMESTAMP(Types.TIMESTAMP);


    private final int TYPE_CODE;

    private static HashMap<Integer, JdbcType> codeLookup = new HashMap<>();

    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.TYPE_CODE, type);
        }
    }

    JdbcType(int code) {
        this.TYPE_CODE = code;
    }

    public static JdbcType forCode(int code) {
        return codeLookup.get(code);
    }

}
