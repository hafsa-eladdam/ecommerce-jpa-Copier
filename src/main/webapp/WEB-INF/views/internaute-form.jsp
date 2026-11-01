<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire internaute</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 10px; }
        input { padding: 6px; width: 300px; }
        button { margin-top: 15px; padding: 8px 16px; }
    </style>
</head>
<body>
    <h2>${empty internaute ? 'Inscription' : 'Modifier'} d'un internaute</h2>

    <form action="${pageContext.request.contextPath}/InternauteServlet" method="post">
        <input type="hidden" name="action" value="${empty internaute ? 'ajouter' : 'modifier'}">
        <input type="hidden" name="id" value="${internaute.id}">

        <label>Nom</label>
        <input type="text" name="nom" value="${internaute.nom}" required>

        <label>Prénom</label>
        <input type="text" name="prenom" value="${internaute.prenom}" required>

        <label>Email</label>
        <input type="email" name="email" value="${internaute.email}" required>

        <label>Mot de passe</label>
        <input type="password" name="motDePasse" value="${internaute.motDePasse}" required>

        <br>
        <button type="submit">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/InternauteServlet?action=lister">Annuler</a>
    </form>
</body>
</html>