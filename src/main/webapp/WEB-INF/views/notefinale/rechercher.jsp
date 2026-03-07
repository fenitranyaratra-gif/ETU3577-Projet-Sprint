<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Candidat, com.restservice.notecorrection.entity.Matiere" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="container-fluid">


    <%-- Messages flash --%>
    <%-- (Garder ton code Java existant ici pour les alertes) --%>

    <div class="row">
        <div class="col-lg-12">
            <div class="card p-4">
                <div class="card-header px-0">
                    <h4 class="mb-0"><i class="bi bi-search me-2"></i> ${titre}</h4>
                </div>
                <div class="card-body px-0 pt-4">
                    <form action="${pageContext.request.contextPath}/notefinale/resultat" method="post">
                        <div class="mb-4">
                            <label for="idCandidat" class="form-label text-muted small fw-bold">CANDIDAT</label>
                            <select class="form-select" id="idCandidat" name="idCandidat" required>
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
                        
                        <div class="mb-4">
                            <label for="idMatiere" class="form-label text-muted small fw-bold">MATIÈRE</label>
                            <select class="form-select" id="idMatiere" name="idMatiere" required>
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
                        
                        <div class="d-flex gap-3">
                            <button type="submit" class="btn btn-primary flex-grow-1">
                                <i class="bi bi-search me-2"></i> Rechercher
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