<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mon panier</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 15px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        input[type=number] { width: 60px; padding: 4px; }
        a.btn { padding: 6px 12px; background: #2d7ff9; color: white; text-decoration: none; border-radius: 4px; }
    </style>
</head>
<body>
    <h2>Panier de ${sessionScope.internaute.prenom} ${sessionScope.internaute.nom}</h2>

    <a href="${pageContext.request.contextPath}/ProduitServlet?action=lister">Continuer mes achats</a> |
    <a href="${pageContext.request.contextPath}/InternauteServlet?action=deconnexion">Se déconnecter</a>

    <c:choose>
        <c:when test="${empty panier.lignes}">
            <p>Votre panier est vide.</p>
        </c:when>
        <c:otherwise>
            <table>
                <tr>
                    <th>Produit</th><th>Prix</th><th>Quantité</th><th>Sous-total</th><th>Action</th>
                </tr>
                <c:forEach var="l" items="${panier.lignes}">
                    <tr>
                        <td>${l.produit.nom}</td>
                        <td>${l.produit.prix}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/PanierServlet" method="post">
                                <input type="hidden" name="action" value="modifierQuantite">
                                <input type="hidden" name="ligneId" value="${l.id}">
                                <input type="number" min="1" name="quantite" value="${l.quantite}">
                                <button type="submit">OK</button>
                            </form>
                        </td>
                        <td>${l.sousTotal}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/PanierServlet?action=retirer&ligneId=${l.id}">Retirer</a>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <th colspan="3">Total</th>
                    <th colspan="2">${panier.total}</th>
                </tr>
            </table>
            <p>
                <a class="btn" href="${pageContext.request.contextPath}/PanierServlet?action=vider"
                   onclick="return confirm('Vider le panier ?')">Vider le panier</a>
            </p>
        </c:otherwise>
    </c:choose>
</body>
</html>