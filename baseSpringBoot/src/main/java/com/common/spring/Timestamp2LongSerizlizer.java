package com.common.spring;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

/**
 * json 转 long
 * @author jianghaoming
 * @date 2018-11-26 14:03:59
 */
public class Timestamp2LongSerizlizer extends JsonSerializer<Timestamp> {
    @Override
    public void serialize(Timestamp timestamp, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(timestamp.getTime());
    }
}
