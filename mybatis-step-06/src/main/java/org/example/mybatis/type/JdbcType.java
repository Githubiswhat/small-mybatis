package org.example.mybatis.type;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public enum JdbcType {

    INTEGER(Types.INTEGER),
    BOOLEAN(Types.BOOLEAN),

    CHAR(Types.CHAR),
    DOUBLE(Types.DOUBLE),

    FLOAT(Types.FLOAT),

    ARRAY(Types.ARRAY),

    DATE(Types.DATE),

    DECIMAL(Types.DECIMAL);

    private int code;

    private static Map<Integer, JdbcType> codeLookup = new HashMap<>();

    static {
        for (JdbcType type : JdbcType.values()) {
            codeLookup.put(type.code, type);
        }
    }

    JdbcType(int code) {
        this.code = code;
    }

    public JdbcType forCode(int code){
        return codeLookup.get(code);
    }

}
