package com.example.http.message;

import com.example.domain.Person;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * 自描述消息处理
 */
public class PropertyHttpMessageConverter extends AbstractHttpMessageConverter<Person> {

    public PropertyHttpMessageConverter() {
        super(MediaType.valueOf("application/property+person"));
        setDefaultCharset(Charset.forName("UTF-8"));
    }

    @Override
    protected boolean supports(Class<?> aClass) {

        return aClass.isAssignableFrom(Person.class);
    }

    /**
     * 将请求中的内容转化为person对象
     *
     * @param aClass
     * @param httpInputMessage
     * @return
     * @throws IOException
     * @throws HttpMessageNotReadableException
     */
    @Override
    protected Person readInternal(Class<? extends Person> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream body = httpInputMessage.getBody();
        Properties properties = new Properties();
        properties.load(body);
        Person person = new Person();
        person.setId(Long.parseLong(properties.getProperty("person.id")));
        person.setName(properties.getProperty("person.name"));
        return person;
    }

    /**
     * uf
     *
     * @param person
     * @param httpOutputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Person person, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream body = httpOutputMessage.getBody();
        Properties properties = new Properties();
        properties.setProperty("person.id", String.valueOf(person.getId()));
        properties.setProperty("person.name", person.getName());
        properties.store(new OutputStreamWriter(body, getDefaultCharset()), "Written by Server");
    }
}
