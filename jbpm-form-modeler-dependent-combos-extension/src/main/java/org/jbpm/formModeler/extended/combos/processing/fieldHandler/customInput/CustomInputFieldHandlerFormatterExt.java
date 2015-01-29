/**
 * Copyright (C) 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.formModeler.extended.combos.processing.fieldHandler.customInput;

import javax.enterprise.inject.Specializes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jbpm.formModeler.api.model.Field;
import org.jbpm.formModeler.core.fieldTypes.CustomFieldType;
import org.jbpm.formModeler.core.processing.fieldHandlers.FieldHandlerParametersReader;
import org.jbpm.formModeler.core.processing.fieldHandlers.customInput.CustomInputFieldHandlerFormatter;
import org.jbpm.formModeler.service.bb.mvc.taglib.formatter.FormatterException;
import org.jbpm.formModeler.service.cdi.CDIBeanLocator;

@Specializes
public class CustomInputFieldHandlerFormatterExt extends CustomInputFieldHandlerFormatter {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws FormatterException {
        FieldHandlerParametersReader paramsReader = new FieldHandlerParametersReader(request);
        Field field = paramsReader.getCurrentField();

        if (StringUtils.isEmpty(field.getCustomFieldType())) return;

        CustomFieldType customFieldType = (CustomFieldType ) CDIBeanLocator.getBeanByType( field.getCustomFieldType() );
        if (customFieldType == null) return;

        String namespace = paramsReader.getCurrentNamespace();
        Object value = paramsReader.getCurrentFieldValue();

        String mode = (String) getParameter(PARAM_MODE);

        String htmlCode = null;

        if (MODE_INPUT.equals(mode)) {
            htmlCode =  customFieldType.getInputHTML(value, paramsReader.getCurrentFieldName(), namespace, field.getFieldRequired(), field.getReadonly(), field.getParam1(), field.getParam2(), field.getParam3(), field.getParam4(), field.getParam5());
        } else if (MODE_SHOW.equals(mode)) {
            htmlCode =  customFieldType.getShowHTML(value, paramsReader.getCurrentFieldName(), namespace, field.getFieldRequired(), field.getReadonly(), field.getParam1(), field.getParam2(), field.getParam3(), field.getParam4(), field.getParam5());
        }

        if (!StringUtils.isEmpty(htmlCode)) {
            setAttribute("htmlCode", htmlCode);
            renderFragment("output");
        }
    }
}
