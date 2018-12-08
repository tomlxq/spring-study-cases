import com.alibaba.fastjson.JSON;
import com.tom.demo.entity.Account;
import com.tom.demo.javax.CustomerConfig;
import com.tom.demo.jdbc.framework.QueryRule;
import com.tom.demo.jdbc.framework.QueryRuleSqlBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CustomerConfig.class})
public class TestMain {
    private static Properties pro;

    @Autowired
    ConfigurableApplicationContext context;
 /*try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class, DBProperties.class);) {
        DBProperties dbProperties = context.getBean(DBProperties.class);
        logger.info("This is dbProperties: " + dbProperties.toString());
    }*/

    public static void printProperties(Properties p) {
        Iterator it = p.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry ent = (Map.Entry) it.next();
            System.out.println(ent.getKey() + " : " + ent.getValue());
        }
    }

    /**
     * 构造properties文件
     *
     * @param path
     * @throws IOException
     */
    public static Properties buildProperty(String path) throws IOException {

        InputStream is = getResourceAsStream("afei.properties");
        pro = new Properties();
        pro.load(is);
        return pro;
    }

    /**
     * 构造properties文件的流
     *
     * @param resource
     * @return
     */
    public static InputStream getResourceAsStream(String resource) {
        String stripped = resource.startsWith("/") ?
                resource.substring(1) : resource;

        InputStream stream = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            stream = classLoader.getResourceAsStream(stripped);
        }
        if (stream == null) {
            stream = TestMain.class.getResourceAsStream(resource);
        }
        if (stream == null) {
            stream = TestMain.class.getClassLoader().getResourceAsStream(stripped);
        }
        if (stream == null) {
            throw new RuntimeException(resource + " not found");
        }
        return stream;
    }

    @Test
    public void testRead() {
        // System.out.println(removeFirstAnd("and name=zhangshan and age=18"));
        System.out.println(CustomerConfig.getValue("jdbc.username"));
        QueryRule instance = QueryRule.getInstance();
        instance.andEqual("name", "tom");
        instance.andBetween("age", 18, 60);
        instance.addLike("addr", "%四川%");
        instance.orIn("phone", "133800138000", "110");
        System.out.println(JSON.toJSONString(instance));
        QueryRuleSqlBuilder queryRuleSqlBuilder = new QueryRuleSqlBuilder(instance);
        System.out.println(queryRuleSqlBuilder.getWhereSql());
        System.out.println(queryRuleSqlBuilder.getOrderSql());
        System.out.println(JSON.toJSON(queryRuleSqlBuilder.getValueArr()));

       /* Type genericSuperclass = AccountDao.class.getGenericSuperclass();
        System.out.println(genericSuperclass.getTypeName());
        System.out.println(genericSuperclass instanceof ParameterizedType);
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        Arrays.asList(actualTypeArguments).forEach(type->{
            System.out.println(type.getTypeName());
        });
        int idx =0;
        if(actualTypeArguments[idx] instanceof Class){
            System.out.println(actualTypeArguments[idx]);
        }*/
    }

    @Test
    public void testRead1() {
        Class<Account> clazz = Account.class;
        Table table = clazz.getAnnotation(Table.class);
        if (null != table) {
            System.out.println(table.name());

        } else {
            System.out.println(clazz.getSimpleName());
        }
    }

}
