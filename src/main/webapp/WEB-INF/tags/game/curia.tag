<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>
    
    class Curia {

        constructor(){
            this.curiaLeft= ${game.senateLeft};
            this.curiaMid= ${game.senateMid};
            this.curiaRight= ${game.senateRight};

            this.printCuria(this.curiaLeft, this.curiaMid,this.curiaRight);
        }

        printCuria(curiaLeft,curiaMid,curiaRight){
            $('.curia').addClass('hidden');
            for(let i=1; i<=curiaLeft; i++){
                $('#curiaLeft'+i).removeClass('hidden');
            }

            for(let i=1; i<=curiaMid; i++){
                $('#curiaMid'+i).removeClass('hidden');
            }

            for(let i=1; i<=curiaRight; i++){
                $('#curiaRight'+i).removeClass('hidden');
            }
        }

    }


    
</script>