CREATE TABLE BiensLouable (
    IdBien INTEGER PRIMARY KEY,
    Adresse varchar(30),
    Ville varchar(20),
    CodePostal varchar(5),
    TypeBien varchar(20) CHECK (TypeBien IN ('Logement', 'Garage')),
    Surface float,
    NombrePieces INTEGER,
    NumeroFiscal varchar(20),
    DateAjout Date,
    CONSTRAINT TypeBien_Specification CHECK (
    (TypeBien = 'Bâtiment' AND Surface IS NULL AND NombrePieces IS NULL) OR
    (TypeBien != 'Bâtiment' AND Surface IS NOT NULL AND NombrePieces IS NOT NULL)
    )
);



CREATE TABLE Bail(
    IdBail INTEGER primary key,
    IdBien INTEGER,
    IdLocataire INTEGER,
    IdEtatDesLieu INTEGER,
    IdChargeEau INTEGER,
    IdChargeElectricite INTEGER,
    IdChargeOrdureMenagere INTEGER,
    IdChargeEntretien INTEGER,
    NbMoisLoue INTEGER,
    DateDebut Date,
    DateFin Date,
    MontantLoyer float,
    TotalCharges float,
    DepotGaranti float,
    TypeBail varchar(20), -- non meublé, meublé, colocation
    Renouvelable Boolean,
    CheminDocument varchar(100),
    DateSignature Date,
    Etat varchar check ( Etat in ('Actif', 'Terminé', 'Preavis') ),
    foreign key (IdBien) references BiensLouable(IdBien),
    foreign key (IdLocataire) references Locataires(IdLocataire),
    foreign key (IdChargeEau) references ChargesEau(IdChargeEau),
    foreign key (IdChargeElectricite) references  ChargesElectricite(IdChargeElectricite),
    foreign key (IdChargeOrdureMenagere) references ChargesOrduresMenagere(IdChargeOrdureMenagere),
    foreign key (IdChargeEntretien) references  ChargesEntretien(IdChargeEntretien),
    foreign key (IdEtatDesLieu) references EtatdesLieux(Id_EtatDesLieu)
);

CREATE TRIGGER CalculTotalCharges
    AFTER INSERT ON Bail
BEGIN
    UPDATE Bail
    SET TotalCharges = (
        SELECT IFNULL(SUM(Montant), 0)
        FROM Charges
        WHERE IdBien = NEW.IdBien
    )
    WHERE IdBail = NEW.IdBail;
END;


CREATE TABLE Locataires (
    IdLocataire INTEGER PRIMARY KEY,
    Nom varchar(20),
    Prenom varchar(20),
    Sexe varchar(20) CHECK (Sexe IN ('M', 'F')),
    Telephone varchar(10),
    Email varchar(20),
    IdColocations INTEGER,
    FOREIGN KEY (IdColocations) REFERENCES Colocations(IdColocation)
);


CREATE TABLE Charges (
    IdCharge INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    Montant float,
    DateCharge DATE,
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien)
);

CREATE TABLE ChargesEau (
    IdChargeEau INTEGER PRIMARY KEY AUTOINCREMENT,
    IdCharge INTEGER,
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateReleve Date,
    NouvelIndice INTEGER,
    AncienIndice INTEGER,
    PartieFixe float,
    PartieVariable float,
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien),
    foreign key (IdLocataire) references Locataires(IdLocataire),
    FOREIGN KEY (IdCharge) references  Charges(IdCharge)
);


CREATE TABLE Travaux (
    IdTravaux INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    NumeroFacture INTEGER,
    AdressePostale varchar(40),
    Etage INTEGER,
    Côté varchar(20),
    TypeBatiment varchar(20),
    Entreprise varchar(20),
    Montant float,
    MontantNonDeductible float,
    MontantADeclarer float,
    Reduction float, -- Stocké en pourcentage, par exemple 0.2 pour 20%
    DateTravaux DATE,
    NatureTravaux varchar(100),
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien)
);

CREATE TRIGGER CalculMontantADeclarer
    AFTER INSERT ON Travaux
BEGIN
    UPDATE Travaux
    SET MontantADeclarer = (Montant - MontantNonDeductible) * (1 - Reduction)
    WHERE IdTravaux = NEW.IdTravaux;
END;


CREATE TABLE Cautions (
    IdCaution INTEGER PRIMARY KEY AUTOINCREMENT,
    Nom varchar(20),
    Prenom varchar(20),
    DateNaissance DATE,
    LieuNaissance varchar(30),
    Email varchar(30),
    SituationFamiliale varchar(20),
    Profession varchar(20),
    Employeur varchar(30),
    TypeContrat varchar(3) CHECK (TypeContrat IN ('CDI', 'CDD')),
    RemunerationMensuelle float,
    AutresRevenus float,
    TotalRevenus float
);

CREATE TRIGGER CalculTotalRevenus
    AFTER INSERT ON Cautions
BEGIN
    UPDATE Cautions
    SET TotalRevenus = RemunerationMensuelle + AutresRevenus
    WHERE IdCaution = NEW.IdCaution;
END;


CREATE TABLE Assurances (
    IdAssurance INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    ProtectionJuridique float,
    QuotitéJurisprudence float,
    Prime float,
    AugmentationAnnuelle float check (AugmentationAnnuelle between 0 and 100),
    TotalPrime float,
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien)
);

CREATE TRIGGER CalculTotalPrime
    AFTER INSERT ON Assurances
BEGIN
    UPDATE Assurances
    SET TotalPrime = ProtectionJuridique + Prime
    WHERE IdAssurance = NEW.IdAssurance;
END;


CREATE TABLE AssocieBailLocataire (
    IDBail INTEGER,
    IDLocataire INTEGER,
    QuotitéLoyer float,
    DateEntree DATE,
    DateSortie DATE,
    PRIMARY KEY (IDBail, IDLocataire),
    FOREIGN KEY (IDBail) REFERENCES Bail(IdBail),
    FOREIGN KEY (IDLocataire) REFERENCES Locataires(IDLocataire)
);



CREATE TABLE EtatdesLieux (
    Id_EtatDesLieu INTEGER primary key autoincrement,
    Id_Bail INTEGER,
    Date_Signature Date,
    Nom_Bailleur  varchar(20),
    Prenom_Bailleur varchar(20),
    Element varchar(20),
    Etat_Element varchar(20) check ( Etat_Element in ('Bon état', 'Etat d''usage', 'neuf', 'Mauvais état') ),
    foreign key (Id_Bail) references Bail(IdBail)
);

Create table ChargesElectricite
(
    IdChargeElectricite INTEGER PRIMARY KEY AUTOINCREMENT,
    IdCharge Integer,
    IdBien             INTEGER,
    IdLocataire INTEGER,
    DateReleve Date,
    Montant float,
    FOREIGN KEY (IdBien) REFERENCES BiensLouable (IdBien),
    foreign key (IdLocataire) references  Locataires(IdLocataire),
    foreign key (IdCharge) references Charges(IdCharge)
);

Create table ChargesOrduresMenagere(
    IdChargeOrdureMenagere INTEGER PRIMARY KEY AUTOINCREMENT,
    IdCharge INTEGER,
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateReleve Date,
    Montant float,
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien),
    foreign key (IdLocataire) references  Locataires(IdLocataire),
    foreign key (IdCharge) references Charges(IdCharge)
);

CREATE TABLE ChargesEntretien(
    IdChargeEntretien INTEGER PRIMARY KEY AUTOINCREMENT,
    IdCharge INTEGER,
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateReleve Date,
    Montant float,
    Pourcentage float check (Pourcentage between 0 and 100),
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien),
    foreign key (IdLocataire) references  Locataires(IdLocataire),
    foreign key (IdCharge) references Charges(IdCharge)
);

CREATE TABLE ArchivesLocataires(
    IDArchives INTEGER PRIMARY KEY autoincrement,
    Nom_Locataire varchar(20),
    Prenom_Locataire varchar(20),
    Date_Depart DATE,
    IdBien INTEGER,
    motifDepart varchar(100),
    montantSoldeCompte float,
    FOREIGN KEY (IdBien) REFERENCES BiensLouable(IdBien)
);

create table DeclarationsFiscales(
    IdDeclarationFiscale INTEGER primary key autoincrement,
    annee INTEGER,
    revenusImmobiliers float,
    fraisGestion float,
    montantTravaux float,
    cotisations float,
    IdBien INTEGER,
    FOREIGN KEY (IdBien) references BiensLouable(IdBien)
);

create table Documents (
    IdDocument INTEGER primary key autoincrement,
    TypeDocument varchar(30), --bail, etat des lieux, diagnostics, etc
    CheminFichier varchar(100),
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateAjout Date,
    FOREIGN KEY (IdBien) references BiensLouable(IdBien),
    foreign key (IdLocataire) references  Locataires(IdLocataire)
);

CREATE TABLE Quittancesloyers(
    IdQuittance INTEGER primary key autoincrement,
    IdLocataire INTEGER,
    IdBien INTEGER,
    MontantLoyer float,
    MontantCharges float,
    datePaiement Date,
    CheminFichier varchar(100),
    foreign key (IdLocataire) references Locataires(IdLocataire),
    foreign key (IdBien) references BiensLouable(IdBien)
);

CREATE TABLE TaxesFonciere(
    IdTaxeFonciere INTEGER primary key autoincrement,
    Montant float,
    Annee INTEGER,
    IdBien INTEGER,
    foreign key (IdBien) references  BiensLouable(IdBien)
);

CREATE TABLE Colocations (
    IdColocation INTEGER primary key autoincrement,
    IdBail INTEGER,
    DateArrivee Date,
    DateDepart Date,
    quotiteLoyer float,
    foreign key (IdBail) references Bail(IdBail)
);

CREATE TABLE Cohabiter(
    IdColocation INTEGER,
    IdColotaire INTEGER,
    DateDebut Date,
    DateFin Date,
    PRIMARY KEY (IdColocation, IdColotaire),
    FOREIGN KEY (IdColocation) REFERENCES Colocations(IdColocation),
    FOREIGN KEY (IdColotaire) REFERENCES Locataires(IdLocataire)
);

CREATE TABLE Paiements (
    IdPaiement INTEGER primary key autoincrement ,
    IdBail INTEGER,
    IdLocataire INTEGER,
    Montant float,
    DatePaiement Date,
    TypePaiement varchar(20) CHECK ( TypePaiement IN ('Chèque', 'Espèce', 'Virement') ), --cheque, espèce, virement
    Statut varchar(10) CHECK ( Statut IN ('Validé','En Attente', 'Refusé') ),
    foreign key (IdBail) references Bail(IdBail),
    foreign key (IdLocataire) references Locataires(IdLocataire)
);

CREATE TABLE RepartitionCharges(
    IdRepartition INTEGER primary key autoincrement,
    IdCharge INTEGER,
    IdLocataire INTEGER,
    Pourcentage float check(Pourcentage between 0 and 100),
    Montant float,
    foreign key (IdCharge) references Charges(IdCharge),
    foreign key (IdLocataire) references Locataires(IdLocataire)
);