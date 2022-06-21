class GameTurn {
    constructor() {
        this.socket = undefined;
        this.socketDice = undefined;

        this.stompcli = undefined;
        this.stompcliDice = undefined;

        this.turnInterval = undefined;

        this.playersIn = undefined; // Jugadores en el juego
        this.playerTurn = undefined; // Turno del jugador

        this.player = undefined;
        this.discard=false;

        this.winner=undefined;

        this.phase = undefined;

        $(document).ready(() => {
            this.interface = new GameInterface(() => {
                this.sendTurn({
                    player: this.player
                });
            }, (actionBoard) => {
                // on First AcionButtons enabled
                actionBoard.buttons.click(() => {
                    this.sendTurn({
                        player: this.player,
                        actions: actionBoard.turn[0]
                    });
                })
            }, limitesOption => {
                this.sendTurn({
                    player: this.player,
                    limitesOption: limitesOption
                });
            }, limitesOption => {
                console.log("holaaaa");
                console.log(limitesOption);
                this.interface.showMessage('Tira los dados', 'Ahora repite la tirada. Avanzarás las guerras según la cantidad de movimientos de una pieza por acierto en los dados.').then(() => {
                    this.interface.showDices(button => {
                        button.click(() => {
                            this.sendTurn({
                                player: this.player,
                                limitesOption: limitesOption
                            });
                        });
                    });
                });
            });
            
            this.initGame();
            this.initElementEvents();
        });
    }
    
    initElementEvents() {    
        const that = this;

        $('.card').click(function(){
            const a = $(this).attr('cardId');
                that.sendTurn({
                    discard:that.discard,
                    cardId: a,
                    player: this.player
                });
            that.discard=false;
        });

        $('.ButtonRemove[data-action="removeDices"]').click(() => {
            const removedDices = this.interface.getDeletedDices();

            this.interface.wait(() => {
                this.sendTurn({
                    player: this.player,
                    removeDices: removedDices
                });
            }, 4000).then(() => {
                this.interface.actionFirst();
            });
        });
        $('.ButtonCuria[data-action="actionCuria"]').click(() => {
            var selected= this.interface.getCuriaSelected();
            console.log("selected:"+selected);

            this.interface.wait(() => {
                this.sendTurn({
                    //phase: "CuriaFirst",
                    player: this.player,
                    selectedCuria: selected
                });
            }, 4000);
        });
    }

    /*
        Función que mueve las fichas, tanto en la base de datos, como en la partida de todos los jugadores.
        Inputs: 
            - piecesForward= Lista de indices de las fichas que van a avanzar
            - piecesBack= Lista de indices de las fichas que van a retroceder
            Ej: piecesForward= [1,2,2,2] de esta forma se movera hacia delante la pieza 1 y la pieza 2 se moverá 3 veces.
            Índices:
                - 0: HISPANIA
                - 1: GALLIA
                - 2: BRITANNIA
                - 3: GERMANIA
                - 4: DIVITIAE
                - 5: SANITAS
                - 6: RAPINA
    */
    moveGamePieces(piecesForward, piecesBack) {
        this.sendTurn({
            gameId: this.gameId,
            player: this.player,
            piecesGoForward: piecesForward,
            piecesGoBack: piecesBack
        });
    }

    initGame() {
        this.player = $('[data-username]').text();

        this.socket = new SockJS('/game');

        this.stompcli = Stomp.over(this.socket);

        this.loadGame().then(() => {
            console.log("Partida cargada");

            this.stompcli.subscribe('/game/turn', response => {
                response = JSON.parse(response.body);

                console.log("turno ", response);
                if(response.warning==null) {
                    if(response.cardUsedId!=null){
                        if(!response.discard){
                            this.interface.useCard(response.cardUsedIndex,response);
                        }
                        
                        this.interface.deletecard(response.cardUsedId);
                    }

                    this.winner=response.winner;

                    if(this.winner!=null && this.winner!= undefined){ //Partida ganada por un jugador
                        this.interface.showMessage("HA GANADO: "+this.winner.username);

                    }
                    const phaseName = response.turn.phase.state.name;
                    this.isPlayer = this.player == response.playersTurn.user.username;
                    
                    if(response.cityStateEvent!=null){
                        console.log("Evento:"+response.cityStateEvent)
                        this.interface.applyEvent(response);
                        
                    }
                    this.interface.movePieces(response.piecesGoForward,response.piecesGoBack);
                    
                    console.log(phaseName);
                    switch(phaseName) {
                        case "DoomDiceDelete":
                            this.interface.throwDices(response.diceResult);

                            if(!this.isPlayer) {
                                    this.interface.wait(() => {}, 4000).then(() => {
                                        this.interface.doomRemovePhase();
                                    });
                            }
                            break;
                        case "ActionFirst":
                            this.interface.actionFirst(response);
                            break;

                        case "ActionSecond":
                            if(response.diceResult!==null) {
                                this.interface.throwDices(response.diceResult);
                                setTimeout(() => {
                                    this.interface.diceElement.addClass('hidden');
                                }, 3000);
                            }
                            this.interface.actionSecond(response);
                            break;

                        default:
                            this.interface.interfaceOptions(response);
                            break;
                    }
                } else {
                    this.interface.showWarning(response.warning);
                }
            });
            
        });
    }
    sendTurn(turnData) {
        this.stompcli.send("/app/game", {}, JSON.stringify(turnData));
    }
    loadGame() {
        return new Promise((resolve, reject) => {
            this.stompcli.connect({}, frame => {
                let suscription = this.stompcli.subscribe('/users/game/load', response => {
                    const res = JSON.parse(response.body);
                    console.log("Respuesta JSON LoadGame", res);

                    if(res.isError!==true) {
                        this.playersIn = res.playerIn; // Jugadores en el juego
                        this.playersTurn = res.playersTurn; // Turno del jugador
                        this.playerUser = this.playersTurn.player.user; //jugador de este turno

                        this.phase = res.phase.state;
                        this.interface.dices = res.diceResult;

                        this.winner=res.winner;
                        if(this.winner!=null && this.winner!= undefined){ //Partida ganada por un jugador
                            this.interface.showMessage("HA GANADO: "+this.winner.username);
                            resolve();
                        }
                        
                        this.interface.showMessage(`Turno de ${this.playerUser.username}`, `Fase: ${this.phase.phaseName}`).then(() => {
                            switch(this.phase.phaseName){
                                case "DoomDiceThrow":
                                    this.interface.doomPhase();
                                    break;
                                case "DoomDiceDelete":
                                    this.interface.doomRemovePhase();
                                    break;
                                case "ActionFirst":
                                    this.interface.actionFirst();
                                    break;
                                case "ActionSecond":
                                    this.interface.actionSecond();
                                    break;
                                default:
                                    this.interface.interfaceOptions(res);
                                    break;
                            }

                            suscription.unsubscribe();
                            resolve();
                        });
                        
                    } else {
                        reject(res.error);
                    }
                });
            });
        });
    }

    romanNumber(index) {
        return ['I', 'II', 'III', 'IV', 'V', 'VI'][index];
    }
    phaseToNumber(phase){ //Esto por un momento tuvo utilidad pero lugo no me hizo falta
        var phaseNum;
        switch(phase){
            case "Doom":
                phaseNum=1;
                break;
            case "Support":
                phaseNum=2;
                break;
            case "Influence":
                phaseNum=3;
                break;
        }
        return phaseNum;
    }
    phaseToBoard(phase){
        var phaseNum;
        switch(phase){
            case "Doom":
                phaseNum=1;
                break;
            default:
                phaseNum=3;
        }
        return phaseNum;
    }
}

const game = new GameTurn();