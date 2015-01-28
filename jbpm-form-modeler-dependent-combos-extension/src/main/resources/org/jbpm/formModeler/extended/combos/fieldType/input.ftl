<div>
    <select name="${fieldName}" id="${fieldName}" class="dynInputStyle skn-input"
            onchange="processFormInputChange(this);"
    <#if readonly == true>
            onfocus="this.blur();" disabled
    </#if>
            >
        <option></option>
    <#list elements?keys as key>
        <option value="${key}"
        <#if key == value>
            selected
        </#if>
        >${elements[key]}</option>
    </#list>
    </select>
</div>