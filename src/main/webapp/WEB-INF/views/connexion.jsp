<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Connexion</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 10px; }
        input { padding: 6px; width: 300px; }
        button { margin-top: 15px; padding: 8px 16px; }
        .erreur { color: red; }
    </style>
</head>
<body>
    <h2>Connexion</h2>

    <c:if test="${not empty erreur}">
        <p class="erreur">${erreur}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/InternauteServlet" method="post">
        <input type="hidden" name="action" value="connexion">

        <label>Email</label>
        <input type="email" name="email" required>

        <label>Mot de passe</label>
        <input type="password" name="motDePasse" required>

        <br>
        <button type="submit">Se connecter</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/InternauteServlet?action=nouveau">Créer un compte</a></p>
</body>
</html>