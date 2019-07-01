package cn.onlov.evaluate.shiro;

import java.io.IOException;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.jagregory.shiro.freemarker.ShiroTags;

import freemarker.template.TemplateException;

public class MyFreeMarkerConfiger extends FreeMarkerConfigurer{
	
	@Override  
	public void afterPropertiesSet() throws IOException, TemplateException {  
        super.afterPropertiesSet();
        freemarker.template.Configuration cfg = this.getConfiguration();
        cfg.setSharedVariable("shiro", new ShiroTags());//shiro标签
    }  
	
}
