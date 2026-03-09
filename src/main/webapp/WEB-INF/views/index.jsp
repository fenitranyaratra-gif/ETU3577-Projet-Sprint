<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Notes - Accueil</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/">Gestion Notes</a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/candidats/liste">Candidats</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/parametres/liste">Paramètres</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/notes/liste">Notes</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-5">
        <div class="row">
            <div class="col-12 text-center mb-5">
                <h1>Système de Gestion des Notes</h1>
                <p class="lead">Application de gestion des notes et paramètres de correction</p>
            </div>
        </div>
        
        <div class="row justify-content-center">
            <div class="col-md-4 mb-4">
                <div class="card text-center h-100">
                    <div class="card-body">
                        <h5 class="card-title">Candidats</h5>
                        <p class="card-text">Gérer les candidats (ajout, modification, suppression)</p>
                        <a href="${pageContext.request.contextPath}/candidats/liste" class="btn btn-primary">Accéder</a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4 mb-4">
                <div class="card text-center h-100">
                    <div class="card-body">
                        <h5 class="card-title">Paramètres</h5>
                        <p class="card-text">Configurer les paramètres de correction par matière</p>
                        <a href="${pageContext.request.contextPath}/parametres/liste" class="btn btn-primary">Accéder</a>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4 mb-4">
                <div class="card text-center h-100">
                    <div class="card-body">
                        <h5 class="card-title">Notes</h5>
                        <p class="card-text">Gérer les notes des candidats par correcteur</p>
                        <a href="${pageContext.request.contextPath}/notes/liste" class="btn btn-primary">Accéder</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>