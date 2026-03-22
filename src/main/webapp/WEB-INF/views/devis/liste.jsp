<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Devis" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="d-flex justify-content-end mb-3">
        <a href="/devis/ajouter" class="btn btn-primary">Ajouter un Devis</a>
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID</th>
                        <th class="py-3 text-muted fw-semibold">Demande</th>
                        <th class="py-3 text-muted fw-semibold">Type Devis</th>
                        <th class="py-3 text-muted fw-semibold">Date</th>
                        <th class="py-3 text-muted fw-semibold text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Devis> devis = (List<Devis>) request.getAttribute("devis");
                        if (devis != null && !devis.isEmpty()) {
                            for (Devis d : devis) {
                    %>
                        <tr>
                            <td class="ps-4 fw-medium"><%= d.getIdDevis() %></td>
                            <td class="text-secondary">Demande N°<%= d.getDemande().getIdDemande() %></td>
                            <td class="text-secondary"><%= d.getTypeDevis().getLibelle() %></td>
                            <td class="text-secondary"><%= d.getDate() %></td>
                            <td class="text-center">
                                <a href="/devis/details/<%= d.getIdDevis() %>" class="btn btn-sm btn-info">Détails</a>
                                <a href="/devis/modifier/<%= d.getIdDevis() %>" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="/devis/supprimer/<%= d.getIdDevis() %>" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ce devis ?')">Supprimer</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="5" class="text-center py-5 text-muted small">
                                Aucun devis trouvé.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

