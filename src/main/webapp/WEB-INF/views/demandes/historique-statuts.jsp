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
        <div class="col-md-4">
            <div class="card border-0 shadow-sm mb-4" style="border-radius: 15px;">
                <div class="card-header bg-white border-0 pt-4">
                    <h5 class="fw-bold">Informations de la demande</h5>
                </div>
                <div class="card-body">
                    <table class="table table-borderless">
                        <tr>
                            <th width="40%">ID Demande :</th>
                            <td>#<%= demande.getIdDemande() %></td>
                        </tr>
                        <tr>
                            <th>Client :</th>
                            <td><%= demande.getClient().getNom() %></td>
                        </tr>
                        <tr>
                            <th>Lieu :</th>
                            <td><%= demande.getLieu() %></td>
                        </tr>
                        <tr>
                            <th>Adresse :</th>
                            <td><%= demande.getAdresse() != null ? demande.getAdresse() : "-" %></td>
                        </tr>
                        <tr>
                            <th>District :</th>
                            <td><%= demande.getDistrict() != null ? demande.getDistrict() : "-" %></td>
                        </tr>
                        <tr>
                            <th>Date création :</th>
                            <td><%= demande.getDate() %></td>
                        </tr>
                    </table>
                </div>
                <div class="card-footer bg-white border-0 pb-4">
                    <a href="/demandes/changer-statut/<%= demande.getIdDemande() %>" class="btn btn-warning">Changer le statut</a>
                    <a href="/demandes/statuts" class="btn btn-secondary">Retour à la liste</a>
                </div>
            </div>
        </div>
        
        <div class="col-md-8">
            <div class="card border-0 shadow-sm" style="border-radius: 15px;">
                <div class="card-header bg-white border-0 pt-4">
                    <h5 class="fw-bold">Historique des statuts</h5>
                </div>
                <div class="card-body">
                    <div class="timeline">
                        <%
                            if (historiqueStatus != null && !historiqueStatus.isEmpty()) {
                                for (int i = 0; i < historiqueStatus.size(); i++) {
                                    DemandeStatus ds = historiqueStatus.get(i);
                                    String badgeClass = "";
                                    String libelle = ds.getStatus().getLibelle();
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
                        %>
                            <div class="timeline-item mb-4">
                                <div class="d-flex">
                                    <div class="timeline-badge me-3">
                                        <span class="badge <%= badgeClass %> rounded-circle p-2">
                                            <%= i + 1 %>
                                        </span>
                                    </div>
                                    <div class="timeline-content flex-grow-1">
                                        <div class="card">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-center">
                                                    <h6 class="mb-0">
                                                        <span class="badge <%= badgeClass %> p-2">
                                                            <%= libelle %>
                                                        </span>
                                                    </h6>
                                                    <small class="text-muted"><%= ds.getDate() %></small>
                                                </div>
                                                <hr class="my-2">
                                                <p class="mb-0 text-muted small">
                                                    Statut mis à jour le <%= ds.getDate() %>
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        <%
                                }
                            } else {
                        %>
                            <div class="text-center py-5 text-muted">
                                Aucun historique de statut disponible.
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
.timeline-item {
    position: relative;
}
.timeline-badge {
    width: 40px;
    text-align: center;
}
.timeline-badge .badge {
    width: 35px;
    height: 35px;
    display: flex;
    align-items: center;
    justify-content: center;
}
.timeline-content .card {
    transition: transform 0.2s;
}
.timeline-content .card:hover {
    transform: translateX(5px);
    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}
</style>

