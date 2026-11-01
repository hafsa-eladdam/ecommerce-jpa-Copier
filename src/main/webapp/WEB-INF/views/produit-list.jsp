<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Vitrine</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        a.btn { padding: 6px 12px; background: #2d7ff9; color: white; text-decoration: none; border-radius: 4px; }
        .box { margin-top: 20px; padding: 10px; background: #f7f7f7; }
    </style>
</head>
<body>
    <h2>Vitrine : liste des produits</h2>

    <p>
        <a href="${pageContext.request.contextPath}/">Accueil</a> |
        <a href="${pageContext.request.contextPath}/PanierServlet?action=voir">Voir mon panier</a> |
        <a href="${pageContext.request.contextPath}/InternauteServlet?action=connexion">Connexion</a>
    </p>

    <a class="btn" href="${pageContext.request.contextPath}/ProduitServlet?action=nouveau">+ Nouveau produit</a>

    <table>
        <tr>
            <th>ID</th><th>Nom</th><th>Description</th><th>Prix</th>
            <th>Stock</th><th>Catégorie</th><th>Actions</th>
        </tr>
        <c:forEach var="p" items="${produits}">
            <tr>
                <td>${p.id}</td>
                <td>${p.nom}</td>
                <td>${p.description}</td>
                <td>${p.prix}</td>
                <td>${p.stock}</td>
                <td>${p.categorie.nom}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/PanierServlet?action=ajouter&produitId=${p.id}">Ajouter au panier</a> |
                    <a href="${pageContext.request.contextPath}/ProduitServlet?action=modifier&id=${p.id}">Modifier</a> |
                    <a href="${pageContext.request.contextPath}/ProduitServlet?action=supprimer&id=${p.id}"
                       onclick="return confirm('Supprimer ce produit ?')">Supprimer</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <div class="box">
        <b>Ajouter une catégorie :</b>
        <form action="${pageContext.request.contextPath}/ProduitServlet" method="post">
            <input type="hidden" name="action" value="ajouterCategorie">
            <input type="text" name="nom" placeholder="Nom de la catégorie" required>
            <button type="submit">Ajouter</button>
        </form>
        <p>Catégories existantes :
            <c:forEach var="c" items="${categories}">[${c.nom}] </c:forEach>
        </p>
    </div>
</body>
</html>