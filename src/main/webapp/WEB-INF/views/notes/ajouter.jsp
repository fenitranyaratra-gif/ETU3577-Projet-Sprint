<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Matiere, com.restservice.notecorrection.entity.Candidat, com.restservice.notecorrection.entity.Correcteur" %>

<div class="container-fluid py-5">
    <div class="row">
        <div class="col-lg-12">
            
            <div class="d-flex align-items-center mb-5">
                <div class="bg-primary rounded-circle d-flex align-items-center justify-content-center shadow-sm" style="width: 48px; height: 48px; background: #5a67d8 !important;">
                    <i class="bi bi-pen text-white fs-4"></i>
                </div>
                <div class="ms-3">
                    <h2 class="fw-bold text-dark mb-0">${titre}</h2>
                    <p class="text-muted small mb-0">Enregistrement d'une nouvelle évaluation dans le système</p>
                </div>
            </div>

            <div class="card border-0 shadow-sm p-4" style="border-radius: 24px;">
                <div class="card-body">
                    
                    <%-- Alertes --%>
                    <%
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null) {
                    %>
                        <div class="alert alert-danger border-0 small mb-4 shadow-sm" style="border-radius: 12px; background-color: #fff5f5; color: #e53e3e;">
                            <i class="bi bi-exclamation-octagon-fill me-2"></i> <%= errorMessage %>
                        </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/notes/ajouter" method="post">
                        
                        <div class="row g-4 mb-4">
                            <div class="col-md-4">
                                <label for="matiereId" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Matière *</label>
                                <select class="form-select form-select-lg border-light bg-light shadow-none" id="matiereId" name="matiere.id" required style="border-radius: 12px; font-size: 0.95rem;">
                                    <option value="">-- Choisir la matière --</option>
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
                            
                            <div class="col-md-4">
                                <label for="candidatId" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Candidat *</label>
                                <select class="form-select form-select-lg border-light bg-light shadow-none" id="candidatId" name="candidat.id" required style="border-radius: 12px; font-size: 0.95rem;">
                                    <option value="">-- Sélectionner l'élève --</option>
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

                            <div class="col-md-4">
                                <label for="correcteurId" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Correcteur *</label>
                                <select class="form-select form-select-lg border-light bg-light shadow-none" id="correcteurId" name="correcteur.id" required style="border-radius: 12px; font-size: 0.95rem;">
                                    <option value="">-- Identifier le correcteur --</option>
                                    <%
                                        List<Correcteur> correcteurs = (List<Correcteur>) request.getAttribute("correcteurs");
                                        if (correcteurs != null) {
                                            for (Correcteur c : correcteurs) {
                                    %>
                                        <option value="<%= c.getId() %>"><%= c.getNom() %> <%= c.getPrenom() %></option>
                                    <%
                                            }
                                        }
                                    %>
                                </select>
                            </div>
                        </div>

                        <div class="row g-4 mb-5">
                            <div class="col-md-4">
                                <label for="valeurNote" class="form-label text-primary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Note Finale / 20 *</label>
                                <div class="input-group">
                                    <input type="number" step="0.01" min="0" max="20" class="form-control form-control-lg border-primary bg-white shadow-none" 
                                           id="valeurNote" name="valeurNote" placeholder="0.00" required
                                           style="border-radius: 12px 0 0 12px; font-size: 1.2rem; font-weight: 600;">
                                    <span class="input-group-text bg-primary text-white border-primary" style="border-radius: 0 12px 12px 0; background: #5a67d8 !important; border: none;">
                                        / 20
                                    </span>
                                </div>
                                <div class="form-text mt-2 text-muted" style="font-size: 0.75rem;">Saisissez une valeur décimale entre 0 et 20.</div>
                            </div>
                        </div>

                        <hr class="my-5 opacity-25">

                        <div class="d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/notes/liste" class="btn btn-link text-secondary text-decoration-none small p-0">
                                <i class="bi bi-x-circle me-1"></i> Abandonner la saisie
                            </a>
                            <div class="d-flex gap-3">
                                <button type="submit" class="btn btn-primary px-5 shadow-sm" style="border-radius: 12px; font-weight: 600; background: #5a67d8; border: none; padding-top: 12px; padding-bottom: 12px;">
                                    <i class="bi bi-check2-all me-2"></i> Valider et Enregistrer
                                </button>
                            </div>
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