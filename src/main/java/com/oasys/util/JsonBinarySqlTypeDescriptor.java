package com.oasys.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.*;

/**
 * Adapted from vladmihalcea/high-performance-java-persistence on Github.
 *
 * @author Vlad Mihalcea
 */
public class JsonBinarySqlTypeDescriptor implements SqlTypeDescriptor {

    public static final JsonBinarySqlTypeDescriptor INSTANCE = new JsonBinarySqlTypeDescriptor();

    @Override
    public <X> ValueBinder<X> getBinder(
            final JavaTypeDescriptor<X> javaTypeDescriptor
    ) {
        return new BasicBinder<X>(javaTypeDescriptor, this) {
            @Override
            protected void doBind(
                    PreparedStatement st,
                    X value,
                    int index,
                    WrapperOptions options
            ) throws SQLException {
                st.setObject(
                        index,
                        javaTypeDescriptor.unwrap(
                                value,
                                JsonNode.class,
                                options
                        ),
                        getSqlType()
                );
            }

            @Override
            protected void doBind(
                    CallableStatement st,
                    X value,
                    String name,
                    WrapperOptions options
            ) throws SQLException {
                st.setObject(
                        name,
                        javaTypeDescriptor.unwrap(
                                value,
                                JsonNode.class,
                                options
                        ),
                        getSqlType()
                );
            }
        };
    }

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }

    @Override
    public boolean canBeRemapped() {
        return true;
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor) {
        return new BasicExtractor<X>(javaTypeDescriptor, this) {
            @Override
            protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(rs.getObject(name), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, int index, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getObject(index), options);
            }

            @Override
            protected X doExtract(CallableStatement statement, String name, WrapperOptions options) throws SQLException {
                return javaTypeDescriptor.wrap(statement.getObject(name), options);
            }
        };
    }
}
