<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="game" tagdir="/WEB-INF/tags/game" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="customScript" required="false" fragment="true"%>

<!doctype html>
<html>
<petclinic:htmlHeader/>


<body id= "backgroundGame">

<img src="/resources/images/ModoHorizontal.jpg" id="horizontal">

<main id="todo" class="gameLayout">
    

    <div class="content">
        <div class="topSide">
            <c:forEach items="${game.getPlayersIn()}" var="player">
                <c:if test= "${(user == null) || (player.getUser().getUsername() != user.getUsername())}">
                    <div class="player" name="${player.getUser().getUsername()}">
                        <div>Nombre: ${player.getUser().getUsername()}</div>
                        <div>Monedas: ${player.getCoins()}</div>
                        <div>Cartas: ${player.getCards().size()}</div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
        <div class="board">
            <div id="divBotonesTableros"style="float: left;">
                <button data-board="1" class="botonesTableros" style="background-image: url('/resources/images/Tablero_2.png'); background-size: 100%;"></button> <br>
                <button data-board="2" class="botonesTableros" style="background-image: url('/resources/images/Tablero_1.png'); background-size: 100%;"></button> <br>
                <button data-board="3" class="botonesTableros" style="background-image: url('/resources/images/Tablero_3.png'); background-size: 100%;"></button> <br>
            </div>
            <jsp:doBody/>
        </div>
            <c:if test= "${user != null}">
                <div class="abajo">
                        
                        <div class="objetivo1">
                        <p class="textoInGame">OBJETIVO GUERRA</p>
                        <div class="${warGoal[0]}"><div> 
                        </div>

                        <div class="objetivo2">
                        <p class="textoInGame">OBJETIVO CURIA</p>
                        <div class="${curiaGoal}"><div> 
                        </div>

                        <button class="open-cards"   style="z-index: 3; height:30px; width:115px; position:fixed; bottom:20px; left:48vw">Mostrar cartas</button>
                               <div class="cardsmenu">
                                    <div style="text-align: center;font-size:25px">
                                        Mis cartas
                                   </div>
                        <div style=" display: flex; align-items: center;">

                            <c:forEach items="${game.getPlayersIn()}" var="player">
                                <c:if test= "${(user == null) || (player.getUser().getUsername() == user.getUsername())}">
                             <c:forEach items="${player.getCards()}" var="card">
                                <div class="card" cardId="${card.getId()}" name="${card.getIndex()}" cardtext="${card.getText()}" style="background-image: url('${card.getUrl()}');">
                               </div>
                             </c:forEach>
                        </div>
                        <button class="close-cards"  style="z-index: 3; height:30px; width:115px; color:black;">Ocultar cartas</button>
                        </div>
                        <div class="monedas">
                                    <div class="player" name="${player.getUser().getUsername()}">
                                        
                                        <div id="containerPadreMonedas">
                                            <p class="textoInGame">Jugador: ${player.getUser().getUsername()} &nbsp; ${player.getCards().size()} &nbsp; cartas</p>
                                            <div id="containerMonedas">
                                                <c:forEach var="moneda" begin="1" end="${player.getCoins()}" >
                                                    <div id="moneda"> </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                           
                    
                </div>
            </c:if>
    </div>

</main>

<div class="popupMessage gameMessage hidden">
    <div class="content">
    </div>
</div>


<div class="popupMessage threeDices hidden">
    <div class="content">
        <div class="diceHolder">
            <game:diceWar/>
        </div>
            <div class="diceHolder">
            <game:diceWar/>
        </div>
        <div class="diceHolder">
            <game:diceWar/>
        </div>

        <button class="lanzarDado">Lanzar dados</button>
    </div>
</div>

<div class="popupMensaje removeDices hidden">  
    <div class="content">
        <div class="diceHolder">
            <button class="RemoveDice1 RemoveDice">
                <img class="tachado tachado1" display="none"> </img>
            </button>
        </div>
        <div class="diceHolder">
            <button class="RemoveDice2 RemoveDice">
                 <img class="tachado tachado2" display="none"> </img>
            </button>
        </div>
        <div class="diceHolder">
            <button class="RemoveDice3 RemoveDice">
                 <img class="tachado tachado3" display="none"> </img>
            </button>
        </div>
        <button class="ButtonRemove" data-action="removeDices" style="margin-top:40px;">Eliminar</button>
    </div>
</div>

<div class="popupMensaje cityCardChoose hidden">  
    <div class="content">

        <div class="grupoImages">
            <img id="plus" src="/resources/images/up.png"/>
            <img id="minus" src="/resources/images/down.png"/>  
        </div>
        
        
        <div class="cityTokenHolder Yellow">
            <input type="radio" cityToken=2 name="cityTokenChosen" class="cityTokenRadio cityTokenYellow">
            <input type="radio" cityToken=1 name="cityTokenChosen" class="cityTokenRadio cityTokenYellow">
        </div>
        <div class="cityTokenHolder Green">
            <input type="radio" cityToken=4 name="cityTokenChosen2" class="cityTokenRadio cityTokenGreen">
            <input type="radio" cityToken=3 name="cityTokenChosen2" class="cityTokenRadio cityTokenGreen">
        </div>
        <div class="cityTokenHolder Red">
            <input type="radio" cityToken=6 name="cityTokenChosen3" class="cityTokenRadio cityTokenRed">
            <input type="radio" cityToken=5 name="cityTokenChosen3" class="cityTokenRadio cityTokenRed">
        </div>
        <button class="ButtonCard5" data-action="actionCard5" style="margin-top:40px;">Seleccionar</button>
    </div>
</div>

<div class="popupMensaje curiaChoose hidden">  
    <div class="content">

        <div class="grupoImages">
            <img id="plus" src="/resources/images/plus.png"/>
            <img id="minus" src="/resources/images/minus.png"/>  
        </div>
        
        
        <div class="curiaHolder Red">
            <input type="radio" curia=2 name="curiaChosen" class="curiaRadio curiaRed">
            <input type="radio" curia=1 name="curiaChosen" class="curiaRadio curiaRed">
        </div>
        <div class="curiaHolder Green">
            <input type="radio" curia=4 name="curiaChosen" class="curiaRadio curiaGreen">
            <input type="radio" curia=3 name="curiaChosen" class="curiaRadio curiaGreen">
        </div>
        <div class="curiaHolder Blue">
            <input type="radio" curia=6 name="curiaChosen" class="curiaRadio curiaBlue">
            <input type="radio" curia=5 name="curiaChosen" class="curiaRadio curiaBlue">
        </div>
        <button class="ButtonCuria" data-action="actionCuria" style="margin-top:40px;">Sobornar</button>
    </div>
</div>


<div class="popupMensaje warElection hidden">  
    <div class="content">
        <div class="diceHolder">
            <div class="election">
                <img class="" src="/resources/images/dado/dadoConejo.png" />
            </div>
        </div>
        <div class="diceHolder">
            <div class="election">
                 <img src="/resources/images/dado/dadoGallo.png" />
            </div>
        </div>
        <div class="diceHolder">
            <div class="election">
                 <img src="/resources/images/dado/dadoCiervo.png" />
            </div>
        </div>
        <div class="diceHolder">
            <div class="election">
                 <img src="/resources/images/dado/dadoLobo.png" />
            </div>
        </div>
        <button style="margin-top:40px;">Mover</button>
    </div>
</div>

<div class="doom hidden">
    
</div>

<script src="/resources/js/game/diceWar.js"></script>

</body>

</html>