package com.quochuy.onixshop.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dr0rjwfoc",  
                "api_key", "548176532233972",
                "api_secret", "CL7BFsiztUv4lr_Ybl2L4P7WiPQ"
        ));
    }
}
