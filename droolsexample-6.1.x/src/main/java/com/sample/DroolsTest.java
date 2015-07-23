package com.sample;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;

import com.sample.tests.Coverage;
import com.sample.tests.Customer;
import com.sample.tests.Insured;
import com.sample.tests.Policy;
import com.sample.tests.PolicyProduct;
import com.sample.tests.PolicyRuleResult;
import com.sample.tests.PrimeTransaction;
import com.sample.tests.Reconcilation;
import com.sample.tests.Stocks;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    public static final void main(String[] args) {
    	//runHelloWorld();
    	//runCollectListExample();
    	runIssueTest();
    	//runCase01475814();
    }

    public static void runCase01475814() {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

            // go !
        	PrimeTransaction pt = new PrimeTransaction("joseph", new Date());
        	Reconcilation rec = new Reconcilation("reconcilation1", pt, new Date());
 
            kSession.insert(pt);
            kSession.insert(rec);
            kSession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void runIssueTest() {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

            // go !
            Policy policy = new Policy();
            policy.addAgent("Bill Smith");
            Coverage coverage1 = new Coverage();
            coverage1.setEligible(true);
            coverage1.setFaceAmount(250000);
 
            Coverage coverage2 = new Coverage();
            coverage2.setEligible(true);
            coverage2.setFaceAmount(1500000);
 
            policy.addCoverage(coverage1);
            policy.addCoverage(coverage2);
            policy.setIssueState("CO");
 
            Insured insured1 = new Insured();
            insured1.setFirstName("Elizabeth");
            insured1.setAge(30);
            insured1.setGender(Insured.GENDER_FEMALE);
            coverage1.setInsured(insured1);
 
            Insured insured2 = new Insured();
            insured2.setFirstName("Elizabeth");
            insured2.setAge(45);
            insured2.setGender(Insured.GENDER_FEMALE);
            coverage2.setInsured(insured2);
 
            PolicyRuleResult result = new PolicyRuleResult();
            PolicyProduct policyProd = new PolicyProduct();
 
            kSession.insert(policy);
            kSession.insert(policyProd);
            kSession.setGlobal("result", result);
            kSession.fireAllRules();

            System.out.println("Eligible: " + result.isEligible());
            System.out.println("AllCoveragesAreEligible: " + result.isAllCoveragesAreEligible());
            System.out.println("AgentInformationValid: " + result.isAgentInformationValid());
            System.out.println("ApprovedState: " + result.isApprovedState());
            System.out.println("SoundMessages: " + result.getSoundMessages());
            System.out.println("SoundexWorksCollect: " + result.isSoundexWorksCollect());
 
            System.out.println("Cov1: " + coverage1.isEligible());
            System.out.println("Cov2: " + coverage2.isEligible());
 
            System.out.println("Ret Id: " + result.isRetIdentifier());
            System.out.println("Total Face Amount: " + result.getTotalFaceAmount());
            System.out.println("Total Face Amount Simp: " + result.getTotalFaceAmountSimp());
            System.out.println("Ave. Face Amount: " + result.getAverageFaceAmount());
 
            //Query Results
            /*QueryResults results = kSession.getQueryResults("Get Insured details for the policy");
            System.out.println( "we have " + results.size() + " insured(s)" );
 
            for ( Iterator it = results.iterator(); it.hasNext(); ) {
                QueryResult qResult = (QueryResult)it.next();
                Insured insured = (Insured) qResult.get( "$insured" );
                System.out.println( insured.getFirstName() + "\n" );
            }*/
        
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void runCollectListExample() {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

            // go !
			List<Stocks> stocks = new ArrayList<Stocks>();
			stocks.add(new Stocks("Apple", 10, 100));
			stocks.add(new Stocks("Google", 35, 28));
			stocks.add(new Stocks("Larsen", 100, 780));
			stocks.add(new Stocks("TCS", 180, 1100));
			stocks.add(new Stocks("Maruti", 160, 200));

			Customer customer = new Customer();
			customer.setStocks(stocks);
			customer.setCustId("C1500564");


			List<Stocks> stocks2 = new ArrayList<Stocks>();
			stocks2.add(new Stocks("Apple", 10, 100));
			stocks2.add(new Stocks("Google", 35, 28));
			stocks2.add(new Stocks("Larsen", 100, 780));
			stocks2.add(new Stocks("TCS", 180, 1100));
			stocks2.add(new Stocks("Maruti", 160, 200));
			stocks2.add(new Stocks("Bhel", 60, 80));
			stocks2.add(new Stocks("NTPC", 160, 40));

			Customer customer2 = new Customer();
			customer2.setStocks(stocks2);
			customer2.setCustId("C1506585");

			kSession.insert(customer);
			kSession.insert(customer2);
            kSession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void runHelloWorld() {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");

            // go !
            Message message = new Message();
            message.setMessage("Hello World");
            message.setStatus(Message.HELLO);
            kSession.insert(message);
            kSession.fireAllRules();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static class Message {

        public static final int HELLO = 0;
        public static final int GOODBYE = 1;

        private String message;

        private int status;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

    }

}
