package kr.magicbox.auth.global.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.converter.RsaKeyConverters;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@ConfigurationPropertiesScan(basePackages = "kr.magicbox.auth")
public class PropertiesConfiguration {

    @Bean
    @ConfigurationPropertiesBinding
    public Converter<String, RSAPrivateKey> rsaPrivateKeyConverter() {
        return RsaKeyConverters.pkcs8()::convert;
    }

    @Bean
    @ConfigurationPropertiesBinding
    public Converter<String, RSAPublicKey> rsaPublicKeyConverter() {
        return RsaKeyConverters.x509()::convert;
    }
}