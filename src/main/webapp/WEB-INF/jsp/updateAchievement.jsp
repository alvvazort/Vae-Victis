<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
            

            
<div class="menu">
    <div class="container2">
        <div class="inner">
            <div id="divEstadisticas"> <!-- Div principal-->
            <ul>
            <br><br>
                <li>
                    <span class="titulo">Editar logro</span>
                </li>
            </ul>
            <form:form method="POST" action="/estadisticas/editLogro/">
                <ul>
                    <li>
                        <span class="textoEstadisticas">Nombre del Logro :</span>
                        <select name="name" style=" left: 45vw; width: 150px;" class="cantidadEstadisticas"> 
                            <c:forEach items="${logros}" var="logro">
                                <option value="${logro.getName()}">${logro.getName()}</option>
                            </c:forEach>
                        </select>
                    </li>
                </ul>
                <ul>
                    <li>
                        <span class="textoEstadisticas">Nueva Condicion del logro :</span>
                        <input type="number" name="achievementCondition" style="opacity: 0.7; left: 45vw; width: 150px;" class="cantidadEstadisticas"> </input> 
                    </li>
                </ul>
                <input class="btn btn-lg buttonlog " type="submit" value="Editar"/>
            </form:form>
            <br><br>
            </div>
        </div>
    </div>
</div>

          

            
</petclinic:layout>
