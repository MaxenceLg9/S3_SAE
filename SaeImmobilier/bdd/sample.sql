USE bdImmo;

INSERT INTO Bien (IdProprio,ComplementAdresse,Adresse,Ville,CodePostal,TypeBien,Surface,NombrePieces,NumeroFiscal,DateAjout,IdAssurance,IdImmeuble)
VALUES
    (1, 'Appartement A3', '12 Rue des Lilas', 'Paris', '75015', 'IMMEUBLE', 45.5, 2, '123456789', '2024-03-03', 2, NULL),
    (2, 'Bâtiment B', '34 Avenue de la République', 'Lyon', '69003', 'IMMEUBLE', 120.0, 5, '987654321', '2024-02-15', 3, NULL),
    (3, '', '56 Boulevard Haussmann', 'Paris', '75008', 'IMMEUBLE', 30.0, 1, '112233445', '2024-01-20', NULL, NULL),
    (4, 'Appartement C5', '89 Rue Victor Hugo', 'Marseille', '65000', 'IMMEUBLE', 60.5, 3, '556677889', '2024-03-01', 1, NULL),
    (5, '', '7 Place Bellecour', 'Lyon', '69002', 'IMMEUBLE', 80.0, 3, '667788990', '2024-02-28', 4, NULL);
