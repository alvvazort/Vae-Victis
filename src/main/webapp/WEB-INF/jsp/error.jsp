<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  
<petclinic:layout pageName="errorView">
    <div class="menu">
        <div class="container">
            <c:if test = "${not empty errorMessage}">
                <h2>Error: ${errorMessage}</h2>

            </c:if>
            <c:if test = "${ empty errorMessage}">
            <h2>Error: error inesperado </h2>
            </c:if>

            
        </div>  
    </div>
    

    <script src="/resources/js/ws/sockjs.min.js"></script>
    <script src="/resources/js/ws/stomp.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/resources/js/ws/game/JoinMatchmaking.js"></script>
    <script src="/resources/js/ws/game/join.js"></script>
</petclinic:layout>