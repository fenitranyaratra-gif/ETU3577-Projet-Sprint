<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.NoteFinale" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${titre}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Gestion Notes</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/candidats/liste">Candidats</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/parametres/liste">Paramètres</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/notes/liste">Notes</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/notefinale/rechercher">Note Finale</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>${titre}</h1>
            <a href="${pageContext.request.contextPath}/notefinale/rechercher" class="btn btn-primary">
                <i class="bi bi-search"></i> Rechercher
            </a>
        </div>
        
        <div class="alert alert-info">
            <i class="bi bi-info-circle"></i> ${filtre}
        </div>
        
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>Candidat</th>
                        <th>Matière</th>
                        <th>Note finale</th>
                        <th>Résolution utilisée</th>
                        <th>Date de calcul</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<NoteFinale> notesFinales = (List<NoteFinale>) request.getAttribute("notesFinales");
                        if (notesFinales != null && !notesFinales.isEmpty()) {
                            for (NoteFinale nf : notesFinales) {
                    %>
                        <tr>
                            <td><%= nf.getCandidat().getNom() %> <%= nf.getCandidat().getPrenom() %></td>
                            <td><%= nf.getMatiere().getNom() %></td>
                            <td><strong><%= nf.getValeurNoteFinale() %></strong></td>
                            <td><%= nf.getResolutionUtilisee().getLibelleNote() %></td>
                            <td><%= nf.getDateCalcul() %></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/notefinale/resultat?idCandidat=<%= nf.getCandidat().getId() %>&idMatiere=<%= nf.getMatiere().getId() %>" 
                                   class="btn btn-sm btn-info">
                                    <i class="bi bi-eye"></i> Voir
                                </a>
                            </td>
                        </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
        
        <% if (notesFinales == null || notesFinales.isEmpty()) { %>
            <div class="alert alert-warning">
                Aucune note finale trouvée.
            </div>
        <% } %>
        
        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                <i class="bi bi-house"></i> Retour à l'accueil
            </a>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>