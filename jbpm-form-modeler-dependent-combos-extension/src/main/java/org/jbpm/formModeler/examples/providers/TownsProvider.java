package org.jbpm.formModeler.examples.providers;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import org.jbpm.formModeler.extended.combos.DependentComboValuesProvider;

@Dependent
public class TownsProvider implements DependentComboValuesProvider {
    Map<String, Map<String, String>> towns = new HashMap<String, Map<String, String>>(  );

    @PostConstruct
    protected void init() {
        Map<String, String> country = new HashMap<String, String>(  );
        country.put( "md", "Madrid" );
        country.put( "al", "Alcobendas" );
        country.put( "mj", "Majadahonda" );
        towns.put( "md", country );

        country = new HashMap<String, String>(  );
        country.put( "st", "Sitges" );
        country.put( "vng", "Vilanova i la Geltru" );
        country.put( "bcn", "Barcelona" );
        towns.put( "ca", country );

        country = new HashMap<String, String>(  );
        country.put( "lk", "Lekunberri" );
        country.put( "bb", "Bilbao" );
        country.put( "dnt", "Donosti" );
        towns.put( "eus", country );

        country = new HashMap<String, String>(  );
        country.put( "al", "Alghero" );
        country.put( "pt", "Porto Torres" );
        country.put( "cts", "Castelsardo" );
        towns.put( "sa", country );

        country = new HashMap<String, String>(  );
        country.put( "am", "Ambra" );
        country.put( "si", "Siena" );
        towns.put( "tu", country );

        country = new HashMap<String, String>(  );
        country.put( "ro", "Rome" );
        country.put( "ci", "Civitavecchia" );
        towns.put( "la", country );

        country = new HashMap<String, String>(  );
        country.put( "av", "Aveiro" );
        country.put( "es", "Estarreja" );
        country.put( "an", "Anadia" );
        towns.put( "av", country );

        country = new HashMap<String, String>(  );
        country.put( "fa", "Faro" );
        country.put( "lga", "Lagoa" );
        country.put( "lg", "Lagos" );
        towns.put( "al", country );

        country = new HashMap<String, String>(  );
        country.put( "ff", "Fafe" );
        country.put( "vi", "Vizela" );
        towns.put( "av", country );
    }

    @Override
    public String getId() {
        return "Towns";
    }

    @Override
    public Map<String, String> getValues( String rootValue, String locale ) {
        return towns.get( rootValue );
    }
}
