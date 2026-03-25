<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Client" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="d-flex justify-content-end mb-3">
        <a href="/clients/ajouter" class="btn btn-primary">Ajouter un Client</a>
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID</th>
                        <th class="py-3 text-muted fw-semibold">Nom</th>
                        <th class="py-3 text-muted fw-semibold">Contact</th>
                        <th class="py-3 text-muted fw-semibold text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Client> clients = (List<Client>) request.getAttribute("clients");
                        if (clients != null && !clients.isEmpty()) {
                            for (Client client : clients) {
                    %>
                        <tr>
                            <td class="ps-4 fw-medium"><%= client.getIdClient() %></td>
                            <td class="text-secondary"><%= client.getNom() %></td>
                            <td class="text-secondary"><%= client.getContact() %></td>
                            <td class="text-center">
                                <a href="/clients/modifier/<%= client.getIdClient() %>" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="/clients/supprimer/<%= client.getIdClient() %>" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ce client ?')">Supprimer</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="4" class="text-center py-5 text-muted small">
                                Aucun client trouvé.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

