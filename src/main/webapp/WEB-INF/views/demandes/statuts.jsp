<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Demande, sprint.forage.entity.DemandeStatus, sprint.forage.entity.Status" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="card border-0 shadow-sm mb-4" style="border-radius: 15px;">
        <div class="card-body p-3">
            <h6 class="fw-semibold mb-3">Ajouter / Modifier un statut</h6>
            <form action="/demandes/ajouter-statut" method="post" class="row g-2 align-items-end">
                <div class="col-auto">
                    <label class="form-label small text-muted mb-1">Demande</label>
                    <select name="demandeId" class="form-select form-select-sm" required>
                        <option value="">-- Demande --</option>
                        <%
                            List<Demande> demandesForm = (List<Demande>) request.getAttribute("demandes");
                            if (demandesForm != null) {
                                for (Demande d : demandesForm) {
                        %>
                            <option value="<%= d.getIdDemande() %>">#<%= d.getIdDemande() %> - <%= d.getClient().getNom() %> (<%= d.getLieu() %>)</option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="col-auto">
                    <label class="form-label small text-muted mb-1">Statut</label>
                    <select name="statusId" class="form-select form-select-sm" required>
                        <option value="">-- Statut --</option>
                        <%
                            List<Status> statusList = (List<Status>) request.getAttribute("statusList");
                            if (statusList != null) {
                                for (Status s : statusList) {
                        %>
                            <option value="<%= s.getIdStatus() %>"><%= s.getLibelle() %></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="col-auto">
                    <button type="submit" class="btn btn-primary btn-sm">Appliquer</button>
                </div>
            </form>
        </div>
    </div>
<%-- Tableau --%>
    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID</th>
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
                    %>
                        <tr>
                            <td class="ps-4 fw-medium">#<%= demande.getIdDemande() %></td>
                            <td class="text-secondary"><%= demande.getClient().getNom() %></td>
                            <td class="text-secondary"><%= demande.getLieu() %></td>
                            <td class="text-secondary">
                                <%= statutActuel != null ? statutActuel.getStatus().getLibelle() : "Non défini" %>
                            </td>
                            <td class="text-secondary">
                                <%= statutActuel != null ? statutActuel.getDate() : "-" %>
                            </td>
                            <td class="text-center">
                                <a href="/demandes/historique-statuts/<%= demande.getIdDemande() %>" class="btn btn-sm btn-outline-secondary">
                                    <i class="bi bi-clock-history"></i> Historique
                                </a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="5" class="text-center py-5 text-muted small">Aucune demande trouvée.</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
