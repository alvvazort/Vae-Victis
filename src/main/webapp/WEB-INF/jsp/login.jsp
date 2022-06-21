<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  
  
<petclinic:layout pageName="home">
    
    <div id="todo">
        <form:form method="POST" action="/login" >
        <div class="grupo">
            <div class="marco">
                
                <input type="text" name="username" placeholder="Username"/>
                
            </div>

            <div class="marco">
                <input type="password" name="password" placeholder="Password"/>
            </div>

            <a id="registrate" style="display: block;" href="<c:url value='/users/new' />">&#191;Todavia no eres un patricio? Registrate</a>

            <input class="btn btn-lg buttonlog " type="submit" value="Iniciar sesion"/>

            
        </div>
        
    </form:form>
    <br></br>
    
</div>
</petclinic:layout>
