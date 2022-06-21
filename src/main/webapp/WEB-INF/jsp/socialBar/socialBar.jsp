<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<button class="botonSocial" id="desplegable" style="z-index: 3;"></button>

<nav class="sideMenu">
<div style="display: flex;">
  
    <i id="close-btn" class="fa fa-caret-left fa-2x side-menu-btn" aria-hidden="true" style="left: 0;"></i>
    <div class="head-line">
        <h1><strong class="txt">Social</strong></h1>

        <a class="side-menu-btn" style="color: white;" href="/"><i class="fa fa-university fa-2x side-menu-btn" aria-hidden="true"></i></a>
        <a class="side-menu-btn" href="/users/profile"><h1><strong class="side-menu-btn" style="color: white;">${showUser}</strong></h1></a>

        
        <form:form action="/logout" method="POST">
            <button type="submit" style="color: red; background: initial; border: initial;" class="btn btn-primary btn-block btn-sm"><i class="fa fa-sign-out fa-2x side-menu-btn" aria-hidden="true"></i></button>
        </form:form>
        
    </div>
</div>
<div class="head-line">
    <a class="btn btn-default btn-edited btn-edited-find txt" href='<spring:url value="/users/find" htmlEscape="true"/>'><h2><strong style="color: white;">Find Users</strong></h2></a>
</div>
<div class="head-line">
    <h1><strong class="txt">Friend Requests</strong></h1>
</div>
<table id="aceptarAmigos" class="aceptarAmigos sideTable">
  <thead>
  <tr>
      <th style="width: 150px;" class="txt">Username</th>
      <th class="txt">Accept<i/th>
        <span></span>
      <th class="txt">Cancel<i/th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${friends}" var="user">
      <tr>
          <td>
              <spring:url value="/users/{username}" var="userUrl">
                  <spring:param name="username" value="${user.username}"/>
              </spring:url>
              <a class = "friendLinkSocialBar" href="${fn:escapeXml(userUrl)}"><c:out value="${user.username}"/></a>
          </td>
          
          
          <td>
              <a class="btn btn-default btn-edited btn-edited-accept txt" href='<spring:url value="/users/${user.username}/addFriend" htmlEscape="true"/>'>Accept petition</a>
          </td>
          <td>
              <a class="btn btn-default btn-edited txt" href='<spring:url value="/users/${user.username}/deleteFriend" htmlEscape="true"/>'>Cancel petition</a>
          </td>
                         
      </tr>
  </c:forEach>
  </tbody>
</table>
<div class="head-line">
    <h1><strong class="txt">Friends</strong></h1>
</div>
<table id="friendsTable" class="aceptarAmigos sideTable">
  <thead>
  <tr>
        <th style="width: 150px;" class="txt">Username</th>
        <th class="txt">Status</th>
        <th class="txt">Delete friend</th>
        <th class="lobby-only txt">Invite friend</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${friendsList}" var="friend">
      <tr>
          <td>
              <spring:url value="/users/{username}" var="userUrl">
                  <spring:param name="username" value="${friend.username}"/>
              </spring:url>
              <a class = "friendLinkSocialBar" href="${fn:escapeXml(userUrl)}"><c:out value="${friend.username}"/></a>
              
          </td>
          <td>
            <c:choose>
                <c:when test="${loggedUsers.contains(friend.username)}">
                    <div class="txt" style="color: green;">
                        Online
                    </div> 
                </c:when>
                <c:otherwise>
                    <div class="txt" style="color: red;">
                        Offline
                    </div>
                </c:otherwise>
            </c:choose>
          </td>
          
          <td>
              <a class="btn btn-default btn-edited txt" href='<spring:url value="/users/${friend.username}/deleteFriend" htmlEscape="true"/>'>Delete friend</a>
          </td>

          <td>
                <c:choose>
                    <c:when test="${!(lobby.isUserInvited(friend.username) || lobby.isUserIn(friend.username))}">
                        <div id="inviteFriendToLobby" data-action="/lobby/invite/${lobby.getId()}" data-username="${friend.getUsername()}">
                            <button class="btn btn-default btn-edited btn-edited-accept txt" type="submit">Invitar</button>
                        </div>
                    </c:when>
                    <c:when test="${lobby.getUsersIn().size() == lobby.getPlayerNumber()}">
                        <button class="btn btn-default btn-edited-accept txt" type="button">Lobby full</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default btn-edited-accept txt" type="button">Invited</button>
                    </c:otherwise>
                </c:choose> 
          </td>
                         
      </tr>
  </c:forEach>
  </tbody>
</table>

<div class="invitaciones">
    <div class="head-line">
        <h1><strong class="txt">Lobby invites</strong></h1>
    </div>
    <c:forEach items="${lobbies}" var="lobby">
        <div class="invitacion">
            <a class="btn btn-default btn-edited btn-edited-accept txt" href="/lobby/${lobby.getId()}">Unirme al Lobby de  ${lobby.getLobbyAdmin().getUsername()}</a>
        </div>
    </c:forEach>
</div>
</nav>


