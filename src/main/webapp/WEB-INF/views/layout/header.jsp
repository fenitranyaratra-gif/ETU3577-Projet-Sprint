<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${titre}</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css">
    
    <style>
        :root {
            --sidebar-bg: #1a1a2e;
            --main-bg: #f8f9fc;
            --accent-color: #6366f1;
            --text-muted: #8e8e93;
        }

        body {
            font-family: 'Outfit', sans-serif;
            background-color: var(--main-bg);
            display: flex;
            min-height: 100vh;
            margin: 0;
        }

        /* Sidebar Style */
        .sidebar {
            width: 260px;
            background-color: var(--sidebar-bg);
            color: white;
            padding: 2rem 1.5rem;
            display: flex;
            flex-direction: column;
            position: fixed;
            height: 100vh;
            z-index: 1000;
        }

        .sidebar-brand {
            font-weight: 700;
            font-size: 1.5rem;
            margin-bottom: 3rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .nav-list {
            list-style: none;
            padding: 0;
            flex-grow: 1;
        }

        .nav-list .nav-item {
            margin-bottom: 0.5rem;
        }

        .nav-list .nav-link {
            color: var(--text-muted);
            padding: 0.8rem 1rem;
            border-radius: 12px;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 15px;
            text-decoration: none;
        }

        .nav-list .nav-link:hover, .nav-list .nav-link.active {
            background-color: rgba(255, 255, 255, 0.1);
            color: white;
        }

        .nav-list .nav-link.active {
            background-color: var(--accent-color);
        }

        /* Main Content Adjustment */
        .main-content {
            margin-left: 260px;
            width: calc(100% - 260px);
            padding: 3rem;
        }

        /* Card Modernization */
        .card {
            border: none;
            border-radius: 24px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.04);
            overflow: hidden;
        }

        .card-header {
            background: white !important;
            border-bottom: 1px solid #f0f0f0;
            padding: 1.5rem;
            color: #1a1a2e !important;
        }

        .form-control, .form-select {
            border-radius: 12px;
            padding: 0.75rem 1.2rem;
            border: 1px solid #e2e8f0;
            background-color: #fcfcfd;
        }

        .btn-primary {
            background-color: var(--accent-color);
            border: none;
            border-radius: 12px;
            padding: 0.75rem 1.5rem;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="sidebar-brand">
            <i class="bi bi-intersect"></i> ETU3577
        </div>
        <ul class="nav-list">
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/candidats/liste"><i class="bi bi-people"></i> Candidats</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/parametres/liste"><i class="bi bi-gear"></i> Paramètres</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/notes/liste"><i class="bi bi-journal-text"></i> Notes</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/notefinale/rechercher"><i class="bi bi-graph-up-arrow"></i> Calculer Note Finale</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/notefinale/liste"><i class="bi bi-graph-up-arrow"></i> Liste Note Finale</a>
            </li>
        </ul>
    </div>
    <div class="main-content">