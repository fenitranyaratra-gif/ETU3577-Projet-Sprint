<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.math.BigDecimal, com.restservice.notecorrection.entity.*" %>
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
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="alert alert-danger" role="alert">
                <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <div class="row">
            <div class="col-md-8 mx-auto">
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0"><i class="bi bi-person"></i> Informations</h4>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <p><strong>Candidat:</strong> ${candidat.nom} ${candidat.prenom}</p>
                            </div>
                            <div class="col-md-6">
                                <p><strong>Matière:</strong> ${matiere.nom} (Coefficient: ${matiere.coefficient})</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header bg-info text-white">
                        <h4 class="mb-0"><i class="bi bi-pencil"></i> Notes brutes</h4>
                    </div>
                    <div class="card-body">
                        <%
                            List<Note> notesBrutes = (List<Note>) request.getAttribute("notesBrutes");
                            if (notesBrutes != null && !notesBrutes.isEmpty()) {
                        %>
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Correcteur</th>
                                        <th class="text-end">Note</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <%
                                    for (Note n : notesBrutes) {
                                %>
                                    <tr>
                                        <td><%= n.getCorrecteur().getNom() %> <%= n.getCorrecteur().getPrenom() %></td>
                                        <td class="text-end"><%= n.getValeurNote() %></td>
                                    </tr>
                                <%
                                    }
                                %>
                                </tbody>
                            </table>
                            
                            <%-- Affichage des paramètres --%>
                            <div class="alert alert-secondary mt-3">
                                <p class="mb-1"><strong>Paramètres de résolution:</strong></p>
                                <p class="mb-0"><%= request.getAttribute("parametreInfo") %></p>
                                <% if (request.getAttribute("ecartCalcule") != null) { %>
                                    <p class="mb-0 mt-2">
                                        <strong>Écart calculé:</strong> <%= request.getAttribute("ecartCalcule") %>
                                        <% if (Boolean.TRUE.equals(request.getAttribute("ecartDepasse"))) { %>
                                            <span class="badge bg-warning">Dépasse le seuil</span>
                                        <% } else { %>
                                            <span class="badge bg-success">Dans le seuil</span>
                                        <% } %>
                                    </p>
                                <% } %>
                            </div>
                        <%
                            } else {
                        %>
                            <div class="alert alert-warning">Aucune note brute trouvée</div>
                        <%
                            }
                        %>
                    </div>
                </div>

                <div class="card mb-4">
                    <div class="card-header bg-success text-white">
                        <h4 class="mb-0"><i class="bi bi-check-circle"></i> Note finale</h4>
                    </div>
                    <div class="card-body text-center">
                        <%
                            NoteFinale noteFinale = (NoteFinale) request.getAttribute("noteFinale");
                            if (noteFinale != null) {
                        %>
                            <h1 class="display-1"><%= noteFinale.getValeurNoteFinale() %></h1>
                            <p class="lead">
                                <span class="badge bg-info">
                                    Résolution utilisée: <%= noteFinale.getResolutionUtilisee().getLibelleNote() %>
                                </span>
                            </p>
                            <p class="text-muted">
                                Calculé le: <%= noteFinale.getDateCalcul() %>
                            </p>
                        <%
                            } else {
                        %>
                            <div class="alert alert-info">
                                <p>Aucune note finale n'a encore été calculée pour ce candidat dans cette matière.</p>
                            </div>
                        <%
                            }
                        %>
                    </div>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/notefinale/rechercher" class="btn btn-secondary">
                        <i class="bi bi-arrow-left"></i> Nouvelle recherche
                    </a>
                    
                    <form action="${pageContext.request.contextPath}/notefinale/calculer" method="post">
                        <input type="hidden" name="idCandidat" value="${candidat.id}" />
                        <input type="hidden" name="idMatiere" value="${matiere.id}" />
                        <button type="submit" class="btn btn-success">
                            <i class="bi bi-calculator"></i> 
                            <%= (noteFinale != null) ? "Recalculer" : "Calculer" %> la note finale
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>