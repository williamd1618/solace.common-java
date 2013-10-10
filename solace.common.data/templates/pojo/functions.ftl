

<#function basePackage pkg stem>
	idx = pkg?index_of(stem)
	<#return pkg?substring(0, idx)>
</#function>