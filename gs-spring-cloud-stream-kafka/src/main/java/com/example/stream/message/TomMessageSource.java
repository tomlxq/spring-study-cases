package com.example.stream.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/6
 */
public interface TomMessageSource {

    /**
     * Name of the output channel.
     */
    String OUTPUT = "tomOutput";

    /**
     * @return output channel
     */
    @Output(OUTPUT)
    MessageChannel tomOutput();

}