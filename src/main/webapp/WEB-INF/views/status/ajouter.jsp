<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Matiere, com.restservice.notecorrection.entity.Operateur, com.restservice.notecorrection.entity.Resolution" %>

<div class="container-fluid py-5">
    <div class="row">
        <div class="col-lg-12">
            
            <div class="mb-5">
                <h2 class="fw-bold text-dark mb-1">${titre}</h2>
                <p class="text-muted">Configurez les règles de correction et les seuils de tolérance par matière.</p>
            </div>

            <div class="card border-0 shadow-sm p-4" style="border-radius: 24px;">
                <div class="card-body">
                    
                    <%-- Alertes d'erreurs --%>
                    <%
                        String errorMessage = (String) request.getAttribute("errorMessage");
                        if (errorMessage != null) {
                    %>
                        <div class="alert alert-danger border-0 small mb-4" style="border-radius: 12px; background-color: #fff5f5; color: #e53e3e;">
                            <i class="bi bi-exclamation-triangle me-2"></i> <%= errorMessage %>
                        </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/parametres/ajouter" method="post">
                        
                        <div class="row">
                            <div class="col-md-6 border-end-md pe-md-4">
                                <div class="mb-4">
                                    <label for="matiereId" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Matière *</label>
                                    <select class="form-select form-select-lg border-light bg-light" id="matiereId" name="matiere.id" required style="border-radius: 12px; font-size: 0.95rem;">
                                        <option value="">Sélectionnez la matière cible</option>
                                        <%
                                            List<Matiere> matieres = (List<Matiere>) request.getAttribute("matieres");
                                            if (matieres != null) {
                                                for (Matiere m : matieres) {
                                        %>
                                            <option value="<%= m.getId() %>"><%= m.getNom() %> (Coefficient: <%= m.getCoefficient() %>)</option>
                                        <%
                                                }
                                            }
                                        %>
                                    </select>
                                </div>

                                <div class="mb-4">
                                    <label for="ecartMax" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Diff</label>
                                    <div class="input-group">
                                        <input type="number" step="0.01" min="0.01" class="form-control form-control-lg border-light bg-light" 
                                               id="ecartMax" name="ecartMax" placeholder="0.00" required
                                               style="border-top-left-radius: 12px; border-bottom-left-radius: 12px; font-size: 0.95rem;">
                                        <span class="input-group-text border-light bg-light text-muted" style="border-top-right-radius: 12px; border-bottom-right-radius: 12px;">points</span>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-6 ps-md-4">
                                <div class="mb-4">
                                    <label for="operateurId" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Opérateur de comparaison *</label>
                                    <select class="form-select form-select-lg border-light bg-light" id="operateurId" name="operateur.id" required style="border-radius: 12px; font-size: 0.95rem;">
                                        <option value="">Operateurr</option>
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

                                <div class="mb-4">
                                    <label for="resolutionId" class="form-label text-secondary small fw-bold text-uppercase" style="letter-spacing: 0.5px;">Mode de résolution *</label>
                                    <select class="form-select form-select-lg border-light bg-light" id="resolutionId" name="resolution.id" required style="border-radius: 12px; font-size: 0.95rem;">
                                        <option value="">Resolutionn an </option>
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
                            </div>
                        </div>

                        <hr class="my-5 opacity-25">
                        
                        <div class="d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/parametres/liste" class="text-secondary text-decoration-none small">
                                <i class="bi bi-chevron-left"></i> Retour à la liste
                            </a>
                            <div class="d-flex gap-3">
                                <button type="reset" class="btn btn-light px-4" style="border-radius: 12px; font-weight: 600;">Réinitialiser</button>
                                <button type="submit" class="btn btn-primary px-5 shadow-sm" style="border-radius: 12px; font-weight: 600; background: #5a67d8; border: none;">
                                    <i class="bi bi-cloud-check me-2"></i> Enregistrer la configuration
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