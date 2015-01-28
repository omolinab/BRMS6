package org.jbpm.formModeler.examples.providers;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

import org.jbpm.formModeler.extended.combos.DependentComboValuesProvider;

@Dependent
public class CitiesProvider implements DependentComboValuesProvider {
    Map<String, Map<String, String>> cities = new HashMap<String, Map<String, String>>(  );

    @PostConstruct
    protected void init() {
        Map<String, String> region = new HashMap<String, String>(  );
        region.put( "mad", "Madrid" );
        region.put( "alc", "Alcobendas" );
        region.put( "ar", "Aranjuez" );
        cities.put( "md", region );

        region = new HashMap<String, String>(  );

        region.put( "bcn", "Barcelona" );
        region.put( "te", "Terrassa" );
        region.put( "sb", "Sabadell" );
        cities.put( "ca", region );

        region = new HashMap<String, String>(  );

        region.put( "bi", "Bilbao" );
        region.put( "se", "Sestao" );
        region.put( "sa", "Santurce" );
        cities.put( "eus", region );

        region = new HashMap<String, String>(  );

        region.put( "av", "Aveiro" );
        region.put( "ar", "Arouca" );
        region.put( "es", "Estarreja" );
        cities.put( "av", region );

        region = new HashMap<String, String>(  );

        region.put( "al", "Alagoa" );
        region.put( "ca", "Carreiras" );
        region.put( "fo", "Fortios" );
        cities.put( "pt", region );
}

    @Override
    public String getId() {
        return "Cities";
    }

    @Override
    public Map<String, String> getValues( String rootValue, String locale ) {
        return cities.get( rootValue );
    }
}
