import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.example.config.SpringWebmvcConfig;

/**
 * 功能描述
 *
 * @author TomLuo
 * @date 2019/7/29
 */
public class AutoConfigDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringWebmvcConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/*"};
    }
}
