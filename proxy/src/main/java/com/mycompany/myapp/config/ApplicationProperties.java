package com.mycompany.myapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Proxy.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private final Kafka kafka = new Kafka();

    public Kafka getKafka() {
        return kafka;
    }

    public static class Kafka {
        private String topic;

        // Getter
        public String getTopic() {
            return topic;
        }

        // Setter
        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
    // jhipster-needle-application-properties-property

    // jhipster-needle-application-properties-property-getter

    // jhipster-needle-application-properties-property-class
}
