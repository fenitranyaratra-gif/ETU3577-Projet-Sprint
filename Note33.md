Bonjour ! Excellente idée d'amélioration. Actuellement, votre méthode `separerEtReaffecter` ne fait que regrouper des réservations existantes, mais ne **divise pas** une réservation trop grande. Voici les modifications à apporter :

## 1. Modifier la méthode `separerEtReaffecter`

```java
public void separerEtReaffecter(List<Reservation> reservations, List<Vehicule> vehicules, 
                               LocalDateTime dateDepart, double vitesse) {
    
    List<Reservation> reservationsTriees = new ArrayList<>(reservations);
    reservationsTriees.sort(Comparator.comparingInt(Reservation::getNbPassagers).reversed());
    
    List<Vehicule> vehiculesDisponibles = new ArrayList<>(vehicules);
    vehiculesDisponibles.sort(Comparator.comparingInt(Vehicule::getNbPlaces));
    
    List<Reservation> restants = new ArrayList<>();
    List<Reservation> aTraiter = new ArrayList<>(reservationsTriees);
    
    java.sql.Date dateSql = java.sql.Date.valueOf(dateDepart.toLocalDate());
    
    while (!aTraiter.isEmpty() && !vehiculesDisponibles.isEmpty()) {
        Reservation reservationCourante = aTraiter.get(0);
        int nbPassagersAReserver = reservationCourante.getNbPassagers();
        
        // Chercher le plus petit véhicule pouvant contenir CE groupe ou partie du groupe
        Vehicule vehiculeChoisi = null;
        int placesUtilisees = 0;
        
        for (Vehicule v : vehiculesDisponibles) {
            if (v.getNbPlaces() >= nbPassagersAReserver) {
                // Cas 1: Le véhicule peut prendre toute la réservation
                vehiculeChoisi = v;
                placesUtilisees = nbPassagersAReserver;
                break;
            } else if (v.getNbPlaces() > placesUtilisees) {
                // Cas 2: On garde le véhicule avec le plus de places pour SPLITTER
                if (vehiculeChoisi == null || v.getNbPlaces() > vehiculeChoisi.getNbPlaces()) {
                    vehiculeChoisi = v;
                    placesUtilisees = v.getNbPlaces(); // On utilise TOUTES les places
                }
            }
        }
        
        if (vehiculeChoisi == null) {
            System.out.println("Aucun véhicule ne peut prendre la réservation de " + 
                             nbPassagersAReserver + " personnes");
            restants.add(reservationCourante);
            aTraiter.remove(0);
            continue;
        }
        
        // CAS 1: Le véhicule peut prendre toute la réservation
        if (vehiculeChoisi.getNbPlaces() >= nbPassagersAReserver) {
            traiterReservationComplete(reservationCourante, vehiculeChoisi, dateSql, vitesse, dateDepart);
            aTraiter.remove(0);
            vehiculesDisponibles.remove(vehiculeChoisi);
        } 
        // CAS 2: Le véhicule ne peut prendre qu'une PARTIE de la réservation (SPLIT)
        else {
            System.out.println("SPLIT: Réservation " + reservationCourante.getId() + 
                             " (" + nbPassagersAReserver + " personnes) répartie sur plusieurs véhicules");
            
            // Répartir les passagers sur plusieurs véhicules
            int passagersRestants = nbPassagersAReserver;
            List<Vehicule> vehiculesUtilises = new ArrayList<>();
            
            while (passagersRestants > 0 && !vehiculesDisponibles.isEmpty()) {
                Vehicule v = trouverMeilleurVehiculePourSplit(vehiculesDisponibles, passagersRestants);
                
                if (v == null) break;
                
                int passagersPourCeVehicule = Math.min(v.getNbPlaces(), passagersRestants);
                
                // Créer une nouvelle réservation SPLIT
                Reservation splitReservation = creerReservationSplit(
                    reservationCourante, passagersPourCeVehicule);
                
                // Affecter au véhicule
                traiterReservationComplete(splitReservation, v, dateSql, vitesse, dateDepart);
                
                passagersRestants -= passagersPourCeVehicule;
                vehiculesUtilises.add(v);
                vehiculesDisponibles.remove(v);
                
                System.out.println("  -> Véhicule " + v.getNumeroMatricule() + 
                                 " prend " + passagersPourCeVehicule + " personnes");
            }
            
            if (passagersRestants > 0) {
                System.out.println("  Impossible de répartir tous les passagers. Reste: " + 
                                 passagersRestants + " personnes");
                // Garder le reste pour traitement ultérieur
                Reservation reste = creerReservationSplit(reservationCourante, passagersRestants);
                restants.add(reste);
            }
            
            aTraiter.remove(0);
        }
    }
    
    // Ajouter les réservations non traitées aux restants
    restants.addAll(aTraiter);
    
    System.out.println("Regroupement terminé. Réservations restantes non traitées : " + restants.size());
}
```

## 2. Ajouter les méthodes utilitaires

```java
private Vehicule trouverMeilleurVehiculePourSplit(List<Vehicule> vehicules, int passagersRestants) {
    // Stratégie: prendre le véhicule avec le plus de places disponibles
    // mais pas trop grand pour ne pas gaspiller
    Vehicule meilleur = null;
    int minGaspillage = Integer.MAX_VALUE;
    
    for (Vehicule v : vehicules) {
        if (v.getNbPlaces() <= passagersRestants) {
            // Véhicule trop petit - on le prend quand même si c'est le seul choix
            if (meilleur == null) meilleur = v;
        } else {
            // Véhicule assez grand - calculer le gaspillage
            int gaspillage = v.getNbPlaces() - passagersRestants;
            if (gaspillage < minGaspillage) {
                minGaspillage = gaspillage;
                meilleur = v;
            }
        }
    }
    
    return meilleur;
}

private Reservation creerReservationSplit(Reservation original, int nbPassagers) {
    Reservation split = new Reservation();
    split.setNomClient(original.getNomClient() + " (split)");
    split.setNbPassagers(nbPassagers);
    split.setDateHeureArrivee(original.getDateHeureArrivee());
    split.setIdLieux(original.getIdLieux());
    split.setId(original.getId()); // Garder l'ID original pour traçabilité
    return split;
}

private void traiterReservationComplete(Reservation reservation, Vehicule vehicule, 
                                       java.sql.Date dateSql, double vitesse, 
                                       LocalDateTime dateDepart) {
    List<Reservation> groupe = new ArrayList<>();
    groupe.add(reservation);
    
    int totalPassagers = reservation.getNbPassagers();
    Vehicule vChoisi = choisirVehicule(List.of(vehicule), totalPassagers, dateSql);
    
    if (vChoisi != null) {
        Planning trajet = distanceService.calculerDateArriveeAvecDistance(groupe, vitesse);
        
        if (trajet != null) {
            for (Reservation r : groupe) {
                planningService.createPlanning(copierTrajet(r, vChoisi, trajet));
            }
            
            System.out.println("Véhicule " + vChoisi.getNumeroMatricule() + 
                             " (" + vChoisi.getNbPlaces() + " places)" +
                             " -> réservation " + reservation.getId() + 
                             " (" + totalPassagers + " personnes)");
        }
    }
}
```

## 3. Amélioration optionnelle : Gérer les réservations fractionnées en base

Si vous voulez persister les réservations fractionnées, ajoutez dans l'entité `Reservation` :

```java
// Dans Reservation.java
private Long reservationParentId;  // null si réservation originale
private Boolean estSplit;          // true si c'est un fragment
```

Et dans la méthode de split :

```java
private Reservation creerReservationSplit(Reservation original, int nbPassagers) {
    Reservation split = new Reservation();
    split.setNomClient(original.getNomClient() + " (partiel)");
    split.setNbPassagers(nbPassagers);
    split.setDateHeureArrivee(original.getDateHeureArrivee());
    split.setIdLieux(original.getIdLieux());
    split.setReservationParentId(original.getId()); // Lien vers la réservation parent
    split.setEstSplit(true);
    
    // Sauvegarder en base
    return reservationService.saveReservation(split);
}
```

## Résumé des améliorations

1. **Détection automatique** : Quand une réservation est trop grande pour un seul véhicule
2. **Split intelligent** : Répartition sur plusieurs véhicules disponibles
3. **Optimisation** : Choix du meilleur véhicule pour minimiser le gaspillage de places
4. **Traçabilité** : Possibilité de garder le lien avec la réservation originale

Ces modifications permettent de gérer des cas comme votre exemple (15 personnes avec véhicules 10 et 5 places) de manière automatique !