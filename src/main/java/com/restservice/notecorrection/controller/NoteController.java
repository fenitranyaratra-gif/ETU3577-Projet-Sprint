package com.restservice.notecorrection.controller;

import com.restservice.notecorrection.entity.Note;
import com.restservice.notecorrection.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping("/liste")
    public ModelAndView listeNotes() {
        List<Note> notes = noteService.getAllNotes();
        
        ModelAndView modelAndView = new ModelAndView("notes/liste");
        modelAndView.addObject("notes", notes);
        modelAndView.addObject("titre", "Liste des Notes");
        modelAndView.addObject("sousTitre", "Gestion des notes");
        
        return modelAndView;
    }
    
    @GetMapping("/ajouter")
    public ModelAndView showAddForm() {
        ModelAndView modelAndView = new ModelAndView("notes/ajouter");
        modelAndView.addObject("note", new Note());
        modelAndView.addObject("matieres", noteService.getAllMatieres());
        modelAndView.addObject("candidats", noteService.getAllCandidats());
        modelAndView.addObject("correcteurs", noteService.getAllCorrecteurs());
        modelAndView.addObject("titre", "Ajouter une Note");
        return modelAndView;
    }
    
    @PostMapping("/ajouter")
    public ModelAndView addNote(@RequestParam("matiere.id") Integer matiereId,
                               @RequestParam("candidat.id") Integer candidatId,
                               @RequestParam("correcteur.id") Integer correcteurId,
                               @RequestParam("valeurNote") BigDecimal valeurNote,
                               RedirectAttributes redirectAttributes) {
        
        if (noteService.noteExists(matiereId, candidatId, correcteurId)) {
            ModelAndView modelAndView = new ModelAndView("notes/ajouter");
            modelAndView.addObject("matieres", noteService.getAllMatieres());
            modelAndView.addObject("candidats", noteService.getAllCandidats());
            modelAndView.addObject("correcteurs", noteService.getAllCorrecteurs());
            modelAndView.addObject("errorMessage", 
                "Cette note existe déjà pour ce correcteur !");
            modelAndView.addObject("titre", "Ajouter une Note");
            return modelAndView;
        }
        
        if (!noteService.validateNoteValue(valeurNote)) {
            ModelAndView modelAndView = new ModelAndView("notes/ajouter");
            modelAndView.addObject("matieres", noteService.getAllMatieres());
            modelAndView.addObject("candidats", noteService.getAllCandidats());
            modelAndView.addObject("correcteurs", noteService.getAllCorrecteurs());
            modelAndView.addObject("errorMessage", 
                "La note doit être comprise entre 0 et 20 !");
            modelAndView.addObject("titre", "Ajouter une Note");
            return modelAndView;
        }
        
        Note note = new Note();
        note.setMatiere(noteService.getMatiereById(matiereId));
        note.setCandidat(noteService.getCandidatById(candidatId));
        note.setCorrecteur(noteService.getCorrecteurById(correcteurId));
        note.setValeurNote(valeurNote);
        
        noteService.saveNote(note);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Note ajoutée avec succès !");
        
        return new ModelAndView("redirect:/notes/liste");
    }
    
    @GetMapping("/modifier/{id}")
    public ModelAndView showEditForm(@PathVariable Integer id) {
        Note note = noteService.getNoteById(id);
        
        if (note == null) {
            return new ModelAndView("redirect:/notes/liste");
        }
        
        ModelAndView modelAndView = new ModelAndView("notes/modifier");
        modelAndView.addObject("note", note);
        modelAndView.addObject("matieres", noteService.getAllMatieres());
        modelAndView.addObject("candidats", noteService.getAllCandidats());
        modelAndView.addObject("correcteurs", noteService.getAllCorrecteurs());
        modelAndView.addObject("titre", "Modifier la Note");
        return modelAndView;
    }
    
    @PostMapping("/modifier/{id}")
    public ModelAndView updateNote(@PathVariable Integer id,
                                  @RequestParam("matiere.id") Integer matiereId,
                                  @RequestParam("candidat.id") Integer candidatId,
                                  @RequestParam("correcteur.id") Integer correcteurId,
                                  @RequestParam("valeurNote") BigDecimal valeurNote,
                                  RedirectAttributes redirectAttributes) {
        
        Note existingNote = noteService.getNoteById(id);
        
        if (existingNote == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Note non trouvée !");
            return new ModelAndView("redirect:/notes/liste");
        }
        
        if (!noteService.validateNoteValue(valeurNote)) {
            ModelAndView modelAndView = new ModelAndView("notes/modifier");
            modelAndView.addObject("note", existingNote);
            modelAndView.addObject("matieres", noteService.getAllMatieres());
            modelAndView.addObject("candidats", noteService.getAllCandidats());
            modelAndView.addObject("correcteurs", noteService.getAllCorrecteurs());
            modelAndView.addObject("errorMessage", 
                "La note doit être comprise entre 0 et 20 !");
            modelAndView.addObject("titre", "Modifier la Note");
            return modelAndView;
        }
        
        existingNote.setMatiere(noteService.getMatiereById(matiereId));
        existingNote.setCandidat(noteService.getCandidatById(candidatId));
        existingNote.setCorrecteur(noteService.getCorrecteurById(correcteurId));
        existingNote.setValeurNote(valeurNote);
        
        noteService.saveNote(existingNote);
        
        redirectAttributes.addFlashAttribute("successMessage", 
            "Note modifiée avec succès !");
        
        return new ModelAndView("redirect:/notes/liste");
    }
    
    @GetMapping("/supprimer/{id}")
    public ModelAndView deleteNote(@PathVariable Integer id,
                                  RedirectAttributes redirectAttributes) {
        
        Note note = noteService.getNoteById(id);
        
        if (note == null) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Note non trouvée !");
        } else {
            noteService.deleteNote(id);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Note supprimée avec succès !");
        }
        
        return new ModelAndView("redirect:/notes/liste");
    }
    
    @GetMapping("/candidat/{candidatId}/matiere/{matiereId}")
    public ModelAndView voirNotesParCandidatEtMatiere(@PathVariable Integer candidatId, 
                                                     @PathVariable Integer matiereId) {
        List<Note> notes = noteService.getNotesByMatiereAndCandidat(matiereId, candidatId);
        
        ModelAndView modelAndView = new ModelAndView("notes/liste");
        modelAndView.addObject("notes", notes);
        modelAndView.addObject("titre", "Notes du candidat");
        modelAndView.addObject("sousTitre", "Filtre par matière");
        
        return modelAndView;
    }
}