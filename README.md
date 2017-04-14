部署时需要修改CAS的spring配置文件deployerConfigContext.xml：
1、增加AuthenticationHandler定义：
    <bean id="huijiaoyunAuthenticationHandler" class="com.ez.cas.support.huixue.authentication.handler.HuixuToHujiaoyunAuthenticationHandler">
      <constructor-arg type="java.lang.String" value="" />
    </bean>
2、在authenticationManager中增加1定义的AuthenticationHandler:
    <entry key-ref="huijiaoyunAuthenticationHandler" ><null/></entry>
3、修改WEB-INF/view/jsp/protocol/2.0/casServiceValidationSuccess.jsp，在增加以下代码<cas:authenticationSuccess></cas:authenticationSuccess>：
    <c:if test="${fn:length(assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes) > 0}">
	<cas:attributes>
		<c:forEach var="attr" items="${assertion.chainedAuthentications[fn:length(assertion.chainedAuthentications)-1].principal.attributes}">
			<cas: ${fn:escapeXml(attr.key)}>${attr.value}</cas:${fn:escapeXml(attr.key)}>
		</c:forEach>
	</cas:attributes>
    </c:if>
