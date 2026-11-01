<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Internautes</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        a.btn { padding: 6px 12px; background: #2d7ff9; color: white; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>
    <h2>Liste des internautes</h2>
    <a href="${pageContext.request.contextPath}/">Accueil</a><br><br>
    <a class="btn" href="${pageContext.request.contextPath}/InternauteServlet?action=nouveau">+ Nouvel internaute</a>

    <table>
        <tr><th>ID</th><th>Nom</th><th>Prénom</th><th>Email</th><th>Actions</th></tr>
        <c:forEach var="i" items="${internautes}">
            <tr>
                <td>${i.id}</td>
                <td>${i.nom}</td>
                <td>${i.prenom}</td>
                <td>${i.email}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/InternauteServlet?action=modifier&id=${i.id}">Modifier</a> |
                    <a href="${pageContext.request.contextPath}/InternauteServlet?action=supprimer&id=${i.id}"
                       onclick="return confirm('Supprimer cet internaute ?')">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>