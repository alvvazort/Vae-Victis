<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="lobbyRoom">

    <sec:authorize access="isAuthenticated()">
        <jsp:include page="./socialBar/socialBar.jsp"/>
    </sec:authorize>

<div class="menu">
        <div class="container2">
            <div class="inner">
                <div id="divEstadisticas"> <!-- Div principal-->
                    <table style="width:100%; border: 1px solid black;">
                        <thead>
                          <tr>
                            <th class="tablaTitulo">Partida</th>
                            <th class="tablaTitulo">Creador</th>
                            <th class="tablaTitulo">Jugadores</th>
                            <th class="tablaTitulo">Ganador</th>
                            <th class="tablaTitulo">Fin</th> 
                          </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${partidas}" var="partida">
                                
                                <tr>
                                    <td class="tablaContenido">${partida.id}</td>
                                    <td class="tablaContenido">${partida.getLobbyAdmin().getUsername()}</td>
                                    <td class="tablaContenido">
                                        <c:forEach items="${partida.getPlayersIn()}" var="player">
                                            ${player.getUser().getUsername()} <br>
                                        </c:forEach> 
                                    </td>
                                    <%--
                                    <td>${partida.getWinner()}</td>
                                    <td>${partida.getRemovedAt()}</td> 
                                    --%>
                                </tr>
                            </c:forEach> 
                        </tbody>
                    </table>
                    <button onclick="location.href='./'" class="iconoEstadisticas"> </button> 

                </div>
            </div>
        </div>
    </div>
    

    <script src="/resources/js/ws/sockjs.min.js"></script>
    <script src="/resources/js/ws/stomp.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/resources/js/ws/lobby/matchmaking.js"></script>
    <script src="/resources/js/ws/lobby/chat.js"></script>
</petclinic:layout>
