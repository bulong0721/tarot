package com.myee.tarot.core.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class FastJsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> {
    private static final Charset             DEFAULT_CHARSET   = Charset.forName("UTF-8");
    private              SerializerFeature[] serializerFeature = {SerializerFeature.DisableCircularReferenceDetect};

    public FastJsonHttpMessageConverter() {
    }

    public SerializerFeature[] getSerializerFeature() {
        return serializerFeature;
    }

    public void setSerializerFeature(SerializerFeature[] serializerFeature) {
        this.serializerFeature = serializerFeature;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // should not be called, since we override canRead/Write instead
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        long contentLength = inputMessage.getHeaders().getContentLength();
        ByteArrayOutputStream baos =
                new ByteArrayOutputStream(contentLength >= 0 ? (int) contentLength : StreamUtils.BUFFER_SIZE);
        StreamUtils.copy(inputMessage.getBody(), baos);
        String requestBody = baos.toString("utf-8");
        if (clazz.equals(String.class)) {
            return requestBody;
        }
        if (requestBody.startsWith("[")) {
            return JSON.parseArray(requestBody, clazz);
        }
        return JSON.parseObject(requestBody, clazz);
    }

    @Override
    protected void writeInternal(Object t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        String jsonString = JSON.toJSONString(t, serializerFeature);
        StreamUtils.copy(jsonString, DEFAULT_CHARSET, outputMessage.getBody());
    }

}
