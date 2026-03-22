<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Demande, sprint.forage.entity.DemandeStatus" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID Demande</th>
                        <th class="py-3 text-muted fw-semibold">Client</th>
                        <th class="py-3 text-muted fw-semibold">Lieu</th>
                        <th class="py-3 text-muted fw-semibold">Statut Actuel</th>
                        <th class="py-3 text-muted fw-semibold">Dernière mise à jour</th>
                        <th class="py-3 text-muted fw-semibold text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Demande> demandes = (List<Demande>) request.getAttribute("demandes");
                        if (demandes != null && !demandes.isEmpty()) {
                            for (Demande demande : demandes) {
                                DemandeStatus statutActuel = (DemandeStatus) request.getAttribute("statut_" + demande.getIdDemande());
                                if (statutActuel == null) {
                                    statutActuel = (DemandeStatus) session.getAttribute("statut_" + demande.getIdDemande());
                                }
                                String badgeClass = "";
                                if (statutActuel != null) {
                                    String libelle = statutActuel.getStatus().getLibelle();
                                    if (libelle.equals("En attente")) badgeClass = "bg-secondary";
                                    else if (libelle.equals("En cours")) badgeClass = "bg-info";
                                    else if (libelle.equals("Devis envoyé")) badgeClass = "bg-primary";
                                    else if (libelle.equals("Devis accepté")) badgeClass = "bg-success";
                                    else if (libelle.equals("Devis refusé")) badgeClass = "bg-danger";
                                    else if (libelle.equals("Accepté")) badgeClass = "bg-success";
                                    else if (libelle.equals("Refusé")) badgeClass = "bg-danger";
                                    else if (libelle.equals("Annulé")) badgeClass = "bg-dark";
                                    else if (libelle.equals("Terminé")) badgeClass = "bg-success";
                                    else badgeClass = "bg-secondary";
                                }
                    %>
                        <tr>
                            <td class="ps-4 fw-medium">#<%= demande.getIdDemande() %></td>
                            <td class="text-secondary"><%= demande.getClient().getNom() %></td>
                            <td class="text-secondary"><%= demande.getLieu() %></td>
                            <td>
                                <% if (statutActuel != null) { %>
                                    <span class="badge <%= badgeClass %> p-2">
                                        <%= statutActuel.getStatus().getLibelle() %>
                                    </span>
                                    <br>
                                    <small class="text-muted"><%= statutActuel.getDate() %></small>
                                <% } else { %>
                                    <span class="badge bg-secondary">Non défini</span>
                                <% } %>
                            </td>
                            <td class="text-secondary">
                                <%= statutActuel != null ? statutActuel.getDate() : "-" %>
                            </td>
                            <td class="text-center">
                                <a href="/demandes/changer-statut/<%= demande.getIdDemande() %>" class="btn btn-sm btn-warning me-1">
                                    <i class="bi bi-pencil"></i> Modifier statut
                                </a>
                                <a href="/demandes/historique-statuts/<%= demande.getIdDemande() %>" class="btn btn-sm btn-info">
                                    <i class="bi bi-clock-history"></i> Historique
                                </a>
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

