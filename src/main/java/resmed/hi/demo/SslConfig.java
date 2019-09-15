package resmed.hi.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;

@ConditionalOnProperty("service.cacertificate")
@Configuration
@Slf4j
public class SslConfig {

    public SslConfig(HttpClientProperties properties, @Value("${service.cacertificate}") String cert) throws Exception {
        File tmp = File.createTempFile("sg_", "");
        try (PrintWriter writer = new PrintWriter(tmp)) {
            writer.println(cert);
        }
        properties.getSsl().setTrustedX509Certificates(Arrays.asList(tmp.getAbsolutePath()));
    }

    @Bean
    public SSLContext sslContext(TrustManagerFactory tmf) throws Exception {
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, tmf.getTrustManagers(), new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes()));
        SSLContext.setDefault(context);
        log.info("===> Created SSL Context");
        return context;
    }

    @Bean
    public TrustManagerFactory trustManagerFactory(@Value("${service.cacertificate}") String cert) throws Exception {
        log.info("Creating the certificate from [{}]", cert);
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore(certificate(cert)));
        log.info("===> Created TrustManagerFactory");
        return factory;
    }

    private KeyStore keystore(X509Certificate cert) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, "".toCharArray());
        ks.setCertificateEntry("cacerts", cert);
        return ks;
    }

    private X509Certificate certificate(String cert) throws Exception {
        cert = cert.replaceAll("\\n", "")
            .replace("-----BEGIN CERTIFICATE-----", "")
            .replace("-----END CERTIFICATE-----", "");

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(Base64.getDecoder().decode(cert)));
    }

}
