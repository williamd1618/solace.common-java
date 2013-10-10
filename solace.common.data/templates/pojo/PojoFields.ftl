<#-- // Fields -->

<#include "ReservedFields.ftl">
<#foreach field in pojo.getAllPropertiesIterator()>
		
	<#if !reserved?seq_contains(field.name?upper_case) && pojo.getMetaAttribAsBool(field, "gen-property", true)> 		
		<#if pojo.hasMetaAttribute(field, "field-description")>    
	/**
     ${pojo.getFieldJavaDoc(field, 0)}
     */
 		</#if>     
 	 	 		
 	${pojo.getFieldModifiers(field)} ${pojo.getJavaTypeName(field, jdk5)} ${field.name}<#if pojo.hasFieldInitializor(field, jdk5)> = ${pojo.getFieldInitialization(field, jdk5)}</#if>;	
	</#if>	
</#foreach>
