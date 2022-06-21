<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  
<petclinic:layout pageName="404">
    <div class="menu">
        <div class="container">
            <h2>Error 404: Página o partida no encontrada</h2>

            
        </div>
    </div>
    

    <script src="/resources/js/ws/sockjs.min.js"></script>
    <script src="/resources/js/ws/stomp.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/resources/js/ws/game/JoinMatchmaking.js"></script>
    <script src="/resources/js/ws/game/join.js"></script>
</petclinic:layout>