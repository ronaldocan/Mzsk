package com.rona.drools;

import com.rona.model.User;
import org.junit.jupiter.api.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DroolsApplicationTests {

    private static KieContainer container = null;
    private KieSession statefulKieSession = null;

    @Test
    void testAgeScript() {
        KieServices kieServices = KieServices.Factory.get();
        container = kieServices.getKieClasspathContainer();
        statefulKieSession = container.newKieSession("myAgeSession");
        User user = new User(12,"can");
        statefulKieSession.insert(user);
        statefulKieSession.fireAllRules();
        statefulKieSession.dispose();
    }
}
