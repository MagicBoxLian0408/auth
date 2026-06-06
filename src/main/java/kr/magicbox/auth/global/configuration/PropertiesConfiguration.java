package kr.magicbox.auth.global.configuration;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.converter.RsaKeyConverters;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@ConfigurationPropertiesScan(basePackages = "kr.magicbox.auth")
public class PropertiesConfiguration {

    @Bean
    @ConfigurationPropertiesBinding
    public RsaPrivateKeyConverter rsaPrivateKeyConverter() {
        return new RsaPrivateKeyConverter();
    }

    @Bean
    @ConfigurationPropertiesBinding
    public RsaPublicKeyConverter rsaPublicKeyConverter() {
        return new RsaPublicKeyConverter();
    }

    static class RsaPrivateKeyConverter implements Converter<String, RSAPrivateKey> {
        @Override
        public RSAPrivateKey convert(String source) {
            return RsaKeyConverters.pkcs8().convert(
                    new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
        }
    }

    static class RsaPublicKeyConverter implements Converter<String, RSAPublicKey> {
        @Override
        public RSAPublicKey convert(String source) {
            return RsaKeyConverters.x509().convert(
                    new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
        }
    }
}