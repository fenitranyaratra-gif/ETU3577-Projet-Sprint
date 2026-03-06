Excellent ! Je vois le problème. Actuellement, votre méthode `optimiser()` ne gère pas bien le cas où un véhicule peut prendre seulement une partie des passagers. Voici les modifications à apporter :

## 1. Modifier la méthode `optimiser` dans `GestionReservation.java`

```java
public void optimiser(List<Reservation> reservations, double vitesse) { 
    if (reservations == null || reservations.isEmpty()) return;
    
    List<Vehicule> vehicules = vehiculeService.getAllVehiculeDispoOrderedByAsc(reservationService.getDateDepartArriveAeroport(reservations));
    
    if (vehicules.isEmpty()) {
        System.out.println("Aucun véhicule disponible pour ce groupe");
        return;
    }
    
    int nbPassagers = reservationService.nombrePassager(reservations);
    LocalDateTime dateDepart = reservationService.getDateDepartArriveAeroport(reservations);
    
    java.sql.Date sqlDate = java.sql.Date.valueOf(dateDepart.toLocalDate());
    
    // ESSAYER DE TROUVER UN VÉHICULE POUR TOUT LE GROUPE
    Vehicule v = choisirVehicule(vehicules, nbPassagers, sqlDate);
    
    if (v != null) {
        // CAS 1: Un seul véhicule peut prendre tout le groupe
        Planning p = distanceService.calculerDateArriveeAvecDistance(reservations, vitesse);
        
        if (p != null) {
            for (Reservation r : reservations) {
                Planning planningPourReservation = new Planning();
                
                planningPourReservation.setIdVehicule(v.getId().intValue());
                planningPourReservation.setIdReservation(r.getId().intValue());
                planningPourReservation.setKmParcouru(p.getKmParcouru());
                planningPourReservation.setDureeTrajetHeures(p.getDureeTrajetHeures());
                planningPourReservation.setDepartAeroport(p.getDepartAeroport());
                planningPourReservation.setArriveAeroport(p.getArriveAeroport());
                
                planningService.createPlanning(planningPourReservation);
            }
            return; // Terminé avec succès
        }
    }
    
    // CAS 2: Aucun véhicule ne peut prendre tout le groupe
    // On essaie de répartir entre plusieurs véhicules
    repartirReservationsEntreVehicules(reservations, vehicules, dateDepart, vitesse);
}
```

## 2. Ajouter la nouvelle méthode `repartirReservationsEntreVehicules`

```java
public void repartirReservationsEntreVehicules(List<Reservation> reservations, 
                                                List<Vehicule> vehicules,
                                                LocalDateTime dateDepart, 
                                                double vitesse) {
    
    if (reservations == null || reservations.isEmpty() || vehicules == null || vehicules.isEmpty()) {
        System.out.println("Impossible de répartir: réservations ou véhicules manquants");
        return;
    }
    
    // Trier les réservations par nombre de passagers (du plus grand au plus petit)
    List<Reservation> reservationsTriees = new ArrayList<>(reservations);
    reservationsTriees.sort(Comparator.comparingInt(Reservation::getNbPassagers).reversed());
    
    // Trier les véhicules par capacité (du plus petit au plus grand pour optimiser)
    List<Vehicule> vehiculesDisponibles = new ArrayList<>(vehicules);
    vehiculesDisponibles.sort(Comparator.comparingInt(Vehicule::getNbPlaces));
    
    List<Reservation> nonAttribuees = new ArrayList<>();
    java.sql.Date sqlDate = java.sql.Date.valueOf(dateDepart.toLocalDate());
    
    for (Reservation reservation : reservationsTriees) {
        boolean attribuee = false;
        int nbPassagers = reservation.getNbPassagers();
        
        // Chercher un véhicule qui peut prendre cette réservation seule
        for (Vehicule vehicule : new ArrayList<>(vehiculesDisponibles)) {
            if (vehicule.getNbPlaces() >= nbPassagers) {
                // Ce véhicule peut prendre cette réservation
                List<Reservation> groupe = new ArrayList<>();
                groupe.add(reservation);
                
                Planning p = distanceService.calculerDateArriveeAvecDistance(groupe, vitesse);
                
                if (p != null) {
                    Planning planningPourReservation = new Planning();
                    planningPourReservation.setIdVehicule(vehicule.getId().intValue());
                    planningPourReservation.setIdReservation(reservation.getId().intValue());
                    planningPourReservation.setKmParcouru(p.getKmParcouru());
                    planningPourReservation.setDureeTrajetHeures(p.getDureeTrajetHeures());
                    planningPourReservation.setDepartAeroport(p.getDepartAeroport());
                    planningPourReservation.setArriveAeroport(p.getArriveAeroport());
                    
                    planningService.createPlanning(planningPourReservation);
                    
                    // Retirer le véhicule des disponibles (il est maintenant occupé)
                    vehiculesDisponibles.remove(vehicule);
                    attribuee = true;
                    break;
                }
            }
        }
        
        if (!attribuee) {
            // Essayer de partager cette réservation avec d'autres
            boolean partagee = essayerPartagerReservation(reservation, vehiculesDisponibles, dateDepart, vitesse, sqlDate);
            if (!partagee) {
                nonAttribuees.add(reservation);
            }
        }
    }
    
    if (!nonAttribuees.isEmpty()) {
        System.out.println("Réservations non attribuées: " + nonAttribuees.size());
        // Vous pouvez appeler separerEtReaffecter pour les réservations restantes
        // separerEtReaffecter(nonAttribuees, vehicules, dateDepart, vitesse);
    }
}
```

## 3. Ajouter la méthode `essayerPartagerReservation`

```java
public boolean essayerPartagerReservation(Reservation reservation, 
                                           List<Vehicule> vehiculesDisponibles,
                                           LocalDateTime dateDepart,
                                           double vitesse,
                                           java.sql.Date sqlDate) {
    
    int nbPassagers = reservation.getNbPassagers();
    
    // Chercher une combinaison de véhicules qui peuvent couvrir cette réservation
    // Par exemple: 15 passagers -> 10 + 5
    
    List<Vehicule> combinaison = trouverCombinaisonVehicules(vehiculesDisponibles, nbPassagers);
    
    if (combinaison.isEmpty()) {
        return false;
    }
    
    // On va diviser la réservation en sous-réservations
    // Pour simplifier, on crée des réservations virtuelles
    List<Reservation> sousReservations = new ArrayList<>();
    int passagersRestants = nbPassagers;
    
    for (Vehicule v : combinaison) {
        int placesDansCeVehicule = v.getNbPlaces();
        int passagersPourCeVehicule = Math.min(passagersRestants, placesDansCeVehicule);
        
        // Créer une copie de la réservation avec le nombre de passagers ajusté
        Reservation sousReservation = new Reservation();
        sousReservation.setId(reservation.getId()); // Même ID pour garder le lien
        sousReservation.setNomClient(reservation.getNomClient() + " (partiel)");
        sousReservation.setNbPassagers(passagersPourCeVehicule);
        sousReservation.setIdLieux(reservation.getIdLieux());
        sousReservation.setDateHeureArrivee(reservation.getDateHeureArrivee());
        
        sousReservations.add(sousReservation);
        passagersRestants -= passagersPourCeVehicule;
        
        if (passagersRestants <= 0) break;
    }
    
    // Attribuer chaque sous-réservation à un véhicule
    for (int i = 0; i < sousReservations.size(); i++) {
        Reservation sousRes = sousReservations.get(i);
        Vehicule v = combinaison.get(i);
        
        List<Reservation> groupe = new ArrayList<>();
        groupe.add(sousRes);
        
        Planning p = distanceService.calculerDateArriveeAvecDistance(groupe, vitesse);
        
        if (p != null) {
            Planning planning = new Planning();
            planning.setIdVehicule(v.getId().intValue());
            planning.setIdReservation(reservation.getId().intValue()); // ID original
            planning.setKmParcouru(p.getKmParcouru());
            planning.setDureeTrajetHeures(p.getDureeTrajetHeures());
            planning.setDepartAeroport(p.getDepartAeroport());
            planning.setArriveAeroport(p.getArriveAeroport());
            
            planningService.createPlanning(planning);
            
            // Retirer le véhicule des disponibles
            vehiculesDisponibles.remove(v);
        }
    }
    
    return true;
}
```

## 4. Ajouter la méthode `trouverCombinaisonVehicules`

```java
public List<Vehicule> trouverCombinaisonVehicules(List<Vehicule> vehicules, int nbPassagers) {
    List<Vehicule> resultat = new ArrayList<>();
    
    // Trier par capacité décroissante pour essayer d'abord les grands véhicules
    List<Vehicule> tries = new ArrayList<>(vehicules);
    tries.sort((v1, v2) -> Integer.compare(v2.getNbPlaces(), v1.getNbPlaces()));
    
    int passagersRestants = nbPassagers;
    
    for (Vehicule v : tries) {
        if (passagersRestants <= 0) break;
        
        if (v.getNbPlaces() <= passagersRestants) {
            // Ce véhicule peut être utilisé entièrement
            resultat.add(v);
            passagersRestants -= v.getNbPlaces();
        }
    }
    
    // Si on n'a pas pu couvrir tous les passagers, chercher un véhicule qui peut prendre le reste
    if (passagersRestants > 0) {
        for (Vehicule v : tries) {
            if (!resultat.contains(v) && v.getNbPlaces() >= passagersRestants) {
                resultat.add(v);
                passagersRestants = 0;
                break;
            }
        }
    }
    
    return passagersRestants == 0 ? resultat : new ArrayList<>();
}
```

## 5. Optionnel: Ajouter une annotation dans Reservation pour le suivi

Si vous voulez garder une trace que la réservation a été fractionnée, vous pourriez ajouter un champ dans l'entité `Reservation`:

```java
// Dans Reservation.java
private Boolean estFractionnee = false;
private Integer idReservationParent; // Pour lier les fractions à la réservation originale
```

Ces modifications permettent de:
1. Essayer d'abord de mettre tout le groupe dans un seul véhicule
2. Si impossible, répartir intelligemment entre plusieurs véhicules
3. Fractionner une réservation si nécessaire (ex: 15 personnes -> 2 véhicules)
4. Trouver la meilleure combinaison de véhicules pour couvrir le nombre de passagers

Voulez-vous que je vous montre aussi comment adapter `separerEtReaffecter` pour utiliser ces nouvelles méthodes ?