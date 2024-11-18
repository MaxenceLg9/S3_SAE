CREATE TABLE BiensImmobiliers (
    IdBien INTEGER PRIMARY KEY,
    Adresse varchar(30),
    TypeBien varchar(20) CHECK (TypeBien IN ('Bâtiment', 'Logement', 'Garage')),
    Surface float,
    NombrePieces INTEGER,
    NumeroFiscal varchar(20),
    IdentifiantAdministratif varchar(20),
    CONSTRAINT TypeBien_Specification CHECK (
    (TypeBien = 'Bâtiment' AND Surface IS NULL AND NombrePieces IS NULL) OR
    (TypeBien != 'Bâtiment' AND Surface IS NOT NULL AND NombrePieces IS NOT NULL)
    )
);

CREATE TABLE Bail(
    IdBail INTEGER primary key,
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateDebut Date,
    DateFin Date,
    MontantLoyer float,
    MontantCharges float,
    DepotGaranti float,
    TypeBail varchar(20), -- non meublé, meublé, colocation
    Renouvelable Boolean,
    CheminDocument varchar(100),
    DateSignature Date,
    Etat varchar check ( Etat in ('Actif', 'Terminé', 'Preavis') ),
    foreign key (IdBien) references BiensImmobiliers(IdBien),
    foreign key (IdLocataire) references Locataires(IdLocataire)
);

CREATE TABLE Locataires (
    IdLocataire INTEGER PRIMARY KEY,
    Nom varchar(20),
    Prenom varchar(20),
    Genre varchar(20) CHECK (Genre IN ('Madame', 'Monsieur')),
    Telephone varchar(10),
    Email varchar(20),
    IsColocataire BOOLEAN
);


CREATE TABLE Charges (
    IdCharge INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    TypeCharge varchar(20) CHECK (TypeCharge IN ('Ordure Ménagères', 'Entretien', 'Eau', 'Electricité', 'Autre')),
    Montant float,
    DateCharge DATE,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien)
);

CREATE TABLE ConsommationEau (
    IdConsommationEau INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    IdLocataire INTEGER,
    Mois varchar(10),
    NouvelIndice float,
    AncienIndice float,
    PartieFixe float,
    PartieVariable float,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien),
    foreign key (IdLocataire) references Locataires(IdLocataire)
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
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien)
);

CREATE TRIGGER CalculateMontantADeclarer
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
    LieuNaissance varchar(20),
    Domicile varchar(40),
    Email varchar(30),
    SituationFamiliale varchar(20),
    Profession varchar(20),
    Employeur varchar(30),
    TypeContrat varchar(3) CHECK (TypeContrat IN ('CDI', 'CDD')),
    DateFinContrat DATE,
    RemunerationMensuelle float,
    AutresRevenus float,
    TotalRevenus float
);

CREATE TRIGGER CalculateTotalRevenus
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
    Prime float,
    AugmentationAnnuelle float,
    TotalPrime float,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien)
);

CREATE TRIGGER CalculateTotalPrime
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
    Date_Dignature Date,
    Nom_Bailleur  varchar(20),
    Prenom_Bailleur varchar(20),
    Element varchar(20),
    Etat_Element varchar(20) check ( Etat_Element in ('Bon état', 'Etat d''usage', 'neuf', 'Mauvais état') )
);

Create table ConsommationsElectricite
(
    IdConsommationElec INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien             INTEGER,
    IdLocataire INTEGER,
    DateReleve         DATE,
    NouvelIndice       float,
    AncienIndice       float,
    PartieFixe         float,
    PartieVariable     float,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers (IdBien),
    foreign key (IdLocataire) references  Locataires(IdLocataire)
);

Create table ConsommationsGaz (
    IdConsommationGaz INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateReleve DATE,
    NouvelIndice float,
    AncienIndice float,
    PartieFixe float,
    PartieVariable float,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien),
    foreign key (IdLocataire) references  Locataires(IdLocataire)
);

CREATE TABLE ArchivesLocataires(
    IDArchives INTEGER PRIMARY KEY autoincrement,
    Nom_Locataire varchar(20),
    Prenom_Locataire varchar(20),
    Date_Depart DATE,
    IdBien INTEGER,
    motifDepart varchar(100),
    montantSoldeCompte float,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien)
);

create table DeclarationsFiscales(
    IdDeclarationFiscale INTEGER primary key autoincrement,
    annee INTEGER,
    revenusImmobiliers float,
    fraisGestion float,
    montantTravaux float,
    cotisations float,
    IdBien INTEGER,
    FOREIGN KEY (IdBien) references BiensImmobiliers(IdBien)
);

create table Documents (
    IdDocument INTEGER primary key autoincrement,
    TypeDocument varchar(30), --bail, etat des lieux, diagnostics, etc
    CheminFichier varchar(100),
    IdBien INTEGER,
    IdLocataire INTEGER,
    DateAjout Date,
    FOREIGN KEY (IdBien) references BiensImmobiliers(IdBien),
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
    foreign key (IdBien) references BiensImmobiliers(IdBien)
);

CREATE TABLE Taxes(
    IdTaxe INTEGER primary key autoincrement,
    TypeTaxe varchar(40), --ordures ménagères, taxe foncières, etc
    Montant float,
    Annee INTEGER,
    IdBien INTEGER,
    foreign key (IdBien) references  BiensImmobiliers(IdBien)
);

CREATE TABLE Colocations (
    IdColocation INTEGER primary key autoincrement,
    IdBail INTEGER,
    IdLocataire INTEGER,
    DateArrivee Date,
    DateDepart Date,
    quotiteLoyer float,
    foreign key (IdBail) references Bail(IdBail),
    foreign key (IdLocataire) references Locataires(IdLocataire)
);

CREATE TABLE Paiements (
    IdPaiement INTEGER primary key autoincrement ,
    IdBail INTEGER,
    IdLocataire INTEGER,
    Montant float,
    DatePaiement Date,
    TypePaiement varchar(20) CHECK ( TypePaiement IN ('Chèque', 'Espèce', 'Virement') ), --cheque, espèce, virement
    MoisConcerné varchar(10),
    Statut varchar(10) CHECK ( Statut IN ('Validé','En Attente', 'Refusé') ),
    foreign key (IdBail) references Bail(IdBail),
    foreign key (IdLocataire) references Locataires(IdLocataire)
);