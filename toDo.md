(ok)  PHASE 2 — INTERFACE SWING (Personne A)
(ok)  Tâches Personne A
(ok)  
(ok)      Modifier l’interface Voyage / Lalana
(ok)  
(ok)          afficher le coût total des réparations
(ok)  
(ok)      Modifier le MainPanel
(ok)  
(ok)          ajouter le bouton “Ouvrir la carte SIG”
(ok)  
(ok)      Créer la logique d’ouverture de la fenêtre SIG
(ok)  
(ok)  📌 Livrable :
(ok)  ✔️ Swing affiche les coûts
(ok)  ✔️ Bouton SIG fonctionnel

🟡 PHASE 3 — SIG MINIMAL (Personne B)
Tâches Personne B

    Créer une JSP minimale (map.jsp)

    Intégrer Leaflet

    Afficher la carte de Madagascar

    Vérifier que la carte s’affiche correctement

📌 Livrable :
✔️ Carte SIG visible (sans données)

🟢 PHASE 4 — ROUTES NATIONALES (RN) SUR LA CARTE
Personne B

    Créer la Servlet RN

    Renvoyer les RN en GeoJSON

    Afficher la liste des RN

    Trier les RN

    Tracer une RN en bleu quand on clique dessus

Personne A (support)

    Vérifier le DAO RN

    Vérifier la cohérence des données PostGIS

📌 Livrable :
✔️ RN visibles, cliquables et colorées en bleu

🔵 PHASE 5 — SIMBA SUR LA CARTE
Personne A

    Créer / adapter la Servlet Simba

    Gérer la récupération des Simba par RN

    Vérifier la logique pkDebut / pkFin

Personne B

    Afficher les Simba en rouge sur la RN

    Ajouter les popups (infos Simba)

📌 Livrable :
✔️ Simba visibles et corrects sur la carte

🟣 PHASE 6 — NAVIGATION & FONCTIONS DEMANDÉES (Partagé)
Tâches partagées

    Fonction afficherRN()

    Fonction colorierRNEnBleu()

    Fonction afficherSimbaEnRouge()

    Fonction fermerFenetreSIG()

    Bouton Fermer → retour au MainPanel

📌 Livrable :
✔️ Navigation fluide entre Swing et SIG
🏁 PHASE 7 — FINALISATION (Partagé)

    Tests complets Swing ↔ SIG

    Nettoyage du code

    Préparation de la démonstration pour le jury

📌 Livrable final :
✔️ Projet fonctionnel, clair et bien structuré
🧭 RÉSUMÉ EXPRESS

    Personne A : Swing + logique métier + Servlets Simba

    Personne B : JSP + Leaflet + affichage RN & Simba

    Ensemble : intégration & tests


osmium cat madagascar-260130.osm.pbf -o data.geojson
