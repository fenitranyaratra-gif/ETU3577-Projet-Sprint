<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Demande, sprint.forage.entity.DemandeStatus" %>
<%
    Demande demande = (Demande) request.getAttribute("demande");
    List<DemandeStatus> historiqueStatus = (List<DemandeStatus>) request.getAttribute("historiqueStatus");
%>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="row">
        <%-- Infos demande --%>
        <div class="col-md-4">
            <div class="card border-0 shadow-sm mb-4" style="border-radius: 15px;">
                <div class="card-body p-4">
                    <h6 class="fw-bold mb-3">Informations</h6>
                    <table class="table table-borderless table-sm mb-0">
                        <tr><th class="text-muted fw-normal" width="45%">ID</th><td>#<%= demande.getIdDemande() %></td></tr>
                        <tr><th class="text-muted fw-normal">Client</th><td><%= demande.getClient().getNom() %></td></tr>
                        <tr><th class="text-muted fw-normal">Lieu</th><td><%= demande.getLieu() %></td></tr>
                        <tr><th class="text-muted fw-normal">Adresse</th><td><%= demande.getAdresse() != null ? demande.getAdresse() : "-" %></td></tr>
                        <tr><th class="text-muted fw-normal">District</th><td><%= demande.getDistrict() != null ? demande.getDistrict() : "-" %></td></tr>
                        <tr><th class="text-muted fw-normal">Date</th><td><%= demande.getDate() %></td></tr>
                    </table>
                </div>
                <div class="card-footer bg-white border-0 pb-4 px-4">
                    <a href="/demandes/statuts" class="btn btn-sm btn-outline-secondary">← Retour</a>
                </div>
            </div>
        </div>

        <%-- Historique --%>
        <div class="col-md-8">
            <div class="card border-0 shadow-sm" style="border-radius: 15px;">
                <div class="card-body p-4">
                    <h6 class="fw-bold mb-3">Historique des statuts</h6>
                    <%
                        if (historiqueStatus != null && !historiqueStatus.isEmpty()) {
                            for (int i = 0; i < historiqueStatus.size(); i++) {
                                DemandeStatus ds = historiqueStatus.get(i);
                    %>
                        <div class="d-flex align-items-start mb-3">
                            <span class="text-muted me-3" style="min-width: 20px; font-size: 0.85rem;"><%= i + 1 %>.</span>
                            <div>
                                <span class="fw-medium"><%= ds.getStatus().getLibelle() %></span>
                                <br>
                                <small class="text-muted"><%= ds.getDate() %></small>
                            </div>
                        </div>
                        <% if (i < historiqueStatus.size() - 1) { %><hr class="my-2"><% } %>
                    <%
                            }
                        } else {
                    %>
                        <p class="text-muted small text-center py-4 mb-0">Aucun historique disponible.</p>
                    <% } %>
                </div>
            </div>
        </div>
    </div>
</div>
