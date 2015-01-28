package org.jbpm.formModeler.examples.providers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import org.jbpm.formModeler.api.model.RangeProvider;

@Dependent
public class CountriesProvider implements RangeProvider {
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
        return countries;
    }
}
