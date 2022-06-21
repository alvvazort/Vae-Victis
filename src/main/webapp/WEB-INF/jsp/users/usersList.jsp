<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="users">
    <jsp:include page="../socialBar/socialBar.jsp"/>
    <div class="menu">
        <div class="container">
    <h2>Users</h2>
            
    <table id="usersTable" class="tablaUsuarios">
        <thead>
        <tr>
            <th style="width: 150px;">Username</th>
        </tr>
        </thead>
        <tbody>
        
        <c:forEach items="${pags}" var="pag" begin="${paginaActual-1}" end="${paginaActual-1}">
            <c:forEach items="${pag}" var="user">
            <tr>
                <td>
                    <spring:url value="/users/{username}" var="userUrl"> 
                        <spring:param name="username" value="${user}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(userUrl)}"><c:out value="${user}"/></a>
                </td>
                             
            </tr>
        </c:forEach>
        </c:forEach>

        Pagina

        <c:forEach var="numPagina" begin="1" end="${pags.size()}"> 
               <button onclick="location.href='http://localhost:8080/users/page/${numPagina}'">  ${numPagina} </button> 
        </c:forEach>

        <script>
           

            
        </script>

        </tbody>
    </table></div></div>
</petclinic:layout>
