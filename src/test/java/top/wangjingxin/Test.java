package top.wangjingxin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.wangjingxin.main.ControllerAndView;

/**
 * Created by 王镜鑫 on 2017/2/23 20:44.
 */
public class Test {
    @org.junit.Test
    public void aa(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Launch.class);
        applicationContext.getBean(ControllerAndView.class).launch();
    }
}
