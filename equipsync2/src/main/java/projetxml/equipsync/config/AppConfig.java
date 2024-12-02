package projetxml.equipsync.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import projetxml.equipsync.Services.XmlService;
import projetxml.equipsync.entities.Equipment;

@Configuration
public class AppConfig {
    @Bean
    public XmlService<Equipment> equipmentXmlService() {
        return new XmlService<>(Equipment.class);
    }
}

