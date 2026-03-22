<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Demande" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="d-flex justify-content-end mb-3">
        <a href="/demandes/ajouter" class="btn btn-primary">Ajouter une Demande</a>
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID</th>
                        <th class="py-3 text-muted fw-semibold">Client</th>
                        <th class="py-3 text-muted fw-semibold">Lieu</th>
                        <th class="py-3 text-muted fw-semibold">District</th>
                        <th class="py-3 text-muted fw-semibold">Date</th>
                        <th class="py-3 text-muted fw-semibold text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Demande> demandes = (List<Demande>) request.getAttribute("demandes");
                        if (demandes != null && !demandes.isEmpty()) {
                            for (Demande demande : demandes) {
                    %>
                        <tr>
                            <td class="ps-4 fw-medium"><%= demande.getIdDemande() %></td>
                            <td class="text-secondary"><%= demande.getClient().getNom() %></td>
                            <td class="text-secondary"><%= demande.getLieu() %></td>
                            <td class="text-secondary"><%= demande.getDistrict() %></td>
                            <td class="text-secondary"><%= demande.getDate() %></td>
                            <td class="text-center">
                                <a href="/demandes/details/<%= demande.getIdDemande() %>" class="btn btn-sm btn-info">Détails</a>
                                <a href="/demandes/modifier/<%= demande.getIdDemande() %>" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="/demandes/supprimer/<%= demande.getIdDemande() %>" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer cette demande ?')">Supprimer</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="6" class="text-center py-5 text-muted small">
                                Aucune demande trouvée.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

