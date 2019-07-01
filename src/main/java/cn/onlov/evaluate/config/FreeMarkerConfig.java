package cn.onlov.evaluate.config;

import cn.onlov.evaluate.shiro.MyFreeMarkerConfiger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreeMarkerConfig{
	
    @Value("${spring.freemarker.template-loader-path}")
    private String templateLoaderPath;
    
	@Bean
	public FreeMarkerConfigurer FreeMarkerConfigurer(){
		MyFreeMarkerConfiger freeMarkerConfiger = new MyFreeMarkerConfiger();
		freeMarkerConfiger.setTemplateLoaderPath(templateLoaderPath);
		return freeMarkerConfiger;
	}
	
}
