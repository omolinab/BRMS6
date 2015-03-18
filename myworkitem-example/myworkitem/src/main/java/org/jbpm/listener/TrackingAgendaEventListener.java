package org.jbpm.listener;

import java.util.Iterator;

import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.BeforeMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.event.rule.RuleFlowGroupActivatedEvent;
import org.kie.api.event.rule.RuleFlowGroupDeactivatedEvent;
import org.kie.api.runtime.rule.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackingAgendaEventListener extends DefaultAgendaEventListener {

    private static final Logger log = LoggerFactory.getLogger(org.jbpm.listener.TrackingAgendaEventListener.class);
    private static final String lineSep = System.getProperty("line.separator");
    private static final String after = "Facts who actually caused the rule to fire (after invocation):";
    private static final String before = "Facts who actually caused the rule to fire (before invocation):";
    private static final String isFired = "Rule '%s' will be fired";
    private static final String wasFired = "Rule '%s' was fired";

    public TrackingAgendaEventListener()
    {
    }

    public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event)
    {
        log.debug(String.format("Deactivated ruleflow-group '%s'", new Object[] {
            event.getRuleFlowGroup().getName()
        }));
    }

    public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event)
    {
        log.debug(String.format("Activated ruleflow-group '%s'", new Object[] {
            event.getRuleFlowGroup().getName()
        }));
    }

    public void beforeMatchFired(BeforeMatchFiredEvent event)
    {
        printRuleEvent(event.getMatch(), "Facts who actually caused the rule to fire (before invocation):", "Rule '%s' will be fired");
    }

    public void afterMatchFired(AfterMatchFiredEvent event)
    {
        printRuleEvent(event.getMatch(), "Facts who actually caused the rule to fire (after invocation):", "Rule '%s' was fired");
    }

    private static void printRuleEvent(Match match, String cause, String ruleFire)
    {
        StringBuilder builder = new StringBuilder();
        Rule rule = match.getRule();
        builder.append(String.format(ruleFire, new Object[] {
            rule.getName()
        }));
        builder.append(lineSep);
        builder.append(cause);
        builder.append(lineSep);
        for(Iterator iterator = match.getObjects().iterator(); iterator.hasNext(); builder.append(lineSep))
        {
            Object tupleObjects = iterator.next();
            builder.append("ObjectDump.dump(tupleObjects)");
        }

        log.debug(builder.toString());
    }

}
