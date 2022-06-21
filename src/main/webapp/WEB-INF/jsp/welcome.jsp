<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<sec:authorize access="isAuthenticated()">
    <jsp:include page="./socialBar/socialBar.jsp"/>
</sec:authorize>

<petclinic:layout pageName="home"> 

<img src="/resources/images/ModoHorizontal.jpg" id="horizontal">

<div class="menu">
    <div class="menuLinks">
        <div class="row">
            
            <sec:authorize access="!isAuthenticated()">
                <div class="col">
                    <a href="<c:url value='/login' />" />Iniciar sesion</a>
                    <a href="<c:url value='/users/new' />" />Registrarme</a>
                </div>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <div class="col">
                    <a href="<c:url value='/lobby/' />" />Jugar ya</a>
                    <a href="<c:url value='/estadisticas/' />" />Estadisticas</a>
                    <sec:authorize access="hasAuthority('admin')">                            
                        <div id="gridWelcome">
                            <div onclick="location.href = '/estadisticas/listOfGames';" id="gridItemWelcome"> Listado partidas</div>
                            <div onclick="location.href = '/users/page/1';" id="gridItemWelcome"> Listado jugadores</div>
                            <div onclick="location.href = '/estadisticas/editLogro';" id="gridItemWelcome"> Edicion logros</div>  
                        </div>
                    </sec:authorize>
                    <c:forEach items="${lobbies}" var="lobby">
                        <a href="/lobby/${lobby.getId()}" />Invitacion al Lobby ${lobby.getId()}</a>
                    </c:forEach>
                </div>
            </sec:authorize>
        </div>
    </div>
</div>

<div class="square" id="miembros">
    <ul>
        <ul id="tituloYgrupo"> ${title} - ${group}</ul>
        <c:forEach items="${members}" var="member">
            <ul id="miembro">${member.firstName} ${member.lastName}</ul>
        </c:forEach>
    </ul>
</div>

</petclinic:layout>