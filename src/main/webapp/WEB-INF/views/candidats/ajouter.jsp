<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>

<div class="container-fluid py-5">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-12">
            
            <div class="text-center mb-4">
                <h2 class="fw-bold text-dark">${titre}</h2>
                <p class="text-muted small">Veuillez renseigner les informations du candidat</p>
            </div>

            <div class="card border-0 shadow-sm p-4" style="border-radius: 24px;">
                <div class="card-body">
                    
                    <%-- Gestion des erreurs épurée --%>
                    <%
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null) {
                    %>
                        <div class="alert alert-danger border-0 small mb-4" style="border-radius: 12px; background-color: #fff5f5; color: #e53e3e;">
                            <i class="bi bi-exclamation-circle me-2"></i> <%= errorMessage %>
                        </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/candidats/ajouter" method="post">
                        
                        <div class="mb-4">
                            <label for="nom" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Nom *</label>
                            <input type="text" class="form-control form-control-lg border-light bg-light" 
                                   id="nom" name="nom" value="${candidat.nom}" 
                                   placeholder="Ex: RAKOTO" required 
                                   style="border-radius: 12px; font-size: 0.95rem;">
                        </div>
                        
                        <div class="mb-4">
                            <label for="prenom" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Prénom</label>
                            <input type="text" class="form-control form-control-lg border-light bg-light" 
                                   id="prenom" name="prenom" value="${candidat.prenom}" 
                                   placeholder="Ex: Jean"
                                   style="border-radius: 12px; font-size: 0.95rem;">
                        </div>

                        <div class="d-grid gap-3 mt-5">
                            <button type="submit" class="btn btn-primary btn-lg shadow-sm" style="border-radius: 12px; font-weight: 600;">
                                <i class="bi bi-check2-circle me-2"></i> Enregistrer le candidat
                            </button>
                            <a href="${pageContext.request.contextPath}/candidats/liste" class="btn btn-link text-muted text-decoration-none small">
                                <i class="bi bi-arrow-left"></i> Annuler et revenir à la liste
                            </a>
                        </div>

                    </form>
                </div>
            </div>

            <div class="text-center mt-4">
                <p class="text-muted small" style="font-size: 0.75rem;">* Champs obligatoires</p>
            </div>

        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>