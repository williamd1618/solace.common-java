<#assign classbody>
<#assign idType = pojo.getJavaTypeName(pojo.getIdentifierProperty(),false)/>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())/>

/**
 * Genericized interface for ${idType}
 *
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 */
public interface I${declarationName}Dao extends IGenericDao<${idType}, ${declarationName}> {

}
</#assign>

${pojo.getPackageDeclaration()}
// Generated ${date} by Hibernate Tools ${version}

${pojo.generateImports()}
import com.solace.data.IGenericDao;
<#if pojo.getPackageName().endsWith(".dao") >
import ${pojo.getPackageName().substring(0,pojo.getPackageName().indexOf(".dao"))}.*;
</#if>
${classbody}
