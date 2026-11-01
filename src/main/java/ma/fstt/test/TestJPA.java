package ma.fstt.test;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ma.fstt.entities.Categorie;
import ma.fstt.entities.Internaute;
import ma.fstt.entities.LignePanier;
import ma.fstt.entities.Panier;
import ma.fstt.entities.Produit;

public class TestJPA {

    public static void main(String[] args) {

        // 1. on ouvre la connexion JPA (le nom vient de persistence.xml)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ecommercePU");
        EntityManager em = emf.createEntityManager();

        // 2. on enregistre des données dans une transaction
        em.getTransaction().begin();

        Categorie cat = new Categorie();
        cat.setNom("Informatique");
        em.persist(cat);

        Produit p = new Produit();
        p.setNom("Clavier");
        p.setDescription("Clavier sans fil");
        p.setPrix(150);
        p.setStock(20);
        p.setCategorie(cat);
        em.persist(p);

        Internaute i = new Internaute();
        i.setNom("Eladdam");
        i.setPrenom("Hafsa");
        i.setEmail("hafsa@mail.com");
        i.setMotDePasse("1234");
        em.persist(i);

        Panier panier = new Panier();
        panier.setDateCreation(LocalDate.now());
        panier.setInternaute(i);

        LignePanier ligne = new LignePanier();
        ligne.setProduit(p);
        ligne.setQuantite(2);
        ligne.setPanier(panier);
        panier.getLignes().add(ligne);
        em.persist(panier);

        em.getTransaction().commit();

        System.out.println("Total du panier = " + panier.getTotal());

        // 3. on ferme
        em.close();
        emf.close();
    }
}