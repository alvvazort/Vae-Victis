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
                <ul>
                <br><br>
                    <li>
                        <span class="titulo">Estadisticas</span>
                        <span class="global" id="global">Global</span>
                        <button class="switch" id="switch"></button>
                    </li>
                </ul>
                
                <ul>
                    <li>
                        <span class="textoEstadisticas">Partidas Totales :</span>
                        <span class="cantidadEstadisticas">${partidas.size()}</span>
                    </li>
                </ul>

                <ul>
                    <li>
                        <span class="textoEstadisticas">Partidas Ganadas :</span>
                        <span class="cantidadEstadisticas" id="PartidasGanadas">-</span>
                    </li>
                </ul>

                <ul>
                    <li>
                        <span class="textoEstadisticas">Duracion de partidas :</span>
                        <span  style="left: 46vw;" class="cantidadEstadisticas" id="DuracionPartidas">${FechaGlobal.get(0)}M/ ${FechaGlobal.get(1)}M / ${FechaGlobal.get(2)}M*</span>
                    </li>
                </ul>

                <ul>
                    <li>
                        <span class="textoEstadisticas">Promedio de jugadores :</span>
                        <span class="cantidadEstadisticas" id="PromedioJugadores">${PromedioUsuarios}</span>
                    </li>
                </ul>

                <ul>
                    <li>
                        <span class="textoEstadisticas">Carta Favorita :</span>
                        <span style="left: 49vw;" class="cantidadEstadisticas" id="cartaMasJugada">${cartaMasJugada.getName()}</span>
                    </li>
                </ul>
                <ul>
                    <li>
                        <button class="iconoRanking" id="iconoRanking"> </button>
                        <button class="iconoMoneda" id="iconoMoneda"> </button>
                        <button onclick="location.href='./listOfGames'" class="iconoPartidas"> </button> 

                        <span class="aclaraciones">*Promedio/Minimo/Maximo</span>
                    </li>
                </ul>
            
                <br><br>
                </div>

                <div id="divRanking" style="display: none;"> <!-- Div ranking-->
                    <br>
                    <ul>
                        <li>
                            <span class="titulo">Ranking</span>
                        </li>
                    </ul>
                    
                    <ul>
                        <li style="background-color: rgba(218, 175, 0, 0.658); border-style: groove; border-radius: 200px;">
                            <span class="textoEstadisticas">Posicion I :</span>
                            <span style="left: 33vw;" class="cantidadEstadisticas">${usuariosGanadores[0].username}</span>
                            <span style="left: 51vw; font-size: 20px; margin-top: 3px;" class="cantidadEstadisticas">${usuariosGanadores[0].getWins()} victorias</span>
                            <span style="left: 12vw;position: absolute;"><img src="/resources/images/corona.png" style="background-size: 100% 100%; height: 35px;"></span>
                            
                        </li>
                    </ul>
    
                    <ul>
                        <li style="background-color: rgba(73, 72, 70, 0.699); border-style: groove; border-radius: 200px;">
                            <span class="textoEstadisticas">Posicion II :</span>
                            <span style="left: 33vw;" class="cantidadEstadisticas">${usuariosGanadores[1].username}</span>
                            <span style="left: 51vw; font-size: 20px; margin-top: 3px;" class="cantidadEstadisticas">${usuariosGanadores[1].getWins()} victorias</span>
                            <span style="left: 12vw;position: absolute;"><img src="/resources/images/corona.png" style="background-size: 100% 100%; height: 35px;"></span>
                        </li>
                    </ul>
    
                    <ul>
                        <li style="background-color: rgba(109, 73, 0, 0.699); border-style: groove; border-radius: 200px;">
                            <span class="textoEstadisticas">Posicion III :</span>
                            <span  style="left: 33vw;" class="cantidadEstadisticas">${usuariosGanadores[2].username}</span>
                            <span style="left: 51vw; font-size: 20px; margin-top: 3px;" class="cantidadEstadisticas">${usuariosGanadores[2].getWins()} victorias</span>
                            <span style="left: 12vw;position: absolute;"><img src="/resources/images/corona.png" style="background-size: 100% 100%; height: 35px;"></span>
                        </li>
                    </ul>
    
                    <ul>
                        <li>
                            <span class="textoEstadisticas">Posicion IV :</span>
                            <span style="left: 33vw;" class="cantidadEstadisticas">${usuariosGanadores[3].username} </span>
                            <span style="left: 51vw; font-size: 20px; margin-top: 3px;" class="cantidadEstadisticas">${usuariosGanadores[3].getWins()} victorias</span>
                        </li>
                    </ul>
                    <ul>
                        <li>
                            <span class="textoEstadisticas">Posicion V :</span>
                            <span style="left: 51vw; font-size: 20px; margin-top: 3px;" class="cantidadEstadisticas">${usuariosGanadores[4].getWins()} victorias</span>
                            <span style="left: 33vw;" class="cantidadEstadisticas">${usuariosGanadores[4].username}</span>
                        </li>
                    </ul>
                    <ul>
                        <li>
                            <button class="iconoEstadisticas" id="iconoEstadisticas"> </button>
                            <button class="iconoMoneda" id="iconoMonedaRanking"> </button>
                        </li>
                    </ul>
                
                    <br>
                    </div>


                    <div id="divLogros" style="display: none;"> <!-- Div Logros-->
                        <br><br><br> <br>
                        <ul>
                            <li>
                                <span class="titulo">Logros</span>
                            </li>
                        </ul>
                        <div class="divContainerLogros">
                            <c:forEach items="${logros}" var="logro"> 
                            
                                <ul>
                                    <li>
                                        <div class="dropdown">
                                            <p class="dropbtn">${logro.getName()}</p>
                                            <div class="dropdown-content">
                                            ${logro.getDescription().replace('1',logro.getAchievementCondition().toString())}
                                            </div>
                                        </div>
                                        <img class="Logro${usuarioActual.getAchievements().contains(logro)}"></img>
                                    </li>
                                </ul>

                            </c:forEach> 
                        </div>
                        <br>      <br>
                       
                        <ul>
                            <li>
                                <button class="iconoRanking" id="iconoRankingLogros"> </button>
                                <button class="iconoEstadisticas" style="margin-left: 0px;" id="iconoEstadisticasLogros"> </button>
                            </li>
                        </ul>
                    
                        <br><br>
                        <br><br>
                </div>
            </div>
        </div>
    </div>
    
    <script>
        let botonSwitch = document.getElementById("switch"); 
        let botonRanking = document.getElementById("iconoRanking");
        let global = document.getElementById("global"); 
        let botonEstadisticas = document.getElementById("iconoEstadisticas");
        let divEstadisticas =  document.getElementById("divEstadisticas");
        let divRanking =  document.getElementById("divRanking");
        let divLogros = document.getElementById("divLogros");
        let iconoMoneda = document.getElementById("iconoMoneda");
        let botonMonedaRanking = document.getElementById("iconoMonedaRanking");
        let iconoRankingLogros = document.getElementById("iconoRankingLogros");
        let iconoEstadisticasLogros = document.getElementById("iconoEstadisticasLogros");
        let partidasGanadas = document.getElementById("PartidasGanadas");
        let duracionPartidas = document.getElementById("DuracionPartidas");
        let promedioJugadores = document.getElementById("PromedioJugadores");
        let cartaMasJugada = document.getElementById("cartaMasJugada");



        botonSwitch.onclick = cambiaGlobal; 
        botonRanking.onclick = cambiaARanking;
        iconoRankingLogros.onclick = cambiaARanking;
        iconoEstadisticasLogros.onclick = cambiaAEstadisticas;
        botonEstadisticas.onclick = cambiaAEstadisticas;
        iconoMoneda.onclick = cambiaALogros;
        botonMonedaRanking.onclick = cambiaALogros;


        function cambiaGlobal(evento) {
            if (botonSwitch.style.backgroundImage  == 'url("/resources/images/switch.png")'){
                botonSwitch.style.backgroundImage  = "url('/resources/images/switchOn.png')";
                global.textContent = "Personal";
                global.style.marginLeft = "6vw";
                partidasGanadas.textContent = "${victoriasUsuario}";
                duracionPartidas.textContent = "${FechaPersonal.get(0)}M/ ${FechaPersonal.get(1)}M / ${FechaPersonal.get(2)}M*";
                promedioJugadores.textContent = "${PromedioUsuariosUser}";
                cartaMasJugada = "${cartaMasJugadaUser.getName()}";
            }
            else{
                botonSwitch.style.backgroundImage  = "url('/resources/images/switch.png')";
                global.textContent = "Global";
                global.style.marginLeft = "10vw";
                partidasGanadas.textContent = "-";
                duracionPartidas.textContent = "${FechaGlobal.get(0)}M/ ${FechaGlobal.get(1)}M / ${FechaGlobal.get(2)}M*";
                promedioJugadores.textContent = "${PromedioUsuarios}";
                cartaMasJugada = "${cartaMasJugada.getName()}";
            }
        }

        function cambiaARanking(evento) {
            divEstadisticas.style.display = "none";
            divRanking.style.display = "block";
            divLogros.style.display = "none";
        }

        function cambiaAEstadisticas(evento) {
            divEstadisticas.style.display = "block";
            divRanking.style.display = "none";
            divLogros.style.display = "none";
        }

        function cambiaALogros(evento){
            divLogros.style.display = "block";
            divRanking.style.display = "none";
            divEstadisticas.style.display = "none";
        }

    </script>

    <script src="/resources/js/ws/sockjs.min.js"></script>
    <script src="/resources/js/ws/stomp.min.js"></script>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/resources/js/ws/lobby/matchmaking.js"></script>
    <script src="/resources/js/ws/lobby/chat.js"></script>
</petclinic:layout>
