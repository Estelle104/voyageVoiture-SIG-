🔴 PHASE 0 — PÉRIMÈTRE & NETTOYAGE

Mettre InfoVoyage hors périmètre (temporairement).

Mettre Lavaka hors périmètre (temporairement).

Se concentrer uniquement sur :

Routes Nationales (RN)

Simba

SIG

Calcul du coût de réparation.

🟠 PHASE 1 — BASE DE DONNÉES (PostgreSQL / PostGIS)

Modifier la table Simba :

ajouter la colonne pkFin
🔴 PHASE 0 — PÉRIMÈTRE & NETTOYAGE

Mettre InfoVoyage hors périmètre (temporairement).

Mettre Lavaka hors périmètre (temporairement).

Se concentrer uniquement sur :

Routes Nationales (RN)

Simba

SIG

Calcul du coût de réparation.

🟠 PHASE 1 — BASE DE DONNÉES (PostgreSQL / PostGIS)

Modifier la table Simba :

ajouter la colonne pkFin

ajouter la colonne tauxRalentissement

Renommer la logique :

pointKilometrique → pkDebut

Vérifier la cohérence des données existantes :

pkDebut <= pkFin

S’assurer que les Simba sont bien rattachés à une RN.

🟡 PHASE 2 — MODÈLE JAVA

Modifier Simba.java :

ajouter pkFin

ajouter tauxRalentissement

remplacer pointKilometrique par pkDebut

Mettre à jour :

constructeurs

getters / setters

Vérifier la compatibilité avec le reste du projet.

🟢 PHASE 3 — DAO / CRUD

Modifier le CRUD Simba :

INSERT → gérer pkDebut, pkFin, tauxRalentissement

UPDATE → gérer les nouvelles colonnes

SELECT → récupérer toutes les colonnes

Ajouter / adapter une requête :

récupérer les Simba par RN

Vérifier que les anciennes fonctionnalités fonctionnent toujours.

🔵 PHASE 4 — LOGIQUE MÉTIER (SERVICE)

Modifier la fonction de calcul du coût de réparation d’un Lalana :

tenir compte de pkDebut et pkFin

parcourir les Simba rencontrés sur la RN

Implémenter la logique :

intersection [pkDebutSimba, pkFinSimba]

Intégrer :

surface

profondeur

tauxRalentissement

Tester le calcul sur plusieurs RN.

🟣 PHASE 5 — INTERFACE SWING (MÉTIER)

Modifier l’interface Voyage / Lalana :

afficher le coût total de réparation

Adapter les champs existants aux nouvelles données Simba.

Tester les calculs via l’interface Swing.

🟤 PHASE 6 — MAIN PANEL

Modifier le MainPanel :

ajouter un bouton “Ouvrir la carte SIG”

Ce bouton doit :

ouvrir une nouvelle fenêtre SIG

conserver le MainPanel ouvert

🟪 PHASE 7 — FENÊTRE SIG (SWING → JSP)

Créer une nouvelle fenêtre SIG.

Ajouter un bouton “Fermer la carte”.

Le bouton doit :

fermer la fenêtre SIG

revenir au MainPanel.

🌍 PHASE 8 — SIG WEB (JSP + LEAFLET)
Routes Nationales

Créer une fonction :

trierRN()

Créer une fonction :

afficherRN()

Créer une fonction :

colorierRNEnBleu(RN rn)

Simba

Créer une fonction :

afficherSimbaEnRouge(RN rn)

Afficher les Simba en tenant compte :

pkDebut

pkFin

🔗 PHASE 9 — INTERACTIONS SIG

Afficher la liste de toutes les RN.

Lorsqu’on clique sur une RN :

la tracer sur la carte en bleu

afficher ses Simba en rouge

Ne pas considérer les Lavaka.

🧩 PHASE 10 — STRUCTURE TECHNIQUE

Créer les packages :

service

web.servlet

Créer les servlets :

GET /api/rn/all

GET /api/simba/byRN/{id}

Convertir les données PostGIS en GeoJSON.

🧪 PHASE 11 — TESTS & VALIDATION

Tester :

affichage RN

affichage Simba

Vérifier :

cohérence PK

exactitude des coûts

Vérifier la fermeture correcte de la fenêtre SIG.

🏁 PHASE 12 — FINALISATION

Nettoyer le code.

Ajouter des commentaires clairs.

Préparer l’explication du projet (jury / prof).🔴 PHASE 0 — PÉRIMÈTRE & NETTOYAGE

Mettre InfoVoyage hors périmètre (temporairement).

Mettre Lavaka hors périmètre (temporairement).

Se concentrer uniquement sur :

Routes Nationales (RN)

Simba

SIG

Calcul du coût de réparation.

🟠 PHASE 1 — BASE DE DONNÉES (PostgreSQL / PostGIS)

Modifier la table Simba :

ajouter la colonne pkFin

ajouter la colonne tauxRalentissement

Renommer la logique :

pointKilometrique → pkDebut

Vérifier la cohérence des données existantes :

pkDebut <= pkFin

S’assurer que les Simba sont bien rattachés à une RN.

🟡 PHASE 2 — MODÈLE JAVA

Modifier Simba.java :

ajouter pkFin

ajouter tauxRalentissement

remplacer pointKilometrique par pkDebut

Mettre à jour :

constructeurs

getters / setters

Vérifier la compatibilité avec le reste du projet.

🟢 PHASE 3 — DAO / CRUD

Modifier le CRUD Simba :

INSERT → gérer pkDebut, pkFin, tauxRalentissement

UPDATE → gérer les nouvelles colonnes

SELECT → récupérer toutes les colonnes

Ajouter / adapter une requête :

récupérer les Simba par RN

Vérifier que les anciennes fonctionnalités fonctionnent toujours.

🔵 PHASE 4 — LOGIQUE MÉTIER (SERVICE)

Modifier la fonction de calcul du coût de réparation d’un Lalana :

tenir compte de pkDebut et pkFin

parcourir les Simba rencontrés sur la RN

Implémenter la logique :

intersection [pkDebutSimba, pkFinSimba]

Intégrer :

surface

profondeur

tauxRalentissement

Tester le calcul sur plusieurs RN.

🟣 PHASE 5 — INTERFACE SWING (MÉTIER)

Modifier l’interface Voyage / Lalana :

afficher le coût total de réparation

Adapter les champs existants aux nouvelles données Simba.

Tester les calculs via l’interface Swing.

🟤 PHASE 6 — MAIN PANEL

Modifier le MainPanel :

ajouter un bouton “Ouvrir la carte SIG”

Ce bouton doit :

ouvrir une nouvelle fenêtre SIG

conserver le MainPanel ouvert

🟪 PHASE 7 — FENÊTRE SIG (SWING → JSP)

Créer une nouvelle fenêtre SIG.

Ajouter un bouton “Fermer la carte”.

Le bouton doit :

fermer la fenêtre SIG

revenir au MainPanel.

🌍 PHASE 8 — SIG WEB (JSP + LEAFLET)
Routes Nationales

Créer une fonction :

trierRN()

Créer une fonction :

afficherRN()

Créer une fonction :

colorierRNEnBleu(RN rn)

Simba

Créer une fonction :

afficherSimbaEnRouge(RN rn)

Afficher les Simba en tenant compte :

pkDebut

pkFin

🔗 PHASE 9 — INTERACTIONS SIG

Afficher la liste de toutes les RN.

Lorsqu’on clique sur une RN :

la tracer sur la carte en bleu

afficher ses Simba en rouge

Ne pas considérer les Lavaka.

🧩 PHASE 10 — STRUCTURE TECHNIQUE

Créer les packages :

service

web.servlet

Créer les servlets :

GET /api/rn/all

GET /api/simba/byRN/{id}

Convertir les données PostGIS en GeoJSON.

🧪 PHASE 11 — TESTS & VALIDATION

Tester :

affichage RN

affichage Simba

Vérifier :

cohérence PK

exactitude des coûts

Vérifier la fermeture correcte de la fenêtre SIG.

🏁 PHASE 12 — FINALISATION

Nettoyer le code.

Ajouter des commentaires clairs.

Préparer l’explication du projet (jury / prof)
ajouter la colonne tauxRalentissement

Renommer la logique :

pointKilometrique → pkDebut

Vérifier la cohérence des données existantes :

pkDebut <= pkFin

S’assurer que les Simba sont bien rattachés à une RN.

🟡 PHASE 2 — MODÈLE JAVA

Modifier Simba.java :

ajouter pkFin

ajouter tauxRalentissement

remplacer pointKilometrique par pkDebut

Mettre à jour :

constructeurs

getters / setters

Vérifier la compatibilité avec le reste du projet.

🟢 PHASE 3 — DAO / CRUD

Modifier le CRUD Simba :

INSERT → gérer pkDebut, pkFin, tauxRalentissement

UPDATE → gérer les nouvelles colonnes

SELECT → récupérer toutes les colonnes

Ajouter / adapter une requête :

récupérer les Simba par RN

Vérifier que les anciennes fonctionnalités fonctionnent toujours.

🔵 PHASE 4 — LOGIQUE MÉTIER (SERVICE)

Modifier la fonction de calcul du coût de réparation d’un Lalana :

tenir compte de pkDebut et pkFin

parcourir les Simba rencontrés sur la RN

Implémenter la logique :

intersection [pkDebutSimba, pkFinSimba]

Intégrer :

surface

profondeur

tauxRalentissement

Tester le calcul sur plusieurs RN.

🟣 PHASE 5 — INTERFACE SWING (MÉTIER)

Modifier l’interface Voyage / Lalana :

afficher le coût total de réparation

Adapter les champs existants aux nouvelles données Simba.

Tester les calculs via l’interface Swing.

🟤 PHASE 6 — MAIN PANEL

Modifier le MainPanel :

ajouter un bouton “Ouvrir la carte SIG”

Ce bouton doit :

ouvrir une nouvelle fenêtre SIG

conserver le MainPanel ouvert

🟪 PHASE 7 — FENÊTRE SIG (SWING → JSP)

Créer une nouvelle fenêtre SIG.

Ajouter un bouton “Fermer la carte”.

Le bouton doit :

fermer la fenêtre SIG

revenir au MainPanel.

🌍 PHASE 8 — SIG WEB (JSP + LEAFLET)
Routes Nationales

Créer une fonction :

trierRN()

Créer une fonction :

afficherRN()

Créer une fonction :

colorierRNEnBleu(RN rn)

Simba

Créer une fonction :

afficherSimbaEnRouge(RN rn)

Afficher les Simba en tenant compte :

pkDebut

pkFin

🔗 PHASE 9 — INTERACTIONS SIG

Afficher la liste de toutes les RN.

Lorsqu’on clique sur une RN :

la tracer sur la carte en bleu

afficher ses Simba en rouge

Ne pas considérer les Lavaka.

🧩 PHASE 10 — STRUCTURE TECHNIQUE

Créer les packages :

service

web.servlet

Créer les servlets :

GET /api/rn/all

GET /api/simba/byRN/{id}

Convertir les données PostGIS en GeoJSON.

🧪 PHASE 11 — TESTS & VALIDATION

Tester :

affichage RN

affichage Simba

Vérifier :

cohérence PK

exactitude des coûts

Vérifier la fermeture correcte de la fenêtre SIG.

🏁 PHASE 12 — FINALISATION

Nettoyer le code.

Ajouter des commentaires clairs.

Préparer l’explication du projet (jury / prof).