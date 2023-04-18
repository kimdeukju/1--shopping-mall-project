package org.spring.teamproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfigClass implements WebMvcConfigurer {

	String saveBoardFiles = "file:///C:/fileFolder/";

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**")
						.addResourceLocations(saveBoardFiles);
	}

}
