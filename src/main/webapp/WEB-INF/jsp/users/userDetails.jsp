<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" 
	uri="http://www.springframework.org/security/tags"%>



<petclinic:layout pageName="users"/>
    <jsp:include page="../socialBar/socialBar.jsp"/>
    <div class="menu">
        <div class="container">
    <h1>Perfil de <c:out value="${user.username}"/></h1>
    
    <sec:authorize access="hasAuthority('admin')">
        <c:forEach items="${revisions}" var="rev">
            <div>
                <li>
                    <i>
                        ${rev}
                    </i>
                </li>
            </div>
        </c:forEach>
    <br>
    </sec:authorize>
    
    <div class="info usuario">
        <c:choose>
        <c:when test='${ocultarboton}'>
            <div class="addfriend">
                <a class="btn btn-default" style="background-color: blue" href='<spring:url value="/users/${user.username}/addFriend" htmlEscape="true"/>'>
                Send friend petition <i class="fa fa-paper-plane" aria-hidden="true"></i></a>
            </div>
        </c:when>
        
            
        <c:when test='${soyDestino}'>
            <div class="peticionPendiente">
                <a class="btn btn-default" style="background-color: darkgreen" href='<spring:url value="/users/${user.username}/addFriend" htmlEscape="true"/>'>
                Accept petition <i class="fa fa-check" aria-hidden="true"></i></a>
                <a class="btn btn-default" style="background-color: darkred" href='<spring:url value="/users/${user.username}/deleteFriend" htmlEscape="true"/>'>
                Cancel petition <i class="fa fa-times" aria-hidden="true"></i></a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="deletefriend">
                <a class="btn btn-default" href='<spring:url value="/users/${user.username}/deleteFriend" htmlEscape="true"/>'>Delete Friend</a>
         </div>
        </c:otherwise>
        
        </c:choose>
    
    <br/>
    <sec:authorize access="hasAuthority('admin')">
        <a class="btn btn-default" style="background-color: darkmagenta"; href='<spring:url value="/users/${user.username}/edit" htmlEscape="true"/>'>
        Change Password <i class="fa fa-pencil" aria-hidden="true"></i></a>
        <a class="btn btn-default" style="background-color: red"; href='<spring:url value="/users/delete/${user.username}" htmlEscape="true"/>'>
        Delete User <i class="fa fa-minus-circle" aria-hidden="true"></i></a>
    </sec:authorize>
    </div>

    
    <a class="btn btn-default" href='<spring:url value="/users" htmlEscape="true"/>'><i class="fa fa-chevron-circle-left" aria-hidden="true"></i> Return</a>
    <br/>
    <br/>
   
        </div>
    </div>
