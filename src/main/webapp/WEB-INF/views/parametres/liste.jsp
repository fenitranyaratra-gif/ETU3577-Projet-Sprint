<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Parametre" %>

<div class="container-fluid py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold text-dark mb-1">${titre}</h2>
            <p class="text-muted small">${sousTitre}</p>
        </div>
        <a href="${pageContext.request.contextPath}/parametres/ajouter" class="btn btn-primary shadow-sm" style="border-radius: 10px;">
            <i class="bi bi-gear-wide-connected me-1"></i> Nouveau paramètre
        </a>
    </div>

    <%-- Messages flash --%>
    <%
        String successMessage = (String) request.getAttribute("successMessage");
        if (successMessage == null && session.getAttribute("successMessage") != null) {
            successMessage = (String) session.getAttribute("successMessage");
            session.removeAttribute("successMessage");
        }
        if (successMessage != null) {
    %>
        <div class="alert alert-success border-0 shadow-sm mb-4" style="border-radius: 12px;">
            <i class="bi bi-check-circle me-2"></i> <%= successMessage %>
        </div>
    <% } %>

    <div class="card border-0 shadow-sm" style="border-radius: 20px; overflow: hidden;">
        <div class="table-responsive">
            <table class="table align-middle mb-0 text-nowrap">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted small fw-bold">ID</th>
                        <th class="py-3 text-muted small fw-bold">MATIÈRE</th>
                        <th class="py-3 text-muted small fw-bold text-center">COEFF.</th>
                        <th class="py-3 text-muted small fw-bold text-center">DIFF</th>
                        <th class="py-3 text-muted small fw-bold text-center">OPÉRATEUR</th>
                        <th class="py-3 text-muted small fw-bold">RÉSOLUTION</th>
                        <th class="pe-4 py-3 text-muted small fw-bold text-end">ACTIONS</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Parametre> parametres = (List<Parametre>) request.getAttribute("parametres");
                        if (parametres != null && !parametres.isEmpty()) {
                            for (Parametre p : parametres) {
                    %>
                        <tr>
                            <td class="ps-4 text-muted small">#<%= p.getId() %></td>
                            <td class="fw-bold text-dark"><%= p.getMatiere().getNom() %></td>
                            <td class="text-center">
                                <span class="badge bg-light text-dark border fw-medium"><%= p.getMatiere().getCoefficient() %></span>
                            </td>
                            <td class="text-center text-danger fw-medium"><%= p.getEcartMax() %> </td>
                            <td class="text-center">
                                <code class="px-2 py-1 bg-white border rounded text-primary fw-bold" style="font-size: 1rem;">
                                    <%= p.getOperateur().getSymbole() %>
                                </code>
                            </td>
                            <td>
                                <span class="text-secondary" style="font-size: 0.85rem;"><%= p.getResolution().getLibelleNote() %></span>
                            </td>
                            <td class="pe-4 text-end">
                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/parametres/modifier/<%= p.getId() %>" class="btn btn-sm btn-light text-warning" title="Modifier">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/parametres/supprimer/<%= p.getId() %>" 
                                       class="btn btn-sm btn-light text-danger"
                                       onclick="return confirm('Supprimer ce paramètre de configuration ?')">
                                        <i class="bi bi-trash3"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="7" class="text-center py-5 text-muted">
                                <i class="bi bi-layers-half d-block mb-2 fs-4"></i>
                                Aucun paramètre configuré pour le moment.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/" class="text-secondary text-decoration-none small">
            <i class="bi bi-chevron-left"></i> Retour au tableau de bord
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>