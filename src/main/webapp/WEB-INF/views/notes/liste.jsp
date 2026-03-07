<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.restservice.notecorrection.entity.Note" %>

<div class="container-fluid py-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="fw-bold text-dark mb-1">${titre}</h2>
            <p class="text-muted small">${sousTitre}</p>
        </div>
        <a href="${pageContext.request.contextPath}/notes/ajouter" class="btn btn-primary shadow-sm" style="border-radius: 10px;">
            <i class="bi bi-plus-lg me-1"></i> Ajouter une note
        </a>
    </div>

    <%-- Messages flash optimisés --%>
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
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted small fw-bold">ID</th>
                        <th class="py-3 text-muted small fw-bold">MATIÈRE</th>
                        <th class="py-3 text-muted small fw-bold">CANDIDAT</th>
                        <th class="py-3 text-muted small fw-bold text-center">NOTE</th>
                        <th class="pe-4 py-3 text-muted small fw-bold text-end">ACTIONS</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Note> notes = (List<Note>) request.getAttribute("notes");
                        if (notes != null && !notes.isEmpty()) {
                            for (Note n : notes) {
                    %>
                        <tr>
                            <td class="ps-4 text-muted small">#<%= n.getId() %></td>
                            <td class="fw-medium"><%= n.getMatiere().getNom() %></td>
                            <td>
                                <div class="fw-bold text-dark"><%= n.getCandidat().getNom() %></div>
                                <div class="text-muted small text-uppercase" style="font-size: 0.7rem;">Corr. par <%= n.getCorrecteur().getNom() %></div>
                            </td>
                            <td class="text-center">
                                <span class="badge rounded-pill bg-light text-primary border" style="font-size: 0.9rem;">
                                    <%= n.getValeurNote() %>
                                </span>
                            </td>
                            <td class="pe-4 text-end">
                                <div class="btn-group">
                                    <a href="${pageContext.request.contextPath}/notes/modifier/<%= n.getId() %>" class="btn btn-sm btn-light border-0 text-warning" title="Modifier">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/notes/supprimer/<%= n.getId() %>" 
                                       class="btn btn-sm btn-light border-0 text-danger"
                                       onclick="return confirm('Supprimer cette note ?')">
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
                            <td colspan="5" class="text-center py-5 text-muted">
                                <i class="bi bi-slash-circle d-block mb-2 fs-4"></i>
                                Aucune note enregistrée.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>

    <div class="mt-4">
        <a href="${pageContext.request.contextPath}/" class="text-secondary text-decoration-none small">
            <i class="bi bi-chevron-left"></i> Retour à l'accueil
        </a>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>