<#if pojo.hasIdentifierProperty()>
${pojo.getPackageDeclaration()}
// Generated ${date} by Hibernate Tools ${version}


<#assign idType = pojo.getJavaTypeName(pojo.getIdentifierProperty(),false)/>
<#assign declarationName = pojo.importType(pojo.getDeclarationName())/>
<#assign classbody>
/**
 * DAO object for domain model class ${declarationName}.
 * @author <a href="mailto:dan.williams@nbcuni.com">Daniel Williams</a>
 */
@Repository
public class ${declarationName}Dao extends GenericDao<${idType},${declarationName}> implements I${declarationName}Dao {

}
</#assign>

${pojo.generateImports()}
import com.solace.data.jpa.GenericDao;
import org.springframework.stereotype.*;
<#if pojo.getPackageName().endsWith(".dao.jpa") >
import ${pojo.getPackageName().substring(0,pojo.getPackageName().indexOf(".dao.jpa"))}.*;
</#if>
${classbody}
</#if>
