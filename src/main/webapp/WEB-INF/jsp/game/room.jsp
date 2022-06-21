<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="vaevictis" tagdir="/WEB-INF/tags/game" %>

<vaevictis:layout>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <c:if test="${game.matchLose}">
        <div class="popupMessage winnerMessage">
            <div class="content">
                <p>ROMA HA PERDIDO</p>
            </div>
        </div> 
    </c:if>
    <c:if test="${game.winner != null}">
        <div class="popupMessage winnerMessage">
            <div class="content">
                <p>HA GANADO: <c:out value="${game.winner.username}"/> </p>
            </div>
        </div> 
        
    </c:if>
    <div class="boardBg">
        <div id="divCanvas" data-board="1" class="boardHolder hidden">
            <vaevictis:board gameBoard="${warBoard}" urlBackground="/resources/images/boards/warBoard.png" typeBoard="WarBoard"/>
        </div>
        <div id="divCanvas" data-board="2" class="boardHolder hidden">
            <vaevictis:board gameBoard="${cityStateBoard}" urlBackground="/resources/images/boards/cityStateBoard.png" typeBoard="CityStateBoard"/>
        </div>
        <div data-board="3" class="boardHolder hidden">
            <div class="boardInterface">
                <div class="upper">
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
                <div class="lower">
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
            </div>
            <img src="/resources/images/boards/actionBoard.png" />
            <img id="curiaLeft3" class="curia" src="/resources/images/curia.png" />
            <img id="curiaLeft2" class="curia" src="/resources/images/curia.png" />
            <img id="curiaLeft1" class="curia" src="/resources/images/curia.png" />
            <img id="curiaMid3" class="curia" src="/resources/images/curia.png" />
            <img id="curiaMid2" class="curia" src="/resources/images/curia.png" />
            <img id="curiaMid1" class="curia" src="/resources/images/curia.png" />
            <img id="curiaRight3" class="curia" src="/resources/images/curia.png" />
            <img id="curiaRight2" class="curia" src="/resources/images/curia.png" />
            <img id="curiaRight1" class="curia" src="/resources/images/curia.png" />
            <vaevictis:curia/>
        </div>

    </div>

    <div id="gameId" gameId="${gameId}"> </div>

    
    <script src="/resources/js/ws/sockjs.min.js"></script>
    <script src="/resources/js/ws/stomp.min.js"></script>
    <script src="/resources/js/game/interface.js"></script>
    <script src="/resources/js/ws/game/turn.js"></script>
    <script src="/resources/js/ws/game/cards.js"></script>

</vaevictis:layout>
