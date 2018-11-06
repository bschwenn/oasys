package com.oasys.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

/**
 * Taken from vladmihalcea/high-performance-java-persistence on Github.
 *
 * @author Vlad Mihalcea
 */
public class JsonNodeBinaryType extends AbstractSingleColumnStandardBasicType<JsonNode> {

    public JsonNodeBinaryType() {
        super(
                JsonBinarySqlTypeDescriptor.INSTANCE,
                JsonNodeTypeDescriptor.INSTANCE
        );
    }

    public String getName() {
        return "jsonb-node";
    }
}