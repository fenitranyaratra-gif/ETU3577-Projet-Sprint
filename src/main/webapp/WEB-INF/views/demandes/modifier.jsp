<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Client, sprint.forage.entity.Demande" %>
<%
    Demande demande = (Demande) request.getAttribute("demande");
    List<Client> clients = (List<Client>) request.getAttribute("clients");
%>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card border-0 shadow-sm" style="border-radius: 15px;">
                <div class="card-body p-4">
                    <form action="/demandes/modifier/<%= demande.getIdDemande() %>" method="post">
                        <div class="mb-3">
                            <label for="clientId" class="form-label">Client *</label>
                            <select class="form-control" id="clientId" name="clientId" required>
                                <option value="">Sélectionner un client</option>
                                <% for(Client client : clients) { 
                                    String selected = (client.getIdClient().equals(demande.getClient().getIdClient())) ? "selected" : "";
                                %>
                                    <option value="<%= client.getIdClient() %>" <%= selected %>><%= client.getNom() %></option>
                                <% } %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="lieu" class="form-label">Lieu *</label>
                            <input type="text" class="form-control" id="lieu" name="lieu" value="<%= demande.getLieu() %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="adresse" class="form-label">Adresse</label>
                            <textarea class="form-control" id="adresse" name="adresse" rows="3"><%= demande.getAdresse() != null ? demande.getAdresse() : "" %></textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="district" class="form-label">District</label>
                            <input type="text" class="form-control" id="district" name="district" value="<%= demande.getDistrict() != null ? demande.getDistrict() : "" %>">
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Date de création</label>
                            <input type="text" class="form-control" value="<%= demande.getDate() %>" disabled>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <a href="/demandes/liste" class="btn btn-secondary">Annuler</a>
                            <button type="submit" class="btn btn-primary">Modifier</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

