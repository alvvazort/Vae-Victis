<%@ attribute name="gamePiece" required="false" rtexprvalue="true" type="vaevictis.game.components.pieces.war.GamePiece"
description="Pieces" %>

<img id="${gamePiece.type}" src="${gamePiece.url}" style="display:none; ">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');
var image2 = document.getElementById('${gamePiece.type}');



image2.onload = function() {
  console.log("pieza");
  ctx.drawImage(
    image2,
    ${gamePiece.getNewPosition(0)[0]},
    ${gamePiece.getNewPosition(0)[1]},
    50,
    50
  );
}

 </script>