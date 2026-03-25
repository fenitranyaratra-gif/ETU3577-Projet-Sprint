<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, sprint.forage.entity.Client" %>
<%
    Client client = (Client) request.getAttribute("client");
%>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card border-0 shadow-sm" style="border-radius: 15px;">
                <div class="card-body p-4">
                    <form action="/clients/modifier/<%= client.getIdClient() %>" method="post">
                        <div class="mb-3">
                            <label for="nom" class="form-label">Nom *</label>
                            <input type="text" class="form-control" id="nom" name="nom" value="<%= client.getNom() %>" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="contact" class="form-label">Contact *</label>
                            <input type="text" class="form-control" id="contact" name="contact" value="<%= client.getContact() %>" required>
                        </div>
                        
                        <div class="d-flex justify-content-between">
                            <a href="/clients/liste" class="btn btn-secondary">Annuler</a>
                            <button type="submit" class="btn btn-primary">Modifier</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

