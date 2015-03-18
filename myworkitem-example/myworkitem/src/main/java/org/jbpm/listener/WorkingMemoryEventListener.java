package org.jbpm.listener;

import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WorkingMemoryEventListener extends DefaultRuleRuntimeEventListener {

    private static final String lineSep = System.getProperty("line.separator");
    private static final Logger log = LoggerFactory.getLogger(org.jbpm.listener.WorkingMemoryEventListener.class);

    public WorkingMemoryEventListener() {}

    public void objectUpdated(ObjectUpdatedEvent event)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("The rule '%s' updated following object: \n", new Object[] {
            event.getPropagationContext().getRule().getName()
        }));
        builder.append(lineSep);
        builder.append("From: ");
        builder.append(lineSep);
        builder.append("To: ");
        builder.append(lineSep);
        log.info(builder.toString());
    }

}
