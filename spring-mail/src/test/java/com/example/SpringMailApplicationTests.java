package com.example;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringMailApplication.class)
@WebAppConfiguration
public class SpringMailApplicationTests {
	private MediaType contentType = new MediaType(
			"application", "hal+json");

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
				hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}
	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

	}
	@Test
	public void contextLoads() {

	}
	@Test
	public void userNotFound() throws Exception {

		String host="smtp.163.com";
		Integer port=25;
		String userName="beauty2001@163.com";
		String password="";
		String defaultEncoding="UTF-8";

		MailServerVO newMailServer=new MailServerVO(host,
				 port,
				 userName,
				 password,
				 defaultEncoding,
				null);

		mockMvc.perform(post("/mail/server")
				.content(this.json(newMailServer))
				.contentType(contentType))
				.andExpect(status().isOk()).andDo(print());;
		mockMvc.perform(get("/mail/server"))
				.andExpect(status().isOk()).andDo(print());
		String subject="Tomluo test email";
		String plainText="hello, tomLuo";
		String htmlText="<P>hello, tomLuo:</P><P>I see your baby</P><P>Regards,</p>TomLuo";
		String charset="utf-8";
		boolean multipart=true;
		AddressVO[] to=new AddressVO[]{new AddressVO("21429503@qq.com","tom")};
		AddressVO[] cc=null;
		AddressVO[] bcc=null;
		AddressVO from=new AddressVO("beauty2001@163.com","Miss Luo");
		AddressVO replyToe=null;
		MailVO mail=new MailVO(subject,plainText,htmlText,charset,multipart,to,cc,bcc,from,replyToe);
		mockMvc.perform(post("/mail")
				.content(this.json(mail))
				.contentType(contentType))
				.andExpect(status().isOk()).andDo(print());
	}
	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(
				o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
