package com.sample.tests;

import java.util.ArrayList;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class DrlCollect {
	public static final void main(String[] args) {
		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase
					.newStatefulKnowledgeSession();
			KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
					.newFileLogger(ksession, "test");
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

			ksession.insert(customer);
			ksession.insert(customer2);
			ksession.fireAllRules();
			logger.close();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("DrlCollect.drl"),
				ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}

