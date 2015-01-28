package org.jbpm.formModeler.examples.providers;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import org.jbpm.formModeler.extended.combos.DependentComboValuesProvider;

@Dependent
public class RegionsProvider implements DependentComboValuesProvider {
    Map<String, Map<String, String>> regions = new HashMap<String, Map<String, String>>(  );

    @PostConstruct
    protected void init() {
        Map<String, String> country = new HashMap<String, String>(  );
        country.put( "md", "Madrid" );
        country.put( "ca", "Catalunya" );
        country.put( "eus", "Euskadi" );
        regions.put( "es", country );

        country = new HashMap<String, String>(  );

        country.put( "sa", "Sardinia" );
        country.put( "tu", "Tuscany" );
        country.put( "la", "Lazio" );
        regions.put( "it", country );

        country = new HashMap<String, String>(  );

        country.put( "av", "Aveiro" );
        country.put( "st", "Setubal" );
        country.put( "pt", "Portalegre" );
        regions.put( "pt", country );
    }

    @Override
    public String getId() {
        return "Regions";
    }

    @Override
    public Map<String, String> getValues( String rootValue, String locale ) {
        return regions.get( rootValue );
    }
}
