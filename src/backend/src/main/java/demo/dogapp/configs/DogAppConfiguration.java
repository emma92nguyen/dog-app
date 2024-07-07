package demo.dogapp.configs;

import demo.dogapp.interceptors.DogAppRequestLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DogAppConfiguration implements WebMvcConfigurer {

    @Autowired
    private DogAppRequestLoggingInterceptor dogAppRequestLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dogAppRequestLoggingInterceptor).addPathPatterns("/**");
    }
}
