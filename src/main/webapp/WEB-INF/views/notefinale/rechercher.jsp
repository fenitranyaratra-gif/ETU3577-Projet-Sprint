<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Candidat, com.restservice.notecorrection.entity.Matiere" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${titre}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <style>
        .search-card {
            max-width: 600px;
            margin: 0 auto;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }
    </style>
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

    <div class="container mt-5">
        <%-- Messages flash --%>
        <%
            String successMessage = (String) request.getAttribute("successMessage");
            if (successMessage == null && session.getAttribute("successMessage") != null) {
                successMessage = (String) session.getAttribute("successMessage");
                session.removeAttribute("successMessage");
            }
            
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage == null && session.getAttribute("errorMessage") != null) {
                errorMessage = (String) session.getAttribute("errorMessage");
                session.removeAttribute("errorMessage");
            }
        %>
        
        <% if (successMessage != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= successMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>
        
        <% if (errorMessage != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= errorMessage %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <div class="card search-card">
            <div class="card-header bg-primary text-white">
                <h4 class="mb-0"><i class="bi bi-search"></i> ${titre}</h4>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/notefinale/resultat" method="post">
                    <div class="mb-3">
                        <label for="idCandidat" class="form-label">Candidat *</label>
                        <select class="form-control" id="idCandidat" name="idCandidat" required>
                            <option value="">Sélectionnez un candidat</option>
                            <%
                                List<Candidat> candidats = (List<Candidat>) request.getAttribute("candidats");
                                if (candidats != null) {
                                    for (Candidat c : candidats) {
                            %>
                                <option value="<%= c.getId() %>"><%= c.getNom() %> <%= c.getPrenom() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    
                    <div class="mb-3">
                        <label for="idMatiere" class="form-label">Matière *</label>
                        <select class="form-control" id="idMatiere" name="idMatiere" required>
                            <option value="">Sélectionnez une matière</option>
                            <%
                                List<Matiere> matieres = (List<Matiere>) request.getAttribute("matieres");
                                if (matieres != null) {
                                    for (Matiere m : matieres) {
                            %>
                                <option value="<%= m.getId() %>"><%= m.getNom() %></option>
                            <%
                                    }
                                }
                            %>
                        </select>
                    </div>
                    
                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                            <i class="bi bi-house"></i> Accueil
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-search"></i> Rechercher
                        </button>
                    </div>
                </form>
            </div>
        </div>
        
        <div class="text-center mt-4">
            <a href="${pageContext.request.contextPath}/notefinale/liste" class="btn btn-info">
                <i class="bi bi-list"></i> Voir toutes les notes finales
            </a>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>