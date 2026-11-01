package ma.fstt.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.fstt.entities.Internaute;
import ma.fstt.entities.LignePanier;
import ma.fstt.entities.Panier;
import ma.fstt.entities.Produit;
import ma.fstt.util.JPAUtil;

@WebServlet("/PanierServlet")
public class PanierServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // retrouve le panier de l'internaute (ou le crée s'il n'existe pas)
    private Panier trouverPanier(EntityManager em, Internaute internaute) {
        List<Panier> liste = em.createQuery(
                "SELECT p FROM Panier p WHERE p.internaute.id = :id", Panier.class)
                .setParameter("id", internaute.getId())
                .getResultList();

        if (!liste.isEmpty()) {
            return liste.get(0);
        }

        em.getTransaction().begin();
        Panier panier = new Panier();
        panier.setDateCreation(LocalDate.now());
        panier.setInternaute(em.find(Internaute.class, internaute.getId()));
        em.persist(panier);
        em.getTransaction().commit();
        return panier;
    }

    // ---------- GET : voir, ajouter, retirer, vider ----------
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // il faut être connecté
        Internaute internaute = (Internaute) request.getSession().getAttribute("internaute");
        if (internaute == null) {
            response.sendRedirect(request.getContextPath() + "/InternauteServlet?action=connexion");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "voir";
        }

        EntityManager em = JPAUtil.getEntityManager();
        Panier panier = trouverPanier(em, internaute);

        if ("voir".equals(action)) {
            panier.getLignes().size();
            request.setAttribute("panier", panier);
            request.getRequestDispatcher("/WEB-INF/views/panier.jsp").forward(request, response);

        } else if ("ajouter".equals(action)) {
            Long produitId = Long.parseLong(request.getParameter("produitId"));
            em.getTransaction().begin();

            // le produit est-il déjà dans le panier ?
            LignePanier existante = null;
            for (LignePanier l : panier.getLignes()) {
                if (l.getProduit().getId().equals(produitId)) {
                    existante = l;
                }
            }

            if (existante != null) {
                existante.setQuantite(existante.getQuantite() + 1);
            } else {
                LignePanier ligne = new LignePanier();
                ligne.setProduit(em.find(Produit.class, produitId));
                ligne.setQuantite(1);
                ligne.setPanier(panier);
                panier.getLignes().add(ligne);
                em.persist(ligne);
            }
            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/PanierServlet?action=voir");

        } else if ("retirer".equals(action)) {
            Long ligneId = Long.parseLong(request.getParameter("ligneId"));
            em.getTransaction().begin();
            LignePanier ligne = em.find(LignePanier.class, ligneId);
            if (ligne != null) {
                panier.getLignes().remove(ligne);
                em.remove(ligne);
            }
            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/PanierServlet?action=voir");

        } else if ("vider".equals(action)) {
            em.getTransaction().begin();
            panier.getLignes().clear();
            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/PanierServlet?action=voir");
        }

        em.close();
    }

    // ---------- POST : modifier la quantité ----------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Internaute internaute = (Internaute) request.getSession().getAttribute("internaute");
        if (internaute == null) {
            response.sendRedirect(request.getContextPath() + "/InternauteServlet?action=connexion");
            return;
        }

        String action = request.getParameter("action");

        if ("modifierQuantite".equals(action)) {
            Long ligneId = Long.parseLong(request.getParameter("ligneId"));
            int quantite = Integer.parseInt(request.getParameter("quantite"));

            EntityManager em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            LignePanier ligne = em.find(LignePanier.class, ligneId);
            if (ligne != null && quantite > 0) {
                ligne.setQuantite(quantite);
            }
            em.getTransaction().commit();
            em.close();
        }

        response.sendRedirect(request.getContextPath() + "/PanierServlet?action=voir");
    }
}