<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.NoteFinale" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">Candidat</th>
                        <th class="py-3 text-muted fw-semibold">Matière</th>
                        <th class="py-3 text-muted fw-semibold">Note</th>
                        <th class="py-3 text-muted fw-semibold text-center">Résolution</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<NoteFinale> notesFinales = (List<NoteFinale>) request.getAttribute("notesFinales");
                        if (notesFinales != null && !notesFinales.isEmpty()) {
                            for (NoteFinale nf : notesFinales) {
                    %>
                        <tr>
                            <td class="ps-4 fw-medium"><%= nf.getCandidat().getNom() %> <%= nf.getCandidat().getPrenom() %></td>
                            <td class="text-secondary"><%= nf.getMatiere().getNom() %></td>
                            <td>
                                <span class="fw-bold text-primary"><%= nf.getValeurNoteFinale() %> / 20</span>
                            </td>
                            <td class="text-center">
                                <span class="badge bg-light text-secondary border fw-normal" style="border-radius: 5px;">
                                    <%= nf.getResolutionUtilisee().getLibelleNote() %>
                                </span>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="4" class="text-center py-5 text-muted small">
                                Aucune donnée disponible.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
    
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>