<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, java.math.BigDecimal, com.restservice.notecorrection.entity.*" %>

<div class="container py-4">
    <div class="row">
        <div class="col-lg-12">
            
            <div class="mb-4 border-bottom pb-3">
                <h2 class="h4 fw-bold">Résumé de la Note Finale</h2>
                <p class="text-muted mb-0">${candidat.nom} ${candidat.prenom} | ${matiere.nom}</p>
            </div>

            <div class="card mb-4 border-0 shadow-sm">
                <div class="card-body">
                    <h6 class="fw-bold mb-3"><i class="bi bi-pencil-square me-2"></i>Notes Brutes</h6>
                    <table class="table table-bordered">
                        <thead class="table-light">
                            <tr>
                                <th>Correcteur</th>
                                <th class="text-center" style="width: 150px;">Note / 20</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                List<Note> notesBrutes = (List<Note>) request.getAttribute("notesBrutes");
                                if (notesBrutes != null) {
                                    for (Note n : notesBrutes) {
                            %>
                                <tr>
                                    <td><%= n.getCorrecteur().getNom() %> <%= n.getCorrecteur().getPrenom() %></td>
                                    <td class="text-center fw-bold"><%= n.getValeurNote() %></td>
                                </tr>
                            <% } } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <% if (request.getAttribute("sommeDifferences") != null) { %>
                <div class="card mb-4 border-0 shadow-sm bg-light">
                    <div class="card-body">
                        <h6 class="fw-bold">Analyse des écarts</h6>
                        <p class="mb-2">Somme des différences : <strong><%= request.getAttribute("sommeDifferences") %></strong></p>
                    
                    </div>
                </div>
            <% } %>

            <div class="table-responsive mt-4">
    <table class="table table-bordered align-middle">
        <thead class="table-light">
            <tr>
                <th colspan="2" class="py-2 text-uppercase small fw-bold text-secondary">Résultat </th>
            </tr>
        </thead>
        <tbody>
            <%
                NoteFinale noteFinale = (NoteFinale) request.getAttribute("noteFinale");
                if (noteFinale != null) {
            %>
                <tr>
                    <td class="text-muted" style="width: 50%;">Note Finale calculée</td>
                    <td class="fw-bold fs-4 text-primary"><%= noteFinale.getValeurNoteFinale() %> / 20</td>
                </tr>
                <tr>
                    <td class="text-muted">Méthode de résolution</td>
                    <td>
                        <span class="badge border text-dark fw-normal">
                            <%= noteFinale.getResolutionUtilisee().getLibelleNote() %>
                        </span>
                    </td>
                </tr>
            <% } else { %>
                <tr>
                    <td colspan="2" class="text-center py-3 text-muted italic">
                        <i class="bi bi-info-circle me-2"></i>Aucun calcul n'a encore été généré pour ce candidat.
                    </td>
                </tr>
            <% } %>
        </tbody>
    </table>
</div>


        </div>
    </div>
</div>