package top.wangjingxin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.wangjingxin.main.ControllerAndView;

/**
 * Created by 王镜鑫 on 2017/2/22 15:13.
 */
@Configuration
@ComponentScan
public class Launch {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Launch.class);
        applicationContext.getBean(ControllerAndView.class).launch();
    }
}
