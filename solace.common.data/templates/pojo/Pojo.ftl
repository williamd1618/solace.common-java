<#if pojo.hasIdentifierProperty()>
${pojo.getPackageDeclaration()}
// Generated ${date} by Hibernate Tools ${version}

<#include "OracleCheck.ftl">
<#assign classbody>
<#assign idType = pojo.getJavaTypeName(pojo.getIdentifierProperty(),false)/>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())/>
<#include "PojoTypeDeclaration.ftl"/> implements java.io.Serializable {

<#if !pojo.isInterface()>
<#include "PojoFields.ftl"/>

<#include "PojoConstructors.ftl"/>
   
<#include "PojoPropertyAccessors.ftl"/>

<#include "PojoToString.ftl"/>

<#include "PojoEqualsHashcode.ftl"/>

<#else>
<#include "PojoInterfacePropertyAccessors.ftl"/>

</#if>
<#include "PojoExtraClassCode.ftl"/>

	@Override
	public void doSave() throws Exception {
		DaoManager.get${declarationName}Dao().save(this);
	}

	@Override
	public void doDelete() throws Exception {
		DaoManager.get${declarationName}Dao().delete(this);
	}
}
</#assign>

${pojo.generateImports()}
import org.springframework.beans.factory.annotation.*;
<#if !useOracle >
import com.solace.data.IdentityEntity;
</#if>
import ${pojo.getPackageName()}.dao.*;

${classbody}
</#if>
