package org.drools.bug.leak;

import java.util.regex.Pattern;

import org.drools.core.impl.KnowledgeBaseFactoryServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.conf.RuleEngineOption;

public class LeakTest {

	protected KieBase kieBase;
    private KieSession kSession;
    private static final int NUMBER_OF_FACTS = 777;

    @Before
    public void prepare() {
		System.setProperty(RuleEngineOption.PROPERTY_NAME, RuleEngineOption.PHREAK.name());
		System.setProperty(EventProcessingOption.PROPERTY_NAME, EventProcessingOption.STREAM.getMode());
		System.setProperty("drools.dateformat", "dd/MM/yyyy HH:mm");

		KieBaseConfiguration KBaseConfig = new KnowledgeBaseFactoryServiceImpl().newKnowledgeBaseConfiguration();
		kieBase = KieServices.Factory.get().getKieClasspathContainer().newKieBase(KBaseConfig);
        kSession = kieBase.newKieSession();
	}

    /*@Test
	public void testNoExpire() {
        String regExPattern = ".*" + Integer.toString(NUMBER_OF_FACTS) + "(\\s+\\d+\\s+)" + SimpleFactWithoutExpire.class.getName() + ".*";
        for (int i = 0; i < NUMBER_OF_FACTS; i++) {
            SimpleFactWithoutExpire simpleFactWithExpire = new SimpleFactWithoutExpire(i, System.currentTimeMillis());
            FactHandle handle = kSession.insert(simpleFactWithExpire);
        }

        kSession.fireAllRules();

        Assert.assertTrue(kSession.getFactCount() == 0);
        String histo = HeapHisto.getHisto();

        //Assert.assertTrue(!histo.contains(SimpleFactWithoutExpire.class.getName()));
        Assert.assertTrue(!Pattern.compile(regExPattern, Pattern.DOTALL).matcher(histo).matches());
	}*/

    @Test
    public void testWithExpire() {
        String regExPattern = ".*" + Integer.toString(NUMBER_OF_FACTS) + "(\\s+\\d+\\s+)" + SimpleFactWithExpire.class.getName() + ".*";
        for (int i = 0; i < NUMBER_OF_FACTS; i++) {
            SimpleFactWithExpire simpleFactWithExpire = new SimpleFactWithExpire(i, System.currentTimeMillis());
            FactHandle handle = kSession.insert(simpleFactWithExpire);
        }

        kSession.fireAllRules();
        
        try {
            System.out.println("testWithExpire - Sleeping to let you check the heap using jvisualvm");        
            Thread.sleep(1*15*1000);
        } catch(Exception ex) {
        }

        Assert.assertTrue(kSession.getFactCount() == 0);
        String histo = HeapHisto.getHisto();
        
        System.out.println("PRE dispose");        
        kSession.dispose();
        System.out.println("POST dispose");        

        /*try {
            System.out.println("2testWithExpire - Sleeping to let you check the heap using jvisualvm");        
            Thread.sleep(3*60*1000);
        } catch(Exception ex) {
        }*/
        
        if(Pattern.compile(regExPattern, Pattern.DOTALL).matcher(histo).matches()) {
            System.out.println(histo);
            Assert.fail("Leak ... SimpleFactWithExpire should have been evacuated from heap");
            // We also have 1000
            //  org.drools.core.time.impl.JDKTimerService$JDKJobHandle
            //  org.drools.core.time.impl.DefaultTimerJobInstance
            //  and org.drools.core.reteoo.ObjectTypeNode$ExpireJobContext
            //  ...
        }
    }

    @After
    public void shutdown() {
        kSession.dispose();
        kSession.destroy();
    }
}
