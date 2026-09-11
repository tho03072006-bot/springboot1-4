package vn.iotstar.config;
import org.springframework.context.annotation.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import jakarta.servlet.DispatcherType;
@Configuration
public class WebConfig {
    @Bean FilterRegistrationBean<org.springframework.web.filter.CharacterEncodingFilter> utf8() {
        var r=new FilterRegistrationBean<>(new org.springframework.web.filter.CharacterEncodingFilter("UTF-8",true));
        r.setOrder(Integer.MIN_VALUE); r.addUrlPatterns("/*"); return r;
    }
    @Bean InternalResourceViewResolver jspViewResolver() {
        var r=new InternalResourceViewResolver(); r.setPrefix("/WEB-INF/views/"); r.setSuffix(".jsp");
        r.setContentType("text/html;charset=UTF-8"); r.setViewClass(JstlView.class); r.setAlwaysInclude(true); r.setOrder(0); return r;
    }
    @Bean FilterRegistrationBean<TomcatSiteMeshFilter> siteMesh() {
        var r=new FilterRegistrationBean<>(new TomcatSiteMeshFilter()); r.setName("sitemesh");
        r.addUrlPatterns("/*"); r.setDispatcherTypes(DispatcherType.REQUEST); r.setOrder(10); return r;
    }
}
