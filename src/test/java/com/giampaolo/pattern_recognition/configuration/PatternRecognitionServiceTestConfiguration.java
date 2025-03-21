package com.giampaolo.pattern_recognition.configuration;

import com.giampaolo.pattern_recognition.service.PatternRecognitionService;
import com.giampaolo.pattern_recognition.service.PatternRecognitionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatternRecognitionServiceTestConfiguration {

    @Bean
    public PatternRecognitionService patternRecognitionService() {
        return new PatternRecognitionServiceImpl();
    }
}

