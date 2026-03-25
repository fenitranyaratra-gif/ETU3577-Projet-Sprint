<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Status" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="d-flex justify-content-end mb-3">
        <a href="/status/ajouter" class="btn btn-primary">Ajouter un Statut</a>
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID</th>
                        <th class="py-3 text-muted fw-semibold">Libellé</th>
                        <th class="py-3 text-muted fw-semibold text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Status> statusList = (List<Status>) request.getAttribute("statusList");
                        if (statusList != null && !statusList.isEmpty()) {
                            for (Status status : statusList) {
                    %>
                        <tr>
                            <td class="ps-4 fw-medium"><%= status.getIdStatus() %></td>
                            <td class="text-secondary"><%= status.getLibelle() %></td>
                            <td class="text-center">
                                <a href="/status/modifier/<%= status.getIdStatus() %>" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="/status/supprimer/<%= status.getIdStatus() %>" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ce statut ?')">Supprimer</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="3" class="text-center py-5 text-muted small">
                                Aucun statut trouvé.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

