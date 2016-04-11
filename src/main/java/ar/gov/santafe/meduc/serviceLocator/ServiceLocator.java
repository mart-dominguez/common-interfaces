package ar.gov.santafe.meduc.serviceLocator;

import java.util.ArrayList;
import java.util.List;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

public class ServiceLocator {

    private final static String url = "http://localhost:8080/cfx-rest-services/api";
    private final static List providers = new ArrayList();

    static {
        CustomJsonProvider jsonProvider = new CustomJsonProvider();
        providers.add(jsonProvider);
    }

    public static <T> T getService(Class<T> entityClass) {
        return JAXRSClientFactory.create(url, entityClass, providers, true);
    }

    public static <T> T getService(Class<T> entityClass, String baseUrl) {
        return JAXRSClientFactory.create(baseUrl, entityClass, providers, true);
    }
}
