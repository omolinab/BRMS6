package org.jbpm.formModeler.extended.combos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class DependentComboValuesProviderManager implements Serializable {
    @Inject
    private Instance<DependentComboValuesProvider> installedProviders;

    private Map<String, DependentComboValuesProvider> providers;

    @PostConstruct
    protected void init() {
        providers = new HashMap<String, DependentComboValuesProvider>(  );
        for (DependentComboValuesProvider provider : installedProviders) {
            providers.put( provider.getId(), provider );
        }
    }

    public DependentComboValuesProvider getProvider(String id) {
        return providers.get( id );
    }
}
