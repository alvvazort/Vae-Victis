(($) => {
    class Matchmaking {
        constructor() {

            this.state = 0; // 0 => parado o cancelado, 1 => buscando
            this.socket = undefined;
            this.stompcli = undefined;

            this.searchInterval = undefined;
        }
        toggleButton() {
            let buttons = [$('#startMatchmaking'), $('#cancelMatchmaking')];
            if(buttons[0].is(':visible')) {
                buttons[0].hide();
                buttons[1].show();
            } else {
                buttons[1].hide();
                buttons[0].show();
            }
        }
        romanNumber(index) {
            return ['I', 'II', 'III', 'IV', 'V', 'VI'][index];
        }
        searchGame() {
            this.toggleButton();
            if(this.state===0) {
                this.state = 1;

                this.socket = new SockJS('/lobbymatch');

                this.stompcli = Stomp.over(this.socket);

                this.stompcli.connect({}, frame => {
                    console.log('Buscando partida...', frame);
                    
                    this.stompcli.subscribe('/lobbymatch/matchmaking', LobbySearchs => {
                        const response = JSON.parse(LobbySearchs.body);

                        console.log('Mensaje recibido: ', response);

                        if(response.isError!==true) {
                            if(response.gameCode!==null) {
                                window.location.href = `/game/${response.gameCode}`;
                            } else {
                                const search = response.lobbySearch,
                                    usersList = $('#playersConnected');

                                usersList.html('');
                                let i = 0;
                                for(let p of search.users) {
                                    usersList.append(`
                                    <li>
                                        <div class="row">
                                            <div class="col noFill">
                                                <span class="number">${this.romanNumber(i++)}</span>
                                            </div>
                                            <span class="col">${p.username}<span></span>
                                                </span>
                                        </div>
                                    </li>
                                    `);
                                }

                                /* Testing Purpose
                                const games = response.lobbySearchs,

                                    gamesList = $('#matchesAvaliable');

                                gamesList.children('li').remove();
                                for(let g of games) {
                                    let usersList = '<ul>';
                                    for(let p of g.users) {
                                        usersList += `<li>${p.username}</li>`;
                                    }
                                    usersList += '</ul>';
                                    gamesList.append(`<li>Partida ${this.getLevel(g.gameLevel)} de ${g.userNumber} jugadores. Quedan ${g.usersLeft}. Jugadores: ${usersList}</li>`)
                                }

                                */
                            }
                        } else {
                            alert(response.error);
                        }
                    });

                    this.stompcli.send("/app/matchmaking", {}, JSON.stringify({ 'action': 1 }));
                    
                    //showMessageOutput(JSON.parse(messageOutput.body));

                    $('#mathmakingState').text(this.state ? 'Buscando' : 'No buscando');
                    const timeSearch = $('#timeSearch').text(0);

                    this.searchInterval = setInterval(() => {
                        timeSearch.text(parseInt(timeSearch.text())+1);
                    }, 1000);
                    
                });
            } else {
                alert('Ya estás buscando una partida. Cancela antes');
            }
        }
        getLevel(intLevel) {
            let level = '';
            switch(intLevel) {
                case 1:
                    level = 'Fácil';
                    break;
                case 2:
                    level = 'Medio';
                    break;
                case 3:
                    level = 'difícil';
                    break;
            }
            return level;
        }
        cancelSearch() {
            this.toggleButton();

            if(this.state===1) {
                this.state = 0;
                this.stompcli.send("/app/matchmaking", {}, JSON.stringify({ 'action': 0 }));

                this.stompcli.disconnect();

                $('#mathmakingState').text(this.state ? 'Buscando' : 'No buscando');
                clearInterval(this.searchInterval);
            } else {
                alert('No estás buscando partida');
            }
        }
    }

    const match = new Matchmaking();

    $('#startMatchmaking').click(() => {
        match.searchGame();
    });

    $('#cancelMatchmaking').click(() => {
        match.cancelSearch();
    });

    /* #QuickChapuza */
    if($('#startMatchmaking').length===0) {
        const socket = new SockJS('/lobbymatch');

        const stompcli = Stomp.over(socket);

        stompcli.connect({}, frame => {
            console.log('Buscando partida...', frame);
            
            stompcli.subscribe('/lobbymatch/matchmaking', LobbySearchs => {
                const response = JSON.parse(LobbySearchs.body);

                if(response.isError!==true) {
                    if(response.gameCode!==null) {
                        window.location.href = `/game/${response.gameCode}`;
                    } else {
                        const search = response.lobbySearch,
                            usersList = $('#playersConnected');

                        usersList.html('');
                        let i = 0;
                        for(let p of search.users) {
                            usersList.append(`
                            <li>
                                <div class="row">
                                    <div class="col noFill">
                                        <span class="number">${['I', 'II', 'III', 'IV', 'V', 'VI'][i++]}</span>
                                    </div>
                                    <span class="col">${p.username}<span></span>
                                        </span>
                                </div>
                            </li>
                            `);
                        }
                    }
                } else {
                    alert(error);
                }

            });
        });
    }

})(jQuery);