<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Parametre" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${titre}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/parametres/liste">Paramètres</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/notes/liste">Notes</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
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

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h1>${titre}</h1>
            <a href="${pageContext.request.contextPath}/parametres/ajouter" class="btn btn-success">
                <i class="bi bi-plus-circle"></i> Ajouter un paramètre
            </a>
        </div>
        
        <p class="lead">${sousTitre}</p>
        
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Matière</th>
                        <th>Coefficient</th>
                        <th>Écart Max</th>
                        <th>Opérateur</th>
                        <th>Résolution</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Parametre> parametres = (List<Parametre>) request.getAttribute("parametres");
                        if (parametres != null && !parametres.isEmpty()) {
                            for (Parametre p : parametres) {
                    %>
                        <tr>
                            <td><%= p.getId() %></td>
                            <td><%= p.getMatiere().getNom() %></td>
                            <td><%= p.getMatiere().getCoefficient() %></td>
                            <td><%= p.getEcartMax() %></td>
                            <td><%= p.getOperateur().getSymbole() %></td>
                            <td><%= p.getResolution().getLibelleNote() %></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/parametres/modifier/<%= p.getId() %>" class="btn btn-sm btn-warning">
                                    <i class="bi bi-pencil"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/parametres/supprimer/<%= p.getId() %>" 
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce paramètre ?')">
                                    <i class="bi bi-trash"></i>
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
        
        <% if (parametres == null || parametres.isEmpty()) { %>
            <div class="alert alert-info">
                Aucun paramètre trouvé.
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