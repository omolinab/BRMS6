package org.jbpm.formModeler.core.processing.formProcessing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import org.jbpm.formModeler.api.model.Field;
import org.jbpm.formModeler.api.model.Form;
import org.jbpm.formModeler.core.processing.FormProcessor;
import org.jbpm.formModeler.core.processing.FormStatusData;
import org.jbpm.formModeler.extended.combos.DependentComboValuesProvider;
import org.jbpm.formModeler.extended.combos.DependentComboValuesProviderManager;
import org.jbpm.formModeler.extended.combos.fieldType.DependentCombo;
import org.jbpm.formModeler.service.LocaleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DependentCombosProcessor extends FormChangeProcessor {


    private static transient Logger log = LoggerFactory.getLogger( DependentCombosProcessor.class );

    @Inject
    protected DependentComboValuesProviderManager providerManager;

    @Inject
    protected LocaleManager localeManager;

    @Override
    public FormChangeResponse doProcess( FormChangeResponse response ) {

        try {
            Form form = context.getForm();

            if (form == null) {
                log.warn("Form object is not present in current FormProcessingContext, form processing will be canceled. context: " + context);
                return response;
            }

            FormStatusData statusData = formProcessor.read( form, context.getNamespace() );

            Collection fieldNames = getEvaluableFields();
            for (Iterator iterator = fieldNames.iterator(); iterator.hasNext();) {
                String fieldName = (String) iterator.next();
                Field field = form.getField(fieldName);
                if (DependentCombo.class.getName().equals( field.getCustomFieldType() )) {
                    String rootField = field.getParam1();
                    String providerId = field.getParam2();

                    String rootValue = ( String ) statusData.getCurrentValue( rootField );
                    String currentValue = ( String ) statusData.getCurrentValue( fieldName );

                    List result = new ArrayList(  );

                    boolean found = false;

                    DependentComboValuesProvider provider = providerManager.getProvider( providerId );
                    if (provider != null) {

                        Map<String, String> combovalues = provider.getValues( rootValue, localeManager.getCurrentLang() );
                        if (combovalues != null) {
                            for (String key : combovalues.keySet()) {
                                Object[] value = new Object[3];
                                value[0] = key;
                                value[1] = combovalues.get( key );
                                value[2] = key.equals( currentValue );
                                if ( key.equals( currentValue ) ) found = true;
                                result.add( value );
                            }
                        }
                    }
                    if (!found) formProcessor.modify( form, context.getNamespace(), fieldName, "" );
                    String inputName = context.getNamespace() + FormProcessor.NAMESPACE_SEPARATOR + form.getId() + FormProcessor.NAMESPACE_SEPARATOR + field.getFieldName();
                    response.addInstruction( new SetListValuesInstruction( inputName, result ) );
                }
            }
        } catch (Exception e) {
            log.error("Error: ", e);
        }
        return response;
    }

    @Override
    public int getSupportedContextType() {
        return FormProcessingContext.TYPE_RANGE;
    }
}
