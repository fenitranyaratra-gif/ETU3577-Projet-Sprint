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
            
            // 1. D'abord, vérifier si une note finale existe déjà
            NoteFinale noteFinale = noteFinaleService.getNoteFinale(idMatiere, idCandidat);
            
            // 2. Récupérer toutes les notes brutes pour affichage
            List<Note> notesBrutes = noteService.getNotesByMatiereAndCandidat(idMatiere, idCandidat);
            
            // 3. Récupérer les paramètres de résolution pour cette matière
            String parametreInfo = "";
            BigDecimal ecartCalcule = null;
            boolean ecartDepasse = false;
            
            try {
                Parametre parametre = resolutionNoteService.getParametreByMatiere(idMatiere);
                if (parametre != null) {
                    ecartCalcule = resolutionNoteService.calculerEcart(notesBrutes);
                    ecartDepasse = resolutionNoteService.verifierEcartAvecParametre(notesBrutes, parametre);
                    
                    parametreInfo = "Écart max: " + parametre.getEcartMax() + 
                                   ", Opérateur: " + parametre.getOperateur().getSymbole() +
                                   ", Résolution si dépassement: " + parametre.getResolution().getLibelleNote();
                }
            } catch (Exception e) {
                parametreInfo = "Paramètres non configurés pour cette matière";
            }
            
            modelAndView.addObject("candidat", candidat);
            modelAndView.addObject("matiere", matiere);
            modelAndView.addObject("noteFinale", noteFinale);
            modelAndView.addObject("notesBrutes", notesBrutes);
            modelAndView.addObject("parametreInfo", parametreInfo);
            modelAndView.addObject("ecartCalcule", ecartCalcule);
            modelAndView.addObject("ecartDepasse", ecartDepasse);
            modelAndView.addObject("titre", "Résultat de la recherche");
            
        } catch (Exception e) {
            modelAndView.addObject("errorMessage", "Erreur: " + e.getMessage());
        }
        
        return modelAndView;
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
}