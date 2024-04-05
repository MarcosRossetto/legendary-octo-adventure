package org.acme.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import java.util.ResourceBundle;

@ApplicationScoped
public class ResourceBundleProducer {

    @Produces
    @Named("messages")
    public ResourceBundle produceResourceBundle() {
        return ResourceBundle.getBundle("messages");
    }
}
