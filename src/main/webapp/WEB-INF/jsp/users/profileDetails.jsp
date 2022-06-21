<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="users">
    <jsp:include page="../socialBar/socialBar.jsp"/>
    <div class="menu">
        <div class="container">
    <h1>Your profile</h1>
    
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
        <div>
            <b><th>Username: </th>
            <td><c:out value="${user.username}"/></b></td>
            </div>
    
    <br/>
    <a class="btn btn-default" href='<spring:url value="/users" htmlEscape="true"/>'><i class="fa fa-chevron-circle-left" aria-hidden="true"></i> Return</a>
    <a class="btn btn-default" style="background-color: darkmagenta" href='<spring:url value="/users/profile/changePassword" htmlEscape="true"/>'>
        Change Password <i class="fa fa-pencil" aria-hidden="true"></i></a>
</div>


    <!-- Aqui ponemos la tabla de aceptar amigos, luego la moveremos a una vista desplegable-->
<br/>
<br/>
<c:if test="${not empty friends}">
<h2>Friend Requests</h2>
<table id="aceptarAmigos" class="aceptarAmigos" style="width: 100%;">
    <thead>
    <tr >
        <th style="width: 250px;">Username</th>
        <th style="width: 200px;" >Accept friend request<i/th>
        <th style="width: 200px">Cancel friend request<i/th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${friends}" var="user">
        <tr>
            <td>
                <spring:url value="/users/{username}" var="userUrl">
                    <spring:param name="username" value="${user.username}"/>
                </spring:url>
                <a href="${fn:escapeXml(userUrl)}"><c:out value="${user.username}"/></a>
            </td>
            
            
            <td>
                <a class="btn btn-default" href='<spring:url value="/users/${user.username}/addFriend" htmlEscape="true"/>'>Accept petition</a>
            </td>
            <td>
                
                <a class="btn btn-default" href='<spring:url value="/users/${user.username}/deleteFriend" htmlEscape="true"/>'>Cancel petition</a>
            </td>
                           
        </tr>
    </c:forEach>
    </tbody>
</table>
</c:if>

<c:if test="${not empty friendsList}">
    
<br/>
<br/>
    <h2>Friend list</h2>
<table id="aceptarAmigos" class="aceptarAmigos">
    <thead>
    <tr>
        <th style="width: 150px;">Username</th>
            <th>Delete friend<i/th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${friendsList}" var="friend">
        <tr>
            <td>
                <spring:url value="/users/{username}" var="userUrl">
                    <spring:param name="username" value="${friend.username}"/>
                </spring:url>
                <a href="${fn:escapeXml(userUrl)}"><c:out value="${friend.username}"/></a>
            </td>
            
            <td>
                
                <a class="btn btn-default" href='<spring:url value="/users/${friend.username}/deleteFriend" htmlEscape="true"/>'>Delete friend</a>
            </td>
                           
        </tr>
    </c:forEach>
    </tbody>
</table>
</c:if>
</div>
    </div>
    <br/>
    <br/>
   

</petclinic:layout>
