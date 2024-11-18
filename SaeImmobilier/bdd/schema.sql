CREATE TABLE BiensImmobiliers (
    IdBien integer PRIMARY KEY,
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

CREATE TABLE Locataires (
    IdLocataire integer PRIMARY KEY,
    Nom varchar(20),
    Prenom varchar(20),
    Genre varchar(20) CHECK (Genre IN ('Madame', 'Monsieur')),
    AdresseContact varchar(40),
    CodePostal varchar(10),
    Ville varchar(20),
    Telephone varchar(10),
    Email varchar(20),
    DateEntree DATE,
    DateSortie DATE,
    IsColocataire INTEGER DEFAULT 0 CHECK (IsColocataire IN (0, 1))
);


CREATE TABLE Charges (
    IdCharge INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    TypeCharge varchar(20) CHECK (TypeCharge IN ('Ordure Menagere', 'Entretien', 'Eau', 'Electricité', 'Autre')),
    Montant float,
    DateCharge DATE,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien)
);

CREATE TABLE ConsommationEau (
    IdConsommation INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    Mois varchar(10),
    NouvelIndice float,
    AncienIndice float,
    PartieFixe float,
    PartieVariable float,
    FOREIGN KEY (IdBien) REFERENCES BiensImmobiliers(IdBien)
);

CREATE TABLE Travaux (
    IdTravaux INTEGER PRIMARY KEY AUTOINCREMENT,
    IdBien INTEGER,
    NumeroFacture integer,
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
     PRIMARY KEY (IDBail, IDLocataire),
     FOREIGN KEY (IDBail) REFERENCES Bail(id_bail),
     FOREIGN KEY (IDLocataire) REFERENCES Locataire(id_Locataire)
);

CREATE TABLE Etat_des_Lieux (
    Date_Dignature Date,
    Nom_Bailleur  varchar(20),
    Prenom_Bailleur varchar(20),
    Element varchar(20),
    Etat_Element varchar(20)
)