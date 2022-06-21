<%@ attribute name="gameBoard" required="false" rtexprvalue="true" type="vaevictis.game.boards.war.GameBoard"
description="Game Board to be rendered" %>
<%@ attribute name="urlBackground" required="false"%>
<%@ attribute name="typeBoard" required="false"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<canvas id="canvas${typeBoard}" width="600" height="600" ></canvas>
<img id="source${typeBoard}" src="${urlBackground}" style="display:none;">

<c:forEach var="i" begin="0" end="${gameBoard.pieces.size()-1}">
    <img id="${gameBoard.pieces[i].type}" src="${gameBoard.pieces[i].url}" style="display:none; ">
</c:forEach>

<script>

var canvas = document.getElementById("canvas${typeBoard}");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source${typeBoard}'); 
const fichas${typeBoard} = [];
image.onload = drawBoard${typeBoard};

function generatePositions(type) {
    let positionsX=[];
    let positionsY=[];
    let positions=[];

    switch(type){
        case "HISPANIA":
            positionsX= [120,164,198,232,268,235,202,163,116,68,28,28,100];
            positionsY= [370,328,295,261,225,190,155,187,187,187,150,93,73];
            break;
        case "GALLIA":
            positionsX= [158,198,234,267,300,335,301,301,330,372,372,405,450];
            positionsY= [410,366,330,294,260,228,188,140,105,73,120,153,195];
            break;
        case "BRITANNIA":
            positionsX= [192,236,269,302,335,465,520,520,520,520,485,454,454];
            positionsY= [447,403,368,333,298,288,300,250,200,150,115,83,34];
            break;
        case "GERMANIA":
            positionsX= [227,271,304,337,370,420,390,380,380,426,478,517,501];
            positionsY= [481,437,402,367,332,390,420,465,518,518,518,487,425];
            break;
        case "DIVITIAE":
            positionsX= [68,110,68,110,68,110,68,110,68,110].reverse();
            positionsY= [125,170,215,261,306,351,396,442,487,532].reverse();
            break;
        case "SANITAS":
            positionsX= [258,300,258,300,258,300,258,300,258,300].reverse();
            positionsY= [125,170,215,261,306,351,396,442,487,532].reverse();
            break;
        case "RAPINA":
            positionsX= [440,482,440,482,440,482,440,482,440,482].reverse();
            positionsY= [125,170,215,261,306,351,396,442,487,532].reverse();
            break;
    }
    //console.log("positionsX:");
    //console.log(positionsX);
    positions.push(positionsX);
    positions.push(positionsY);
    return positions;
}

class Ficha${typeBoard}{
    constructor(type,img,index){
        //console.log(index);
        let positions=generatePositions(type);
        //console.log(positions);
        this.x=positions[0];
        this.y=positions[1];

        this.img = img;
        this.index = index
    }
    draw(){
        var canvas = document.getElementById("canvas${typeBoard}");
        var ctx = canvas.getContext("2d");
        ctx.drawImage(
            this.img,
            this.x[this.index],
            this.y[this.index],
            50,
            50
        )
        ctx.fillStyle='blue'
        ctx.fill()
    }
}

function drawBoard${typeBoard}(){
    var canvas = document.getElementById("canvas${typeBoard}");
    var ctx = canvas.getContext("2d");
    var image = document.getElementById('source${typeBoard}'); 
    ctx.drawImage(
    image,
    0,
    0,
    canvas.width,
    canvas.height
  );
  console.log("Pintando piezas");
  let ficha;
    <c:forEach var="i" begin="0" end="${gameBoard.pieces.size()-1}">
        var piece${i} = document.getElementById("${gameBoard.pieces[i].type}");
        ficha= new Ficha${typeBoard}(
            "${gameBoard.pieces[i].type}",
            piece${i},
            ${gameBoard.pieces[i].index});
        fichas${typeBoard}.push(ficha);
        ficha.draw();
    
     </c:forEach>


  
}

function avanza${typeBoard}(a){
    if("${typeBoard}"=="WarBoard" && (fichas${typeBoard}[a].index==12 || fichas${typeBoard}[a].index==0)){
        fichas${typeBoard}[a].index--;
    }
    fichas${typeBoard}[a].index++;
    
    repaint${typeBoard}();
}


function retrocede${typeBoard}(a){
    if("${typeBoard}"=="WarBoard" && (fichas${typeBoard}[a].index==12 || fichas${typeBoard}[a].index==0)){
        fichas${typeBoard}[a].index++;
    }
    fichas${typeBoard}[a].index--;
    console.log(fichas${typeBoard}[a].index);
    if("${typeBoard}"=="CityStateBoard" && fichas${typeBoard}[a].index==8){
        fichas${typeBoard}[(a+1)%3].index--;
        
        var messageHolder = $('.gameMessage');
            
        setTimeout(() => {
            var message = 'Como una ficha ha caido en la segunda casilla, otra ficha ha descendido también';
            messageHolder.children('.content').text(message);
            messageHolder.removeClass('hidden');
            
            setTimeout(() =>{
                messageHolder.addClass('hidden');
            },3000)
        }, 5000);
    }

    
    
    repaint${typeBoard}();
}

function repaint${typeBoard}(){
    var canvas = document.getElementById("canvas${typeBoard}");
    var ctx = canvas.getContext("2d");
    var image = document.getElementById('source${typeBoard}'); 
    
    ctx.clearRect(0,0,canvas.width,canvas.height)
    ctx.clearRect(0,0,canvas.width,canvas.height)
    ctx.drawImage(
    image,
    0,
    0,
    canvas.width,
    canvas.height
  );

    
    fichas${typeBoard}.forEach(ficha => ficha.draw())
}


</script>