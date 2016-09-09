package com.myee.tarot.cache.redisson;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import org.redisson.client.codec.Codec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

import java.io.IOException;

/**
 * Created by Martin on 2016/9/5.
 */
public class FastJsonCodec implements Codec {

    private Encoder encoder = new Encoder() {
        @Override
        public byte[] encode(Object in) throws IOException {
            return JSON.toJSONBytes(in);
        }
    };

    private Decoder<Object> decoder = new Decoder<Object>() {
        @Override
        public Object decode(ByteBuf buf, State state) throws IOException {
            return JSON.parseObject(buf.array(), Object.class);
        }
    };

    @Override
    public Decoder<Object> getMapValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getMapValueEncoder() {
        return encoder;
    }

    @Override
    public Decoder<Object> getMapKeyDecoder() {
        return decoder;
    }

    @Override
    public Encoder getMapKeyEncoder() {
        return encoder;
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return encoder;
    }
}
