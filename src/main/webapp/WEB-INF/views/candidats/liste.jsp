<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Candidat" %>

<div class="container-fluid py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold text-dark mb-1">${titre}</h2>
            <p class="text-muted small">${sousTitre}</p>
        </div>
        <a href="${pageContext.request.contextPath}/candidats/ajouter" class="btn btn-primary shadow-sm" style="border-radius: 10px;">
            <i class="bi bi-person-plus-fill me-1"></i> Nouveau candidat
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
            <i class="bi bi-check2-all me-2"></i> <%= successMessage %>
        </div>
    <% } %>

    <div class="card border-0 shadow-sm" style="border-radius: 20px; overflow: hidden;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted small fw-bold" style="width: 10%;">ID</th>
                        <th class="py-3 text-muted small fw-bold">NOM & PRÉNOM</th>
                        <th class="pe-4 py-3 text-muted small fw-bold text-end" style="width: 20%;">ACTIONS</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Candidat> candidats = (List<Candidat>) request.getAttribute("candidats");
                        if (candidats != null && !candidats.isEmpty()) {
                            for (Candidat c : candidats) {
                    %>
                        <tr>
                            <td class="ps-4 text-muted small">#<%= c.getId() %></td>
                            <td>
                                <div class="d-flex align-items-center">
                                    <div class="avatar-sm me-3 bg-soft-primary rounded-circle d-flex align-items-center justify-content-center" 
                                         style="width: 32px; height: 32px; background: #f0f4ff; color: #5a67d8;">
                                        <i class="bi bi-person-circle"></i>
                                    </div>
                                    <span class="fw-bold text-dark text-uppercase"><%= c.getNom() %></span>
                                    <span class="ms-2 text-secondary"><%= c.getPrenom() %></span>
                                </div>
                            </td>
                            <td class="pe-4 text-end">
                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/candidats/modifier/<%= c.getId() %>" 
                                       class="btn btn-sm btn-light text-warning border-0" title="Modifier">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/candidats/supprimer/<%= c.getId() %>" 
                                       class="btn btn-sm btn-light text-danger border-0"
                                       onclick="return confirm('Supprimer ce candidat ?')" title="Supprimer">
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
                            <td colspan="3" class="text-center py-5 text-muted">
                                <i class="bi bi-people mb-2 fs-4 d-block"></i>
                                Aucun candidat dans la base de données.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>