<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Devis, sprint.forage.entity.Demande, sprint.forage.entity.TypeDevis" %>
<%
    Devis devis = (Devis) request.getAttribute("devis");
    List<Demande> demandes = (List<Demande>) request.getAttribute("demandes");
    List<TypeDevis> typesDevis = (List<TypeDevis>) request.getAttribute("typesDevis");
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
                    <form action="/devis/modifier/<%= devis.getIdDevis() %>" method="post">
                        <div class="mb-3">
                            <label class="form-label">ID Devis</label>
                            <input type="text" class="form-control" value="<%= devis.getIdDevis() %>" disabled>
                        </div>
                        
                        <div class="mb-3">
                            <label for="demandeId" class="form-label">Demande *</label>
                            <select class="form-control" id="demandeId" name="demandeId" required>
                                <option value="">Sélectionner une demande</option>
                                <% for(Demande demande : demandes) { 
                                    String selected = (demande.getIdDemande().equals(devis.getDemande().getIdDemande())) ? "selected" : "";
                                %>
                                    <option value="<%= demande.getIdDemande() %>" <%= selected %>>
                                        Demande N°<%= demande.getIdDemande() %> - <%= demande.getClient().getNom() %> - <%= demande.getLieu() %>
                                    </option>
                                <% } %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label for="typeDevisId" class="form-label">Type de Devis *</label>
                            <select class="form-control" id="typeDevisId" name="typeDevisId" required>
                                <option value="">Sélectionner un type de devis</option>
                                <% for(TypeDevis type : typesDevis) { 
                                    String selected = (type.getIdTypeDevis().equals(devis.getTypeDevis().getIdTypeDevis())) ? "selected" : "";
                                %>
                                    <option value="<%= type.getIdTypeDevis() %>" <%= selected %>><%= type.getLibelle() %></option>
                                <% } %>
                            </select>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Date de création</label>
                            <input type="text" class="form-control" value="<%= devis.getDate() %>" disabled>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <a href="/devis/liste" class="btn btn-secondary">Annuler</a>
                            <button type="submit" class="btn btn-primary">Modifier</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

