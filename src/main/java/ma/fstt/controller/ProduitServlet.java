package ma.fstt.controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ma.fstt.entities.Categorie;
import ma.fstt.entities.Produit;
import ma.fstt.util.JPAUtil;

@WebServlet("/ProduitServlet")
public class ProduitServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "lister";
        }

        EntityManager em = JPAUtil.getEntityManager();

        if ("lister".equals(action)) {
            List<Produit> produits = em.createQuery("SELECT p FROM Produit p", Produit.class).getResultList();
            List<Categorie> categories = em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
            request.setAttribute("produits", produits);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/produit-list.jsp").forward(request, response);

        } else if ("nouveau".equals(action)) {
            List<Categorie> categories = em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/produit-form.jsp").forward(request, response);

        } else if ("modifier".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Produit produit = em.find(Produit.class, id);
            List<Categorie> categories = em.createQuery("SELECT c FROM Categorie c", Categorie.class).getResultList();
            request.setAttribute("produit", produit);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/produit-form.jsp").forward(request, response);

        } else if ("supprimer".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            em.getTransaction().begin();
            Produit produit = em.find(Produit.class, id);
            if (produit != null) {
                em.remove(produit);
            }
            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/ProduitServlet?action=lister");
        }

        em.close();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        if ("ajouterCategorie".equals(action)) {
            Categorie c = new Categorie();
            c.setNom(request.getParameter("nom"));
            em.persist(c);

        } else if ("ajouter".equals(action) || "modifier".equals(action)) {
            Produit p = new Produit();
            p.setNom(request.getParameter("nom"));
            p.setDescription(request.getParameter("description"));
            p.setPrix(Double.parseDouble(request.getParameter("prix")));
            p.setStock(Integer.parseInt(request.getParameter("stock")));

            Long categorieId = Long.parseLong(request.getParameter("categorieId"));
            p.setCategorie(em.find(Categorie.class, categorieId));

            if ("ajouter".equals(action)) {
                em.persist(p);
            } else {
                p.setId(Long.parseLong(request.getParameter("id")));
                em.merge(p);
            }
        }

        em.getTransaction().commit();
        em.close();

        response.sendRedirect(request.getContextPath() + "/ProduitServlet?action=lister");
    }
}