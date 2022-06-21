// interfaz de la primera opción del tablero de acciones
class CardDraw{
    constructor(onCardDraw) {
        this.onCardDraw = onCardDraw;

        this.interface = $('.onCardDraw');
        this.interface.removeClass('hidden');
    }
}

class CuriaElection{
    constructor(){
        this.interfaceCuria= $('.curiaChoose');
        
    }
    showCuria(onCuriaElection){
        if(onCuriaElection){
            this.interfaceCuria.removeClass('hidden');
        }else{
            this.interfaceCuria.addClass('hidden');
        }
    }
}
class WarElection {
    constructor(onWarElection) {
        this.onWarElection = onWarElection;

        this.interface = $('.warElection');
        this.interface.removeClass('hidden');
        this.send = this.interface.find('button');

        this.elections = this.interface.find('.diceHolder');
        this.choices = [0, 0, 0, 0];

        this.initEvents();
    }
    initEvents() {
        let t = this;
        this.elections.each(function() {
            $(this).click(function() {
                let e = $(this).index(); // índice actual
                let c = t.choices[e];  // decisión con índice actual
                
                if(c==0) {
                    if(t.countChoices()==0) {
                        t.setChoice(e, 1);
                    } else if(t.countChoices()<3) {
                        if(!t.checkValue(2)) {
                            t.setChoice(e, 1);
                        }
                    } 
                } else if(c==1) {
                    if(t.countChoices()<2) {
                        t.setChoice(e, 2);
                    } else {
                        t.setChoice(e, 0);
                    }
                } else {
                    t.setChoice(e, 0);
                }
            });
        });

        this.send.click(() => {
            console.log(this.getResult());
            this.onWarElection(this.getResult());
            this.destroy();
        });
    }
    destroy() {
        this.interface.addClass('hidden');
        this.choices = [0, 0, 0, 0];
    }
    checkValue(value) {
        let ret = false;
        for(let c of this.choices) {
            if(c === value) {
                ret = true;
            }
        }
        return ret;

    }
    countChoices() {
        let ret = 0;
        for(let c of this.choices) {
            ret += c;
        }
        return ret;
    }
    setChoice(index, value) {
        this.choices[index] = value;

        let element = $(this.elections.get(index));
        let points = element.children('.points');

        if(!points.length) {
            element.append('<div class="points"><span>'+value+'</span></div>');
        } else {
            points.children('span').text(value);
        }
    }
    setOtherChoices(value, cb=()=>{ return true; }) {
        for(let c in this.choices) {
            console.log("asdasd ", cb(c));
            if(cb(c)) {
                console.log("decision: " +c);
                this.setChoice(c, value);
            }
        }
    }
    setOtherChoicesZero(index) {
        for(let c in this.choices) {
            if(index!==c) {
                this.setChoice(c, 0);
            }
        }
    }
    getResult() {
        return this.choices;
    }
}
class ActionBoard {
    constructor(level=0, onButtonsLoad=()=>{}) {

        this.board = $('.boardInterface');
        this.buttons = $('<div class="actionButtons"></div>');
        this.actionButtons = [];

        $(document).ready(() => {
            this.interface = $('.boardInterface');
            this.init(level, onButtonsLoad);
        });
    }
    init(level=0, onButtonClick=()=>{}) {
        if(!this.actionButtons.length) {
            this.actionButtons[0] = $('<button class="sendFirstAction">Enviar acción</button>').appendTo($('<div class="buttonInterface"></div>'));
            this.actionButtons[1] = $('<button class="sendSecondAction">Enviar acción</button>').appendTo($('<div class="buttonInterface"></div>'));

            let i=0;
            for(let a of this.actionButtons) {
                if(i==level) {
                    a.click(() => {
                        const turn = this.turn[i];
                        onButtonClick(this, turn);
                    });
                }
                this.buttons.append(a);
                i++;
            }
            
            this.buttons.appendTo(this.board);
        }

        console.log(level);
        console.log(this.actionButtons);
        $(this.actionButtons).addClass('hidden');
        this.actionButtons[level].removeClass('hidden');

        this.interface.append(this.buttons);

        const u = this.interface.children('.upper');
        this.upper = {
            limites: u.children('div:nth-child(1)'),
            aerarium: u.children('div:nth-child(2)'),
            domus: u.children('div:nth-child(3)')
        }

        const l = this.interface.children('.lower');
        this.lower = {
            thermae: l.children('div:nth-child(1)'),
            forum: l.children('div:nth-child(2)'),
            curia: l.children('div:nth-child(3)')
        }
        
        this.turn = [
            [0, 0, 0],
            [0, 0, 0]
        ];

        this.actionEvents();
    }
    
    actionEvents() {
        const that = this;
        const parts = [
            this.upper,
            this.lower
        ]
        let i = 0;
        for(let p of parts) {
            for(let a in p) {
                const action = p[a];

                action.click(function() {
                    action.html('');

                    let index = $(this).index();

                    if(that.countTurn(that.turn[i])<=1) {
                        that.turn[i][index] += 1;
                        $(this).addClass('actioned');
                    } else {
                        that.turn[i][index] = 0;
                        $(this).removeClass('actioned');
                    }
                    if(that.countTurn(that.turn[i])) {

                    }
                    action.append(`<span>${that.turn[i][index]}</span>`);

                    console.log(that.turn);
                });
            }
        }
    }

    getTurn() {
        return this.turn;
    }

    countTurn(turn) {
        let movements = 0;
        for(let t of turn) {
            movements += t;
        }
        return movements;
    }

}
class GameInterface {
    constructor(onDoomPhase=()=>{}, onButtonsLoad=()=>{}, onWarElection=()=>{}, onWarElectionSecond=()=>{}) {
        this.thumbs = $('#divBotonesTableros').children('button');
        this.boards = $('.boardBg');
        this.phases = $('#phaseInterface');
        this.players = $('.player');

        this.dices = [];
        this.diceElement = $(".threeDices");

        this.pieces = [[], []];

        this.removeDicesElement = $(".removeDices");

        this.onDoomPhase = onDoomPhase;
        this.onButtonsLoad = onButtonsLoad;
        this.onWarElection = onWarElection;
        this.onWarElectionSecond = onWarElectionSecond;

        this.actionBoard = undefined;

        this.curiaInterface= new Curia();
        this.curiaElection = new CuriaElection();

        $(document).ready(() => {
            const that = this;
            $('.botonesTableros').click(function() {
                that.changeBoard($(this).attr("data-board"));
            });
            $('.RemoveDice1').click(function() {
                that.crossDice($(this));
            });
            $('.RemoveDice2').click(function() {
                that.crossDice($(this));
            });
            $('.RemoveDice3').click(function() {
                that.crossDice($(this));
            });

            this.messageHolder = $('.gameMessage');
        });
    }
    wait(cb, ms, message = "") {
        return new Promise(resolve => {
            if(message.length) {
                this.messageHolder.children('.content').text(message);
                this.messageHolder.removeClass('hidden');
            }
            cb();
            
            setTimeout(() => {
                if(message.length) {
                    this.messageHolder.addClass('hidden');
                }
                resolve();
            }, ms);
        });
    }
    useCard(cardIndex,response) {
        switch(cardIndex){
            case 0:
                console.log("aaaaaa");
                console.log(response.Card0Selected);
                for( let a of response.Card0Selected){
                    console.log(a);
                    this.deletecard(a);
                }
                break;
            case 6:
                this.updateUser(response.playersTurn.user.username, response.playersTurn.coins);
                break;
            case 5:
                $('.cityCardChoose').addClass('hidden');
                let forward = response.piecesGoForward ?? [],
                    back = response.piecesGoBack ?? [];

                this.movePieces(forward, back);
                break;
        }
    }
    deletecard(cardIdpa) {
        const a = $('.card[cardId="'+cardIdpa+'"]');
        if(a!=undefined){
        console.log(a);
        a.addClass('hidden');
        a.attr('name', '-1');
    }
    }
    throwDices(dicesResult) {
        this.dices = dicesResult;
        let dices = this.diceElement.find('.dice');

        const dicePositions = [
            /* zorro */ [1080, 1800],
            /* gallo */ [1620, 1080],
            /* ciervo */ [360, 630],
            /* lobo */ [360, 810],
            /* oro */ [1530, 720],
            /* rata */ [630, 720]
        ]

        let dicesArray = [dicePositions[dicesResult[0]], dicePositions[dicesResult[1]], dicePositions[dicesResult[2]]];

        let i = 0;
        for(let d of dices) {
            $(d).css('webkitTransform', 'rotateX('+(dicesArray[i][0])+'deg) rotateY('+(dicesArray[i][1])+'deg)');
            $(d).css('transform', 'rotateX('+(dicesArray[i][0])+'deg) rotateY('+(dicesArray[i][1])+'deg)');
            i++;
        }
    }
    getDicesFaces() {
        let dices = [];
        for(let d of this.diceElement) {
            let biggestArea = 0,
                diceFace = null;
            for(let c of d.children('*')) {
                let area = c.width() * c.height();
                if(area > biggestArea) {
                    biggestArea = area;
                    diceFace = c;
                }
            }
            dices.push(diceFace);
        }
        return dices;
    }
    setPieces(pieces) {
        this.pieces = pieces;
    }
    getPieces() {
        return this.pieces;
    }
    miniDices() {
        this.diceElement.addClass('miniDice');
        this.diceElement.find('button[data-action="throwDices"]').addClass('hidden');
        this.diceElement.find('button[data-action="removeDices"]').removeClass('hidden');
    }
    changeBoard(index) {
        this.boards.children('div').addClass('hidden');
        this.boards.children('div[data-board="'+index+'"]').removeClass('hidden');
    }
    unlockPhaseInterface(phase) {
        this.phases.children('*').addClass('hidden');
        this.phases.children('[data-phase="'+phase+'"]').removeClass('hidden');
    }
    showDices(cb=()=>{}) {
        this.diceElement.removeClass("hidden");
        this.diceElement.find('.lanzarDado').remove();
        let button = $('<button class="lanzarDado">Lanzar dados</button>').appendTo(this.diceElement.children('.content'));
        console.log(button);
        button.click(function() {
            
        })
        cb(button);
    }
    getDeletedDices() {
        const dices = $('.removeDices > .content > .diceHolder');
        let deletedDices = [0, 0, 0];

        dices.each(function(d) {
            deletedDices[d] = $(this).hasClass("removed") ? 1 : 0;
        });

        return deletedDices;
    }
    applyEvent(response){

        switch(response.cityStateEvent){
            case "DISCARD1CARD":
                console.log("Llega a DISCARD1CARD");
                setTimeout(() => {
                    
                    var message= "Notificación de casilla";
                    var subMessage="Como divitiae ha caido a la segunda casilla, te descartas una carta.";
                    this.showMessage(message, subMessage);
                    game.discard=true;
                    
                },8400);
                break;
            case "ALLLOSE2COINS":
                this.updateUser(response.playersTurn.user.username, response.playersTurn.coins);
                setTimeout(() => {
                    var message= "Notificación de casilla";
                    var subMessage= "Como sanitas ha caido a la segunda casilla, todos los jugadores pierden 2 monedas.";
                    this.showMessage(message, subMessage);
                },8400);
                break;
            case "YOULOSE1COIN":

                this.updateUser(response.playersTurn.user.username, response.playersTurn.coins); 
                setTimeout(() => {
                    var message= "Notificación de casilla";
                    var subMessage= "Como rapina ha caido a la segunda casilla, pierdes una moneda.";
                    this.showMessage(message, subMessage);
                },8400);
                break;
            case "ALLLOSE2COINSANDWARS1":
                this.updateUser(response.playersTurn.user.username, response.playersTurn.coins); 
                setTimeout(() => {
                    var message= "Notificación de casilla";
                    var subMessage= "Como sanitas ha caido a la cuarta casilla, todos los jugadores pierden 2 monedas y se retrocede en cada track de guerra.";
                    this.showMessage(message, subMessage);
                },8400);
                break;
            case "YOULOSE2COIN":
                this.updateUser(response.playersTurn.user.username, response.playersTurn.coins); 
                setTimeout(() => {
                    var message= "Notificación de casilla";
                    var subMessage= "Como rapina ha caido a la cuarta casilla, pierdes dos monedas.";
                    this.showMessage(message, subMessage);
                },8400);
        }   
    }

    getCuriaSelected(){

       return $('.curiaRadio:checked').attr('curia'); 
    }

    getCard5Selected(){
        let a=[];
       $('.cityCardChoose').find('.cityTokenRadio:checked').each(function(){
        a.push($(this).attr('cityToken')
        ); 
        
    });
    return a;
    }
    
    loadDicesToCross(){
        let imagenes = ['/resources/images/dado/dadoConejo.png',
        '/resources/images/dado/dadoGallo.png',
        '/resources/images/dado/dadoCiervo.png',
        '/resources/images/dado/dadoLobo.png',
        '/resources/images/dado/dadoOro.png',
        '/resources/images/dado/dadoRata.png'];
       
        console.log(this.dices);

        $('.RemoveDice1').css("background-image",'url(' + imagenes[this.dices[0]] + ')');
        $('.RemoveDice2').css("background-image",'url(' + imagenes[this.dices[1]] + ')');
        $('.RemoveDice3').css("background-image",'url(' + imagenes[this.dices[2]] + ')');
    }

    crossDice(dice){
        dice.parent().toggleClass("removed");
    }

    showMessage(message, subMessage="") {
        this.messageHolder.children('.content').html(`<p>${message}</p>${(subMessage.length ? `<small>${subMessage}</small>` : '')}`);
        this.messageHolder.removeClass('hidden');
        
        return new Promise(resolve => {
            setTimeout(() => {
                this.messageHolder.addClass('hidden');
                resolve();
            }, 2500);
        });
    }

    showWarning(message) {
        this.wait(()=>{}, 4000, message);
    }

    /*
        Diferentes configuraciones de interfaz según fases de la partida
    */

   /* Ya se han lanzado los dados, pero aún no se han eliminado */
   doomPhase() {
        this.changeBoard(1);
        this.showDices(button => {
            button.click(() => {
                this.wait(() => {
                    this.onDoomPhase();
                }, 4000).then(() => {
                    this.doomRemovePhase();
                });
            });
        });
   }
   
   doomRemovePhase() {
        this.diceElement.addClass('hidden');
        this.loadDicesToCross();
        this.removeDicesElement.removeClass("hidden");
        this.changeBoard(1);
   }
   
    actionFirst(response) {
        if(response!==undefined) {
            this.updateUser(response.playersTurn.user.username, response.playersTurn.coins);

            this.removeDicesElement.addClass('hidden');
            this.wait(() => {
                    this.changeBoard(1);
            }, 2000, 'Movimientos en los tableros realizados', 'Retroceden fichas de guerra y estado de ciudad').then(() => {
                    this.showMessage('Primer turno de acción').then(() => {
                        this.changeBoard(3);
                        this.actionBoard = new ActionBoard(0, this.onButtonsLoad);
                    });
            });
        } else {
            this.changeBoard(3);
            this.actionBoard = new ActionBoard(0, this.onButtonsLoad);
        }
    }
    actionSecond(response) {
        if(response!==undefined) {
            this.updateUser(response.playersTurn.user.username, response.playersTurn.coins);

            this.removeDicesElement.addClass('hidden');
            this.wait(() => {
                    this.changeBoard(1);
            }, 2000, 'Movimientos en los tableros realizados', 'Retroceden fichas de guerra y estado de ciudad').then(() => {
                    this.showMessage('Segundo turno de acción').then(() => {
                        this.changeBoard(3);
                        this.actionBoard = new ActionBoard(1, this.onButtonsLoad);
                    });
            });
        } else {
            this.changeBoard(3);
            this.actionBoard = new ActionBoard(1, this.onButtonsLoad);
        }
    }

    actionCuria(response){
        var plusOrMinus= response.plusOrMinus;
        var section= response.sectionCuria;

        console.log("section: "+section);
        console.log("plusOrMinus: "+plusOrMinus);

        var add=-1;
        if(plusOrMinus){
            add=1;
        }

        switch(section){
            case 1:
                this.curiaInterface.curiaLeft+=add
                if(this.curiaInterface.curiaLeft>3 || this.curiaInterface.curiaLeft<0)
                this.curiaInterface.curiaLeft-=add;
                break;
            case 2:
                this.curiaInterface.curiaMid+=add
                if(this.curiaInterface.curiaMid>3 || this.curiaInterface.curiaMid<0)
                this.curiaInterface.curiaMid-=add;
                break;
            case 3:
                this.curiaInterface.curiaRight+=add
                if(this.curiaInterface.curiaRight>3 || this.curiaInterface.curiaRight<0)
                this.curiaInterface.curiaRight-=add;
        }
        this.curiaInterface.printCuria(this.curiaInterface.curiaLeft,this.curiaInterface.curiaMid,this.curiaInterface.curiaRight);
        

    }
    setInterface(interfaceName) {
        this.interface = interfaceName;
    }
    interfaceOptions(response) {
        let player = response.playersTurn;

        console.log(response.phase.state.name);

        switch(response.phase.state.name) {
            case 'LimitesFirst':
                this.showMessage("Primera acción de Limites", "Elige 2 movimientos en 1 facción, o 1 movimiento en 3 facciones").then(() => {
                    this.changeBoard(1);
                    this.warElection = new WarElection(this.onWarElection);
                });
                break;

            case 'LimitesSecond':
                this.showMessage("Segunda acción de Limites", "Repite el turno de limites.").then(() => {
                    this.changeBoard(1);
                    this.warElection = new WarElection(this.onWarElectionSecond);
                });
                break;

            case 'AerariumFirst':
                this.showMessage("Primera acción de Aerarium", `El jugador ${player.user.username} pasa a tener ${player.coins} y la tesorería se reduce a ${response.treasury}`).then(() => {
                    this.changeBoard(3);
                });
                break;

            case 'AerariumSecond':
                this.showMessage("Segunda acción de Aerarium", `El jugador ${player.user.username} recibe la tesorería al completo. lanzará los dados y retrocederá por cada cara en estados de economía y salud de la ciudad.`).then(() => {
                    this.changeBoard(3);
                    
                });
                break;
            case 'DomusFirst':
                this.showMessage("Primera acción de Domus Palatina", `Jugador ${player.user.username} avanza un estado de ciudad`).then(() => {
                    this.changeBoard(2);
                });
                break;
            case 'DomusSecond':
                this.showMessage("Segunda acción de Domus Palatina", `Jugador ${player.user.username} recibe 2 monedas`).then(() => {
                    this.changeBoard(2);
                });
                break;
            case 'ThermaeFirst':
                this.showMessage("Primera acción de Thermae", `Jugador ${player.user.username} compra una carta de Thermae`).then(() => {
                    this.changeBoard(3);
                });
                break;
            case 'ThermaeSecond':
                this.showMessage("Segunda acción de Thermae", `Jugador ${player.user.username} compra una segunda carta de Thermae`).then(() => {
                    this.changeBoard(3);
                });
                break;
            case 'ForumFirst':
                this.showMessage("Primera acción de Forum", `Jugador ${player.user.username} compra una carta de Forum`).then(() => {
                    this.changeBoard(3);
                });
                break;
            case 'ForumSecond':
                this.showMessage("Segunda acción de Forum", `Jugador ${player.user.username} compra una segunda carta de Forum`).then(() => {
                    this.changeBoard(3);
                });
                break;
            case 'CuriaFirst':
                this.showMessage("Primera acción de Curia", `Jugador ${player.user.username} altera en 1 el senado`).then(() => {
                    this.changeBoard(3);
                    console.log("callate");
                    this.curiaElection.showCuria(true);
                });
                break;
            case 'CuriaSecond':
                this.showMessage("Segunda acción de Curia", `Jugador ${player.user.username} altera en 1 el senado`).then(() => {
                    this.changeBoard(3);
                });
                if(response.sectionCuria!=null){
                    this.actionCuria(response);
                }
                break;
            case 'EndTurn':
                this.showMessage("Fin de tu turno");
                this.curiaElection.showCuria(false);
                if(response.sectionCuria!=null){
                    this.actionCuria(response);
                }
                
                break;
            default:
                this.showWarning("La fase del turno no existe");
        }
    }
    move(piecesToMove, forwardOrBack){
        console.log(piecesToMove);
        for(var i=0; i<piecesToMove.length; i++){
            if(piecesToMove[i]<4){ // Si el indice va de 0 a 3, es del tablero de guerra
                if(forwardOrBack){
                    avanzaWarBoard(piecesToMove[i]);
                }else{
                    retrocedeWarBoard(piecesToMove[i]);
                }
            }else if(piecesToMove[i]<7){ // Si el indice va de 4 a 7, es del tablero de estado de ciudad
                if(forwardOrBack){
                    avanzaCityStateBoard(piecesToMove[i]-4);
                }else{
                    retrocedeCityStateBoard(piecesToMove[i]-4);
                }
            }
        }

    }
    movePieces(piecesGoForward, piecesGoBack){
        /*
        Se llamará respectivamente a las funciones de avanzar o retroceder de cada tablero según las fichas a mover
        formato de inputs: Lista de índices de fichas a mover, una lista para avanzar, otra para retroceder.
        Funciones: avanzaWarBoard(indice) - avanzaCityStateBoard(indice) - RetrocedeWarBoard(indice) - RetrocedeCityStateBoard(indice)
        */
        if(piecesGoForward!==null) {
            this.move(piecesGoForward, true); // El boolean decide si avanza o retrocede
        }
        console.log(piecesGoBack);
        if(piecesGoBack!==null) {
            this.move(piecesGoBack, false);
        }
    }
    updateUser(name,coins){
        for(let a of this.players) {
            if(a.getAttribute('name')==name){
                this.updateCoins(a, coins);
                break;
            }
        }
    }


    updateCoins(player, coins) {
        console.log(player);
        let monedas = $(player).find('#containerMonedas');
        monedas.html('');
        for(let i=0; i<coins; i++) {
            monedas.append($('<div id="moneda"></div>'));
        }
    }
}