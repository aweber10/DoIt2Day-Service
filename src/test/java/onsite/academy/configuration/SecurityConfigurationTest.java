package onsite.academy.configuration;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.servlet.ControllerEndpointHandlerMapping;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigurationTest {

    private static final String[] PUBLIC_ENDPOINTS = {
            "actuator",
            "swagger-ui",
            "v3/api-docs"
    };

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @Test
    void assertEndpoints() {
        context.getBean(ControllerEndpointHandlerMapping.class).getHandlerMethods()
                .forEach((requestMappingInfo, handlerMethod) -> testEndpoints(requestMappingInfo));
    }

    private void testEndpoints(final RequestMappingInfo info) {
        for (String endpoint : info.getDirectPaths()) {
            String correctEndpoint = endpoint.replaceAll("\\{\\w+?}", "STUB_PATH_PARAM");
            if (StringUtils.containsAny(endpoint, PUBLIC_ENDPOINTS)) {
                info.getMethodsCondition().getMethods().forEach(method -> assertPublic(correctEndpoint, method));
            } else {
                info.getMethodsCondition().getMethods().forEach(method -> assertSecured(correctEndpoint, method));
            }
        }
    }

    @SneakyThrows
    private void assertPublic(final String endpoint, final RequestMethod method) {
        int status = mvc.perform(MockMvcRequestBuilders.request(HttpMethod.valueOf(method.name()), endpoint))
                .andReturn().getResponse().getStatus();
        HttpStatus httpStatus = HttpStatus.resolve(status);
        assertTrue(httpStatus.is2xxSuccessful() || httpStatus.is3xxRedirection());
    }

    @SneakyThrows
    private void assertSecured(final String endpoint, final RequestMethod method) {
        mvc.perform(MockMvcRequestBuilders.request(HttpMethod.valueOf(method.name()), endpoint))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}
