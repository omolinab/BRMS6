package org.jbpm.formModeler.extended.combos;

import java.util.Map;

public interface DependentComboValuesProvider {
    String getId();
    Map<String, String> getValues( String rootValue, String locale );
}
