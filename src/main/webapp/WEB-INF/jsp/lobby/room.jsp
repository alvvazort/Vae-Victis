<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="lobbyRoom">
    <jsp:include page="../socialBar/socialBar.jsp"/>
    <div class="menu">
        <div class="container">
            <h2>Lobby</h2>

            <h2>Jugadores conectados</h2>

            <div class="gif"></div>
            <div class="inner">
                <div id="lobbyBoard" class="row">
                    <div class="col">
                        <ul id="playersConnected" class="numberList" type="I">
                            <c:forEach items="${lobby.getUsersIn()}" var="user" varStatus="loop">
                                <li>
                                    <div class="row">
                                        <div class="col noFill">
                                            <span class="number">${numerosRomanos[loop.index]}
                                            </span>
                                        </div>
                                        <span class="col">${user.getUsername()}
                                            <c:if test= "${user.getUsername() == lobby.getLobbyAdmin().getUsername()}">
                                                <span><i class="fa fa-star" aria-hidden="true"></i></span>
                                            </c:if>
                                        </span>
                                    </div>
                                </li>
                            </c:forEach>
                            
                            <c:forEach begin="${lobby.getUsersIn().size() + 1}" end="${lobby.getPlayerNumber()}" varStatus="loop">
                                <li>
                                    <div class="row">
                                        <div class="col noFill">
                                            <span class="number">${numerosRomanos[loop.index-1]}
                                            </span>
                                        </div>
                                        <span class="col">No encontrado</span>
                                    </div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="col noFill">
                        <form id="inviteFriendToLobby" action="/lobby/invite/${lobby.getId()}" target="_blank" method="post">
                            <label>
                                <span style="display: block;">
                                Invitar cualquier usuario:
                                </span>
                                <input type="text" name="username">
                            </label>
                            <button type="submit">Invitar</button>
                        </form>
                        <ul id="lobbyLog">
                            
                        </ul>
                        <c:if test= "${userLogged == lobby.getLobbyAdmin().getUsername()}">
                        <div class="infoMatchmaking">
                            <div>
                                Estado: <span id="mathmakingState"></span>
                            </div>
                            <div>
                                Tiempo buscando: <span id="timeSearch"></span>
                            </div>
                        </div>
                        </c:if>
                    </div>
                </div>
                
                <div class="bottom row">
                    <c:if test= "${userLogged == lobby.getLobbyAdmin().getUsername()}">
                        <form id="gameOptions" class="col inputGroup" action="/lobby/${lobby.getId()}" method="post">
                            <div>
                                <label for="gameLevel">Dificultad</label>
                                <select name="gameLevel" id="gameLevel">
                                    <option value="1" <c:if test="${lobby.getGameLevel() == 1}">selected</c:if>>Facil</option>
                                    <option value="2" <c:if test="${lobby.getGameLevel() == 2}">selected</c:if>>Medio</option>
                                    <option value="3" <c:if test="${lobby.getGameLevel() == 3}">selected</c:if>>Dificil</option>
                                </select>
                            </div>
                            <div>
                                <label for="userNumber">Jugadores</label>
                                <select name="userNumber" id="userNumber">
                                    <option value="1">TESTING: 1</option>
                                    <option value="2">TESTING: 2</option>
                                    <c:forEach begin="3" end="6" var="loop">
                                        <c:if test="${loop >= lobby.getUsersIn().size()}">
                                            <c:choose>
                                                <c:when test="${loop == lobby.getPlayerNumber()}">
                                                    <option value="${loop}" selected>${loop}</option>
                                                </c:when>    
                                                <c:otherwise>
                                                    <option value="${loop}">${loop}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <div>
                                <button style = "background-color: dodgerblue;" type="submit">Guardar preferencias</button>
                            </div>
                        </form>
                    </c:if>
                    <div class="col menuLinks">
                        <c:if test= "${userLogged == lobby.getLobbyAdmin().getUsername()}">
                            <a id="startMatchmaking">Buscar jugadores</a>
                            <a id="cancelMatchmaking" style="display: none;">Cancelar busqueda</a>
                        </c:if>
                        <a href="/lobby/logout">Desconectarse</a>
                    </div>
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
