<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
    <link href='http://fonts.googleapis.com/css?family=Ubuntu' rel='stylesheet' type='text/css'>
    <%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/static/css/general.css" />"/>--%>
    <link rel="stylesheet" href="/css/general.css">

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Invalid Player Names</title>
</head>

<body>
<div class="fullPageCenter">
    <h3>Please enter player names with a minimum length of 2 characters</h3>
    <table>
        <tr>
            <td><a href="/playerSetup.do">Retry</a></td>
        </tr>
    </table>
</div>

</body>

</html>