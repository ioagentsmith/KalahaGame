<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Ubuntu' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/css/general.css" />"/>

    <title>Player Setup</title>
</head>
<body>
<div class="fullPageCenter">
    <h3 class="title">Welcome! Please Enter The Player Names</h3>
    <form:form method="POST" action="/startGame.do" modelAttribute="playerNames">
        <table class="table">
            <tr>
                <td><form:label path="player1Name">First player's name</form:label></td>
                <td><form:input path="player1Name"/></td>
            </tr>
            <tr>
                <td><form:label path="player2Name">Second player's name</form:label></td>
                <td><form:input path="player2Name"/></td>
            </tr>
            <tr>
                <td></td>
                <td><input type="submit" value="Submit"/></td>
            </tr>
        </table>
    </form:form>
</div>
</body>
</html>