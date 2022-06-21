<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->

<petclinic:layout pageName="Users">
    <jsp:include page="../socialBar/socialBar.jsp"/>
    <div class="menu">
        <div class="container">
            <h2>Find Users</h2>

            <form:form modelAttribute="user" action="/users" method="get" class="form-horizontal" id="search-user-form">
                <div class="control-grmoup" id="username">

                    <label class="col-sm-2 control-label">Username </label>
                    <div class="col-sm-10">
                        <form:input class="form-control" path="username" size="30" maxlength="80"/>
                        <span class="help-inline"><form:errors path="*"/></span>
                    </div>
                    
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">Find User</button>
                    </div>
                </div>
            </form:form>

            <br/> 
            <sec:authorize access="hasAuthority('admin')">
                <a class="btn btn-default" style="background-color: sienna" href='<spring:url value="/users/new" htmlEscape="true"/>'>
                <i class="fa fa-plus-circle" aria-hidden="true"></i> Create new user (Admin option)</a>
            </sec:authorize>
        </div>
    </div>
	


</petclinic:layout>
