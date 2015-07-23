package org.jbpm.formModeler.examples.providers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jbpm.formModeler.api.client.FormRenderContext;
import org.jbpm.formModeler.api.client.FormRenderContextManager;
import org.jbpm.formModeler.api.model.RangeProvider;

@Dependent
public class CountriesProvider implements RangeProvider {

    @Inject
    FormRenderContextManager formRenderContextManager;

	Map<String, String> countries = new HashMap<String, String>(  );

    @PostConstruct
    protected void init() {
        countries.put( "es", "Spain" );
        countries.put( "it", "Italy" );
        countries.put( "pt", "Portugal" );
    }

    @Override
    public String getType() {
        return "Countries";
    }

    @Override
    public Map<String, String> getRangesMap( String s ) {
        /*System.out.println("s: " + s);
        FormRenderContext rootcontext = formRenderContextManager.getRootContext(s);
        System.out.println("rootcontext: " + rootcontext);
        for (String key : rootcontext.getInputData().keySet()) {
            System.out.println("key :" + key);
            System.out.println("value : " + rootcontext.getInputData().get(key));
        }
        FormRenderContext formcontext = formRenderContextManager.getFormRenderContext(s);
        System.out.println("formcontext: " + formcontext);
        for (String key : formcontext.getInputData().keySet()) {
            System.out.println("key :" + key);
            System.out.println("value : " + formcontext.getInputData().get(key));
        }
*/
        return countries;
    }
}
