<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Matiere, com.restservice.notecorrection.entity.Candidat, com.restservice.notecorrection.entity.Correcteur" %>
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/parametres/liste">Paramètres</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/notes/liste">Notes</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header bg-warning text-white">
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
                        
                        <form action="${pageContext.request.contextPath}/notes/modifier/${note.id}" method="post">
                            <input type="hidden" name="id" value="${note.id}" />
                            
                            <div class="mb-3">
                                <label for="matiereId" class="form-label">Matière *</label>
                                <select class="form-control" id="matiereId" name="matiere.id" required>
                                    <option value="">Sélectionnez une matière</option>
                                    <%
                                        List<Matiere> matieres = (List<Matiere>) request.getAttribute("matieres");
                                        Integer matiereId = request.getAttribute("matiereId") != null ? 
                                            (Integer) request.getAttribute("matiereId") : 
                                            (request.getAttribute("note") != null ? 
                                                ((com.restservice.notecorrection.entity.Note) request.getAttribute("note")).getMatiere().getId() : null);
                                        
                                        if (matieres != null) {
                                            for (Matiere m : matieres) {
                                                String selected = (matiereId != null && matiereId.equals(m.getId())) ? "selected" : "";
                                    %>
                                        <option value="<%= m.getId() %>" <%= selected %>>
                                            <%= m.getNom() %>
                                        </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="candidatId" class="form-label">Candidat *</label>
                                <select class="form-control" id="candidatId" name="candidat.id" required>
                                    <option value="">Sélectionnez un candidat</option>
                                    <%
                                        List<Candidat> candidats = (List<Candidat>) request.getAttribute("candidats");
                                        Integer candidatId = request.getAttribute("candidatId") != null ? 
                                            (Integer) request.getAttribute("candidatId") :
                                            (request.getAttribute("note") != null ? 
                                                ((com.restservice.notecorrection.entity.Note) request.getAttribute("note")).getCandidat().getId() : null);
                                        
                                        if (candidats != null) {
                                            for (Candidat c : candidats) {
                                                String selected = (candidatId != null && candidatId.equals(c.getId())) ? "selected" : "";
                                    %>
                                        <option value="<%= c.getId() %>" <%= selected %>>
                                            <%= c.getNom() %> <%= c.getPrenom() %>
                                        </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="correcteurId" class="form-label">Correcteur *</label>
                                <select class="form-control" id="correcteurId" name="correcteur.id" required>
                                    <option value="">Sélectionnez un correcteur</option>
                                    <%
                                        List<Correcteur> correcteurs = (List<Correcteur>) request.getAttribute("correcteurs");
                                        Integer correcteurId = request.getAttribute("correcteurId") != null ? 
                                            (Integer) request.getAttribute("correcteurId") :
                                            (request.getAttribute("note") != null ? 
                                                ((com.restservice.notecorrection.entity.Note) request.getAttribute("note")).getCorrecteur().getId() : null);
                                        
                                        if (correcteurs != null) {
                                            for (Correcteur c : correcteurs) {
                                                String selected = (correcteurId != null && correcteurId.equals(c.getId())) ? "selected" : "";
                                    %>
                                        <option value="<%= c.getId() %>" <%= selected %>>
                                            <%= c.getNom() %> <%= c.getPrenom() %>
                                        </option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                            
                            <div class="mb-3">
                                <label for="valeurNote" class="form-label">Note *</label>
                                <input type="number" step="0.01" min="0" max="20" class="form-control" 
                                       id="valeurNote" name="valeurNote" value="${note.valeurNote}" required>
                            </div>
                            
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/notes/liste" class="btn btn-secondary">
                                    <i class="bi bi-arrow-left"></i> Annuler
                                </a>
                                <button type="submit" class="btn btn-warning">
                                    <i class="bi bi-save"></i> Modifier
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