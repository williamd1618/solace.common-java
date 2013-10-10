<#-- // reserved words -->
<#include "OracleCheck.ftl" />

<#if !useOracle>
<#assign reserved = ["ID","CREATEDATE","LASTUPDATEDATE","VERSION"]>
<#else>
<#assign reserved = ["CREATEDATE","LASTUPDATEDATE","VERSION"]>
</#if>