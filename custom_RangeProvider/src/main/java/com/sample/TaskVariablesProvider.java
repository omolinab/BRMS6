package com.sample;

import org.jbpm.formModeler.api.client.FormRenderContext;
import org.jbpm.formModeler.api.client.FormRenderContextManager;
import org.jbpm.formModeler.api.model.RangeProvider;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class TaskVariablesProvider implements RangeProvider {
    @Inject
    FormRenderContextManager formRenderContextManager;
    @Override
    public String getType() {
        System.out.println("**** TaskVariablesProvider ****"); // just to confirm if this class is registered
        return "{task_variables}"; // put this value in "Range value:" field of form modeler
    }

    @Override
    public Map<String, String> getRangesMap(String namespace) {
        FormRenderContext context = formRenderContextManager.getRootContext(namespace);
        
        Map<String, String> result = new HashMap<String, String>();

        for (String key : context.getInputData().keySet()) {
            
            // Implement this part as you like. 
            
            System.out.println("key :" + key);
            System.out.println("value : " + context.getInputData().get(key));
            
            if (key.equals("task") || key.equals("process")) continue;
            Object value = context.getInputData().get(key);

            // This map should contain <value>, <text to show> pairs of select box
            if (value != null) result.put(key, key + "-" + value.toString());
        }

        return result;
    }
}
