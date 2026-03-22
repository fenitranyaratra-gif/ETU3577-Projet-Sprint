<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/layout/header.jsp" %>
<%@ page import="java.util.List, com.example.gestiondevis.entity.TypeDevis" %>

<div class="container-fluid py-4">
    <div class="mb-4">
        <h2 class="fw-bold text-dark">${titre}</h2>
        <hr class="w-25 text-primary" style="height: 3px;">
    </div>

    <div class="d-flex justify-content-end mb-3">
        <a href="/types-devis/ajouter" class="btn btn-primary">Ajouter un Type</a>
    </div>

    <div class="card border-0 shadow-sm" style="border-radius: 15px;">
        <div class="table-responsive">
            <table class="table align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th class="ps-4 py-3 text-muted fw-semibold">ID</th>
                        <th class="py-3 text-muted fw-semibold">Libellé</th>
                        <th class="py-3 text-muted fw-semibold text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<TypeDevis> typesDevis = (List<TypeDevis>) request.getAttribute("typesDevis");
                        if (typesDevis != null && !typesDevis.isEmpty()) {
                            for (TypeDevis type : typesDevis) {
                    %>
                        <tr>
                            <td class="ps-4 fw-medium"><%= type.getIdTypeDevis() %></td>
                            <td class="text-secondary"><%= type.getLibelle() %></td>
                            <td class="text-center">
                                <a href="/types-devis/modifier/<%= type.getIdTypeDevis() %>" class="btn btn-sm btn-warning">Modifier</a>
                                <a href="/types-devis/supprimer/<%= type.getIdTypeDevis() %>" class="btn btn-sm btn-danger" onclick="return confirm('Supprimer ce type ?')">Supprimer</a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="3" class="text-center py-5 text-muted small">
                                Aucun type de devis trouvé.
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/layout/footer.jsp" %>