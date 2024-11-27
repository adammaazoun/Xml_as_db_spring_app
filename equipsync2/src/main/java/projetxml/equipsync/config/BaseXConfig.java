package projetxml.equipsync.config;

import org.basex.api.client.ClientSession;
import org.basex.core.BaseXException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class BaseXConfig {
    @Value("${basex.host:localhost}")
    private String host;

    @Value("${basex.port:1984}")
    private int port;

    @Value("${basex.username:admin}")
    private String username;

    @Value("${basex.password:1234}")
    private String password;

    @Bean
    public ClientSession baseXSession() throws IOException {
        return new ClientSession(host, port, username, password);
    }
}

