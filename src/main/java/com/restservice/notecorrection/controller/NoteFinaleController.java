package com.restservice.notecorrection.controller;

import com.restservice.notecorrection.entity.*;
import com.restservice.notecorrection.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/notefinale")
public class NoteFinaleController {

    @Autowired
    private ResolutionNoteService resolutionNoteService;
    
    @Autowired
    private NoteFinaleService noteFinaleService;
    
    @Autowired
    private CandidatService candidatService;
    
    @Autowired
    private MatiereService matiereService;
    
    @Autowired
    private NoteService noteService;
    
    /**
     * Affiche le formulaire de recherche de note finale
     */
    @GetMapping("/rechercher")
    public ModelAndView showRechercheForm() {
        ModelAndView modelAndView = new ModelAndView("notefinale/rechercher");
        modelAndView.addObject("candidats", candidatService.getAllCandidats());
        modelAndView.addObject("matieres", matiereService.getAllMatieres());
        modelAndView.addObject("titre", "Rechercher une note finale");
        return modelAndView;
    }
    
    /**
     * Traite la recherche et affiche le résultat
     */
    @PostMapping("/resultat")
    public ModelAndView getNoteFinale(@RequestParam("idCandidat") Integer idCandidat,
                                      @RequestParam("idMatiere") Integer idMatiere,
                                      RedirectAttributes redirectAttributes) {
        
        ModelAndView modelAndView = new ModelAndView("notefinale/resultat");
        
        try {
            // Récupérer les informations du candidat et de la matière
            Candidat candidat = candidatService.getCandidatById(idCandidat);
            Matiere matiere = matiereService.getMatiereById(idMatiere);
            
            if (candidat == null || matiere == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Candidat ou matière non trouvé");
                return new ModelAndView("redirect:/notefinale/rechercher");
            }
            
            // 1. Récupérer toutes les notes brutes pour affichage
            List<Note> notesBrutes = noteService.getNotesByMatiereAndCandidat(idMatiere, idCandidat);
            
            // 2. Vérifier si une note finale existe déjà
            NoteFinale noteFinale = noteFinaleService.getNoteFinale(idMatiere, idCandidat);
            
            // 3. Si pas de note finale mais qu'il y a des notes brutes, la calculer automatiquement
            if (noteFinale == null && !notesBrutes.isEmpty()) {
                try {
                    BigDecimal noteCalculee = resolutionNoteService.resoudreNote(idMatiere, idCandidat);
                    // Recharger la note finale après calcul
                    noteFinale = noteFinaleService.getNoteFinale(idMatiere, idCandidat);
                    modelAndView.addObject("autoCalculMessage", "Note finale calculée automatiquement");
                } catch (Exception e) {
                    modelAndView.addObject("errorMessage", "Erreur lors du calcul automatique: " + e.getMessage());
                }
            }
            
            // 4. Récupérer TOUS les paramètres de résolution pour cette matière
            String parametreInfo = "";
            BigDecimal sommeDifferences = null;
            Parametre parametreCorrespondant = null;
            String detailsCalcul = "";
            
            try {
                List<Parametre> parametres = resolutionNoteService.getParametresByMatiere(idMatiere);
                if (!parametres.isEmpty() && !notesBrutes.isEmpty()) {
                    // Calculer la SOMME des différences
                    sommeDifferences = resolutionNoteService.calculerSommeDifferences(notesBrutes);
                    
                    // Trouver le paramètre correspondant
                    parametreCorrespondant = resolutionNoteService.trouverParametrePourSomme(idMatiere, sommeDifferences);
                    
                    // Générer les détails du calcul pour affichage
                    detailsCalcul = genererDetailsCalcul(notesBrutes);
                    
                    // Construire l'info de tous les paramètres
                    StringBuilder sb = new StringBuilder();
                    sb.append("Paramètres disponibles:<br>");
                    for (Parametre p : parametres) {
                        sb.append("- Seuil: ").append(p.getEcartMax())
                          .append(", Opérateur: ").append(p.getOperateur().getSymbole())
                          .append(", Résolution: ").append(p.getResolution().getLibelleNote())
                          .append("<br>");
                    }
                    if (parametreCorrespondant != null) {
                        sb.append("<br><strong>✅ Paramètre correspondant: seuil=").append(parametreCorrespondant.getEcartMax())
                          .append(", opérateur=").append(parametreCorrespondant.getOperateur().getSymbole())
                          .append(", résolution=").append(parametreCorrespondant.getResolution().getLibelleNote())
                          .append("</strong>");
                    } else {
                        sb.append("<br><strong>❌ Aucun paramètre ne correspond à la somme calculée (")
                          .append(sommeDifferences).append(")</strong>");
                    }
                    parametreInfo = sb.toString();
                } else if (parametres.isEmpty()) {
                    parametreInfo = "Aucun paramètre configuré pour cette matière";
                }
            } catch (Exception e) {
                parametreInfo = "Erreur lors de la lecture des paramètres: " + e.getMessage();
            }
            
            modelAndView.addObject("candidat", candidat);
            modelAndView.addObject("matiere", matiere);
            modelAndView.addObject("noteFinale", noteFinale);
            modelAndView.addObject("notesBrutes", notesBrutes);
            modelAndView.addObject("parametreInfo", parametreInfo);
            modelAndView.addObject("sommeDifferences", sommeDifferences);
            modelAndView.addObject("parametreCorrespondant", parametreCorrespondant);
            modelAndView.addObject("detailsCalcul", detailsCalcul);
            modelAndView.addObject("titre", "Résultat de la recherche");
            
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "Erreur: " + e.getMessage());
        }
        
        return modelAndView;
    }
    
    /**
     * Génère les détails du calcul de la somme des différences
     */
    private String genererDetailsCalcul(List<Note> notes) {
        if (notes.size() < 2) {
            return "Pas assez de notes pour calculer les différences";
        }
        
        StringBuilder details = new StringBuilder();
        List<BigDecimal> valeurs = notes.stream()
                .map(Note::getValeurNote)
                .collect(java.util.stream.Collectors.toList());
        
        BigDecimal somme = BigDecimal.ZERO;
        
        for (int i = 0; i < valeurs.size(); i++) {
            for (int j = i + 1; j < valeurs.size(); j++) {
                BigDecimal diff = valeurs.get(i).subtract(valeurs.get(j)).abs();
                somme = somme.add(diff);
                
                if (details.length() > 0) {
                    details.append(" + ");
                }
                details.append("|").append(valeurs.get(i)).append(" - ").append(valeurs.get(j)).append("| = ").append(diff);
            }
        }
        
        details.append(" = ").append(somme);
        return details.toString();
    }
    
    /**
     * Calcule (ou recalcule) la note finale et l'affiche
     */
    @PostMapping("/calculer")
    public ModelAndView calculerNoteFinale(@RequestParam("idCandidat") Integer idCandidat,
                                          @RequestParam("idMatiere") Integer idMatiere,
                                          RedirectAttributes redirectAttributes) {
        
        try {
            // Appeler le service de résolution qui calcule et sauvegarde la note finale
            BigDecimal noteCalculee = resolutionNoteService.resoudreNote(idMatiere, idCandidat);
            
            redirectAttributes.addFlashAttribute("successMessage", 
                "Note finale calculée avec succès: " + noteCalculee);
            
            // Rediriger vers la page de résultat avec les paramètres
            return new ModelAndView("redirect:/notefinale/resultat?idCandidat=" + idCandidat + 
                                   "&idMatiere=" + idMatiere);
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Erreur lors du calcul: " + e.getMessage());
            return new ModelAndView("redirect:/notefinale/rechercher");
        }
    }
    
    /**
     * Affiche toutes les notes finales (pour un candidat ou une matière)
     */
    @GetMapping("/liste")
    public ModelAndView listeNotesFinales(@RequestParam(value = "idCandidat", required = false) Integer idCandidat,
                                         @RequestParam(value = "idMatiere", required = false) Integer idMatiere) {
        
        ModelAndView modelAndView = new ModelAndView("notefinale/liste");
        List<NoteFinale> notesFinales;
        
        if (idCandidat != null) {
            notesFinales = noteFinaleService.getNotesFinalesParCandidat(idCandidat);
            Candidat candidat = candidatService.getCandidatById(idCandidat);
            modelAndView.addObject("filtre", "Candidat: " + candidat.getNom() + " " + candidat.getPrenom());
        } else if (idMatiere != null) {
            notesFinales = noteFinaleService.getNotesFinalesParMatiere(idMatiere);
            Matiere matiere = matiereService.getMatiereById(idMatiere);
            modelAndView.addObject("filtre", "Matière: " + matiere.getNom());
        } else {
            notesFinales = noteFinaleService.getAllNotesFinales();
            modelAndView.addObject("filtre", "Toutes les notes finales");
        }
        
        modelAndView.addObject("notesFinales", notesFinales);
        modelAndView.addObject("titre", "Liste des notes finales");
        modelAndView.addObject("candidats", candidatService.getAllCandidats());
        modelAndView.addObject("matieres", matiereService.getAllMatieres());
        
        return modelAndView;
    }
    
    /**
     * API pour obtenir les détails du calcul (format JSON)
     */
    @GetMapping("/api/details/{idMatiere}/{idCandidat}")
    @ResponseBody
    public java.util.Map<String, Object> getDetailsCalcul(@PathVariable Integer idMatiere, 
                                                          @PathVariable Integer idCandidat) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        
        try {
            List<Note> notes = noteService.getNotesByMatiereAndCandidat(idMatiere, idCandidat);
            List<Parametre> parametres = resolutionNoteService.getParametresByMatiere(idMatiere);
            
            if (notes.isEmpty()) {
                result.put("error", "Aucune note trouvée");
                return result;
            }
            
            // Calculer la somme des différences
            BigDecimal sommeDifferences = resolutionNoteService.calculerSommeDifferences(notes);
            
            result.put("notes", notes.stream()
                .map(n -> n.getValeurNote() + " (" + n.getCorrecteur().getNom() + ")")
                .collect(java.util.stream.Collectors.toList()));
            result.put("sommeDifferences", sommeDifferences);
            
            // Ajouter tous les paramètres
            if (!parametres.isEmpty()) {
                List<java.util.Map<String, Object>> paramsList = new java.util.ArrayList<>();
                for (Parametre p : parametres) {
                    java.util.Map<String, Object> paramMap = new java.util.HashMap<>();
                    paramMap.put("id", p.getId());
                    paramMap.put("seuil", p.getEcartMax());
                    paramMap.put("operateur", p.getOperateur().getSymbole());
                    paramMap.put("resolution", p.getResolution().getLibelleNote());
                    
                    boolean condition = resolutionNoteService.verifierCondition(
                        sommeDifferences, p.getOperateur(), p.getEcartMax());
                    paramMap.put("conditionRemplie", condition);
                    
                    paramsList.add(paramMap);
                }
                result.put("parametres", paramsList);
                
                // Trouver le paramètre correspondant
                Parametre correspondant = resolutionNoteService.trouverParametrePourSomme(idMatiere, sommeDifferences);
                if (correspondant != null) {
                    result.put("parametreCorrespondantId", correspondant.getId());
                }
            }
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}