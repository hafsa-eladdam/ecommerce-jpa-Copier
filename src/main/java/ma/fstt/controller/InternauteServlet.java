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
import ma.fstt.entities.Panier;
import ma.fstt.util.JPAUtil;

@WebServlet("/InternauteServlet")
public class InternauteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ---------- GET : afficher des pages ----------
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "lister";
        }

        EntityManager em = JPAUtil.getEntityManager();

        if ("lister".equals(action)) {
            List<Internaute> internautes = em.createQuery("SELECT i FROM Internaute i", Internaute.class).getResultList();
            request.setAttribute("internautes", internautes);
            request.getRequestDispatcher("/WEB-INF/views/internaute-list.jsp").forward(request, response);

        } else if ("nouveau".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/internaute-form.jsp").forward(request, response);

        } else if ("modifier".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            request.setAttribute("internaute", em.find(Internaute.class, id));
            request.getRequestDispatcher("/WEB-INF/views/internaute-form.jsp").forward(request, response);

        } else if ("supprimer".equals(action)) {
            Long id = Long.parseLong(request.getParameter("id"));
            em.getTransaction().begin();

            // on supprime d'abord le panier de cet internaute (et ses lignes)
            List<Panier> paniers = em.createQuery(
                    "SELECT p FROM Panier p WHERE p.internaute.id = :id", Panier.class)
                    .setParameter("id", id).getResultList();
            for (Panier p : paniers) {
                em.remove(p);
            }
            Internaute i = em.find(Internaute.class, id);
            if (i != null) {
                em.remove(i);
            }
            em.getTransaction().commit();
            response.sendRedirect(request.getContextPath() + "/InternauteServlet?action=lister");

        } else if ("connexion".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/views/connexion.jsp").forward(request, response);

        } else if ("deconnexion".equals(action)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/InternauteServlet?action=connexion");
        }

        em.close();
    }

    // ---------- POST : enregistrer depuis un formulaire ----------
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        EntityManager em = JPAUtil.getEntityManager();

        if ("ajouter".equals(action)) {
            em.getTransaction().begin();

            Internaute i = new Internaute();
            i.setNom(request.getParameter("nom"));
            i.setPrenom(request.getParameter("prenom"));
            i.setEmail(request.getParameter("email"));
            i.setMotDePasse(request.getParameter("motDePasse"));
            em.persist(i);

            // chaque internaute reçoit son panier
            Panier panier = new Panier();
            panier.setDateCreation(LocalDate.now());
            panier.setInternaute(i);
            em.persist(panier);

            em.getTransaction().commit();
            em.close();
            response.sendRedirect(request.getContextPath() + "/InternauteServlet?action=lister");

        } else if ("modifier".equals(action)) {
            em.getTransaction().begin();

            Internaute i = em.find(Internaute.class, Long.parseLong(request.getParameter("id")));
            i.setNom(request.getParameter("nom"));
            i.setPrenom(request.getParameter("prenom"));
            i.setEmail(request.getParameter("email"));
            i.setMotDePasse(request.getParameter("motDePasse"));

            em.getTransaction().commit();
            em.close();
            response.sendRedirect(request.getContextPath() + "/InternauteServlet?action=lister");

        } else if ("connexion".equals(action)) {
            String email = request.getParameter("email");
            String motDePasse = request.getParameter("motDePasse");

            List<Internaute> resultat = em.createQuery(
                    "SELECT i FROM Internaute i WHERE i.email = :email AND i.motDePasse = :mdp", Internaute.class)
                    .setParameter("email", email)
                    .setParameter("mdp", motDePasse)
                    .getResultList();
            em.close();

            if (resultat.isEmpty()) {
                request.setAttribute("erreur", "Email ou mot de passe incorrect");
                request.getRequestDispatcher("/WEB-INF/views/connexion.jsp").forward(request, response);
            } else {
                request.getSession().setAttribute("internaute", resultat.get(0));
                response.sendRedirect(request.getContextPath() + "/ProduitServlet?action=lister");
            }
        }
    }
}