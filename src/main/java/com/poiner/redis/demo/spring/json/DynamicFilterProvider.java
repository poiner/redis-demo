package com.poiner.redis.demo.spring.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import static java.util.Objects.requireNonNull;

/**
 * Dynamic Filter Provider
 *
 * @author Du Ping
 * @date 2018-09-28
 */
public class DynamicFilterProvider extends SimpleFilterProvider implements PropertyFilter {

    static final String FILTER_ID = "DynamicFilterProvider$FILTER";
    private static final long serialVersionUID = -362486406389944351L;
    private static ThreadLocal<CustomJsonSerializer> customJsonSerializerThreadLocal = new ThreadLocal<>();
    private final transient PropertyFilter delegate;

    /**
     * Construct a {@code PropertyFilterHolder} that delegate
     * {@link SimpleBeanPropertyFilter#serializeAll()}
     */
    DynamicFilterProvider() {
        this(SimpleBeanPropertyFilter.serializeAll());
    }

    /**
     * Construct a {@code PropertyFilterHolder} for given {@code delegate}
     *
     * @param delegate PropertyFilter
     * @throws NullPointerException if {@code delegate} is {@code null}
     */
    private DynamicFilterProvider(PropertyFilter delegate) {
        this.delegate = requireNonNull(delegate);
        addFilter(FILTER_ID, this);
    }

    public static void setSerializer(CustomJsonSerializer customJsonSerializer) {
        customJsonSerializerThreadLocal.remove();
        customJsonSerializerThreadLocal.set(customJsonSerializer);
    }

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen, SerializerProvider prov,
                                 PropertyWriter writer) throws Exception {

        // 避免pojo为null时，抛出Exception
        if (customJsonSerializerThreadLocal.get() != null && pojo != null) {
            if (customJsonSerializerThreadLocal.get().apply(pojo.getClass(), writer.getName())) {
                writer.serializeAsField(pojo, jgen, prov);
            }
        } else {
            writer.serializeAsField(pojo, jgen, prov);
        }
    }

    @Override
    public void serializeAsElement(Object elementValue, JsonGenerator jgen, SerializerProvider prov,
                                   PropertyWriter writer) throws Exception {
        delegate.serializeAsElement(elementValue, jgen, prov, writer);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void depositSchemaProperty(PropertyWriter writer, ObjectNode propertiesNode,
                                      SerializerProvider provider) {
        // 已经过时，只是为了实现接口，并无代码编写
    }

    @Override
    public void depositSchemaProperty(PropertyWriter writer, JsonObjectFormatVisitor objectVisitor,
                                      SerializerProvider provider) throws JsonMappingException {
        delegate.depositSchemaProperty(writer, objectVisitor, provider);
    }

}
