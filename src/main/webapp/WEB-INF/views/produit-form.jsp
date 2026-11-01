<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire produit</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 10px; }
        input, select { padding: 6px; width: 300px; }
        button { margin-top: 15px; padding: 8px 16px; }
    </style>
</head>
<body>
    <h2>${empty produit ? 'Ajouter' : 'Modifier'} un produit</h2>

    <form action="${pageContext.request.contextPath}/ProduitServlet" method="post">
        <input type="hidden" name="action" value="${empty produit ? 'ajouter' : 'modifier'}">
        <input type="hidden" name="id" value="${produit.id}">

        <label>Nom</label>
        <input type="text" name="nom" value="${produit.nom}" required>

        <label>Description</label>
        <input type="text" name="description" value="${produit.description}">

        <label>Prix</label>
        <input type="number" step="0.01" min="0" name="prix" value="${produit.prix}" required>

        <label>Stock</label>
        <input type="number" min="0" name="stock" value="${produit.stock}" required>

        <label>Catégorie</label>
        <select name="categorieId" required>
            <c:forEach var="c" items="${categories}">
                <option value="${c.id}" ${c.id == produit.categorie.id ? 'selected' : ''}>${c.nom}</option>
            </c:forEach>
        </select>

        <br>
        <button type="submit">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/ProduitServlet?action=lister">Annuler</a>
    </form>
</body>
</html>