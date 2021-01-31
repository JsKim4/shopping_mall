package me.kjs.mall.configs;

import org.hibernate.dialect.SQLServerDialect;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
public class SQLServerDBDialect extends SQLServerDialect {

    public SQLServerDBDialect() {
        super();
        this.registerColumnType(Types.VARCHAR, "nvarchar($l)");
    }
}