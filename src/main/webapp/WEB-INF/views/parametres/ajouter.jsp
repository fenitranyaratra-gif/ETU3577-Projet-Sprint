<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Matiere, com.restservice.notecorrection.entity.Operateur, com.restservice.notecorrection.entity.Resolution" %>
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
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h4 class="mb-0">${titre}</h4>
                    </div>
                    <div class="card-body">
                        
                        <%-- Affichage des erreurs --%>
                        <%
                            String errorMessage = (String) request.getAttribute("errorMessage");
                            if (errorMessage != null) {
                        %>
                            <div class="alert alert-danger" role="alert">
                                <%= errorMessage %>
                            </div>
                        <% } %>
                        
                        <form action="${pageContext.request.contextPath}/parametres/ajouter" method="post">
                            <div class="mb-3">
                                <label for="matiereId" class="form-label">Matière *</label>
                                <select class="form-control" id="matiereId" name="matiere.id" required>
                                    <option value="">Sélectionnez une matière</option>
                                    <%
                                        List<Matiere> matieres = (List<Matiere>) request.getAttribute("matieres");
                                        if (matieres != null) {
                                            for (Matiere m : matieres) {
                                    %>
                                        <option value="<%= m.getId() %>"><%= m.getNom() %> (Coef: <%= m.getCoefficient() %>)</option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="ecartMax" class="form-label">Écart Maximum *</label>
                                <input type="number" step="0.01" min="0.01" class="form-control" 
                                       id="ecartMax" name="ecartMax" required>
                                <small class="text-muted">La différence maximale autorisée entre deux notes</small>
                            </div>
                            
                            <div class="mb-3">
                                <label for="operateurId" class="form-label">Opérateur *</label>
                                <select class="form-control" id="operateurId" name="operateur.id" required>
                                    <option value="">Sélectionnez un opérateur</option>
                                    <%
                                        List<Operateur> operateurs = (List<Operateur>) request.getAttribute("operateurs");
                                        if (operateurs != null) {
                                            for (Operateur o : operateurs) {
                                    %>
                                        <option value="<%= o.getId() %>"><%= o.getSymbole() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="resolutionId" class="form-label">Résolution *</label>
                                <select class="form-control" id="resolutionId" name="resolution.id" required>
                                    <option value="">Sélectionnez une résolution</option>
                                    <%
                                        List<Resolution> resolutions = (List<Resolution>) request.getAttribute("resolutions");
                                        if (resolutions != null) {
                                            for (Resolution r : resolutions) {
                                    %>
                                        <option value="<%= r.getId() %>"><%= r.getLibelleNote() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/parametres/liste" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Annuler
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="bi bi-save"></i> Enregistrer
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>