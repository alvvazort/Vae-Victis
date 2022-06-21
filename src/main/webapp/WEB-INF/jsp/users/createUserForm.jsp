<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="users">
    <c:forEach items="${errorMessage}" var="errorM">
        <div class="alert alert-info" role="alert" style="color:firebrick">
            <c:out value="${errorM}"></c:out>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button> 
        </div>
    </c:forEach>
    <div class="menu">
        <div class="menuLinks">
            
            

            <div class="row">
                <form:form modelAttribute="user" class="registerForm" style="width: 100%; justify-content: center;" id="add-user-form">
                    <div class="col">
                        <c:if test="${empty username}">                                    
                            <a><form:input class="campoForm"  placeholder="Nombre" path="username"/></a>
                        </c:if>
                        <a><form:input class="campoForm" type="password" placeholder="Contrasena" path="password"/></a>
                    </div>
                    <div class="formulario">
                        <div class="col-md-12 text-center">
                            <c:choose>
                                <c:when test="${empty username}">                                    
                                    <button class="btn btn-default" type="submit">Registrar usuario</button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-default" type="submit">Aceptar</button>
                                </c:otherwise>
                            </c:choose>
                               
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</petclinic:layout>
