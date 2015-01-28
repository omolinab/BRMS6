package org.jbpm.formModeler.extended.combos.processing;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Specializes;

import org.jbpm.formModeler.core.processing.formProcessing.DependentCombosProcessor;
import org.jbpm.formModeler.core.processing.formProcessing.FormChangeProcessor;
import org.jbpm.formModeler.core.processing.formProcessing.FormulasCalculatorChangeProcessor;
import org.jbpm.formModeler.service.cdi.CDIBeanLocator;

@Specializes
public class FormulasCalculatorChangeProcessorExt extends FormulasCalculatorChangeProcessor {

    @PostConstruct
    protected void init() {
        this.setNextProcessor( ( FormChangeProcessor ) CDIBeanLocator.getBeanByType( DependentCombosProcessor.class ) );
    }
}
