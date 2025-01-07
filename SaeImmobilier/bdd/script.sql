DROP DATABASE IF EXISTS bdImmo;

CREATE DATABASE IF NOT EXISTS bdImmo;

Use bdImmo;

CREATE TABLE Locataire(
                          IdLocataire INT auto_increment,
                          Nom VARCHAR(50),
                          Prenom VARCHAR(50),
                          Sexe CHAR(1),
                          Telephone CHAR(10),
                          Email VARCHAR(50),
                          DateDepart DATE,
                          MotifDepart VARCHAR(50),
                          MontantSoldeCompte DOUBLE,
                          DateNaissance DATE,
                          LieuNaissance VARCHAR(50),
                          SituationFamiliale VARCHAR(50),
                          Employeur VARCHAR(50),
                          Profession VARCHAR(50),
                          TypeContrat VARCHAR(4),
                          RemunerationMensuelle DOUBLE,
                          AutresRevenus DOUBLE,
                          Archive BOOLEAN,
                          TotalRevenus DOUBLE,
                          PRIMARY KEY(IdLocataire)
);

CREATE TABLE Travaux(
                        IdTravaux INT auto_increment,
                        NumeroFacture VARCHAR(50),
                        Cote VARCHAR(50),
                        Entreprise VARCHAR(50),
                        Montant DOUBLE,
                        MontantNonDeductible DOUBLE,
                        MontantADeclarer DOUBLE,
                        Reduction DOUBLE,
                        DateTravaux DATE,
                        Nature VARCHAR(50),
                        PRIMARY KEY(IdTravaux)
);
CREATE TABLE Assurance(
                          IdAssurance INT auto_increment,
                          NumeroContrat VARCHAR(50) UNIQUE NOT NULL,
                          ProtectionJuridique DOUBLE,
                          Prime DOUBLE,
                          TypeContrat VARCHAR(20),
                          Annee INT,
                          IdBail INTEGER,
                          PRIMARY KEY(IdAssurance)
);

ALTER TABLE Assurance
    ADD CONSTRAINT FOREIGN KEY (IdBail) REFERENCES Bail(IdBail);

CREATE TABLE Proprietaire(
                             IdProprietaire INT auto_increment,
                             Email VARCHAR(50),
                             MotDePasse VARCHAR(50),
                             PRIMARY KEY(IdProprietaire)
);

CREATE TABLE Bien(
                     IdBien INT auto_increment,
                     ComplementAdresse VARCHAR(50),
                     Adresse VARCHAR(50),
                     Ville VARCHAR(50),
                     CodePostal CHAR(5),
                     TypeBien VARCHAR(20),
                     Surface DOUBLE,
                     NombrePieces INT,
                     NumeroFiscal VARCHAR(50) UNIQUE,
                     DateAjout DATE,
                     IdAssurance INT default 0,
                     IdImmeuble INT default 0,
                     PRIMARY KEY(IdBien)
);

Alter table Bien
    Add constraint CK_Type_Bien
        CHECK ( Bien.TypeBien IN('HABITATION','GARAGE','IMMEUBLE') );
Alter table Assurance
    Add constraint CK_Type_Contrat
        CHECK ( Assurance.TypeContrat IN('PROPRIETAIRE','AIDE_JURIDIQUE') );
DELIMITER //
CREATE TRIGGER CHECK_IDIMMEUBLE_NON_IMMEUBLE
    BEFORE INSERT ON Bien
    FOR EACH ROW
BEGIN
    DECLARE v_type VARCHAR(20);
    IF NEW.TypeBien != 'IMMEUBLE' THEN
        IF NEW.IdImmeuble IS NULL THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Le bien n'' étant pas un immeuble doit référencer un immeuble';
        END IF;
    END IF;
    SELECT TypeBien INTO v_type FROM Bien WHERE IdBien = NEW.IdImmeuble;
    IF v_type != 'IMMEUBLE' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Le type de bien doit être un immeuble';
    END IF;
END;
//
DELIMITER ;

DELIMITER //
CREATE TRIGGER CHECK_TYPE_BIEN_
    BEFORE INSERT ON Bien
    FOR EACH ROW
BEGIN
    DECLARE v_type VARCHAR(20);
    SELECT TypeBien INTO v_type FROM Bien WHERE IdBien = NEW.IdImmeuble;
    IF v_type != 'IMMEUBLE' THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Le type de bien doit être un immeuble';
    END IF;
END;
//
DELIMITER ;


CREATE TABLE Bail(
                     IdBail INT auto_increment,
                     NbMoisLoues INT,
                     DateDebut DATE,
                     DateFin DATE,
                     TotalCharges DOUBLE,
                     DepotGaranti DOUBLE,
                     MontantLoyer DOUBLE,
                     Archive BOOLEAN,
                     TypeBail VARCHAR(50),
                     Renouvelable BOOLEAN,
                     CheminDocument VARCHAR(50),
                     DateSignature DATE,
                     Etat VARCHAR(50),
                     RepartitionEntretien DOUBLE,
                     QuotiteLoyer DOUBLE,
                     RepartitionElectricite VARCHAR(50),
                     RepartitionOrdures_Menageres VARCHAR(50),
                     IdBien INT NOT NULL,
                     PRIMARY KEY(IdBail),
                     FOREIGN KEY(IdBien) REFERENCES Bien(IdBien)
);

CREATE TABLE Charges(
                        IdCharges INT auto_increment,
                        Montant DOUBLE,
                        DateCharge DATE,
                        TypeCharges VARCHAR(50),
                        IdBail INT NOT NULL,
                        PRIMARY KEY(IdCharges),
                        FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE ChargesEau(
                           IdChargesEau INT auto_increment,
                           NouvelIndice INT,
                           AncienIndice INT,
                           PartieFixe DOUBLE,
                           PartieVariable DOUBLE,
                           IdCharges INT NOT NULL,
                           PRIMARY KEY(IdChargesEau),
                           UNIQUE(IdCharges),
                           FOREIGN KEY(IdCharges) REFERENCES Charges(IdCharges)
);

CREATE TABLE Paiement(
                         IdPaiement INT auto_increment,
                         Montant DOUBLE,
                         DatePaiement DATE,
                         TypePaiement VARCHAR(50),
                         Statut VARCHAR(50),
                         IdBail INT NOT NULL,
                         PRIMARY KEY(IdPaiement),
                         FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE Document(
                         IdDocument INT auto_increment,
                         TypeDocument VARCHAR(50),
                         CheminFichier VARCHAR(50),
                         DateAjout DATE,
                         IdBail INT NOT NULL,
                         PRIMARY KEY(IdDocument),
                         FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE ChargesEntretien(
                                 IdChargesEntretien INT auto_increment,
                                 Pourcentage DOUBLE,
                                 IdCharges INT NOT NULL,
                                 PRIMARY KEY(IdChargesEntretien),
                                 UNIQUE(IdCharges),
                                 FOREIGN KEY(IdCharges) REFERENCES Charges(IdCharges)
);

CREATE TABLE DeclarationFiscale(
                                   IdDeclarationFiscale INT auto_increment,
                                   Annee INT,
                                   RevenusImmobiliers DOUBLE,
                                   FraisGestion DOUBLE,
                                   MontantTravaux DOUBLE,
                                   Cotisations DOUBLE,
                                   IdDocument INT NOT NULL,
                                   PRIMARY KEY(IdDeclarationFiscale),
                                   UNIQUE(IdDocument),
                                   FOREIGN KEY(IdDocument) REFERENCES Document(IdDocument)
);

CREATE TABLE EtatDesLieux(
                             IdEtatDesLieux INT auto_increment,
                             Date_Signature DATE,
                             ElementEDL VARCHAR(50),
                             EtatElement VARCHAR(50),
                             IdDocument INT NOT NULL,
                             PRIMARY KEY(IdEtatDesLieux),
                             UNIQUE(IdDocument),
                             FOREIGN KEY(IdDocument) REFERENCES Document(IdDocument)
);

CREATE TABLE QuittanceLoyer(
                               IdQuittanceLoyer INT auto_increment,
                               MontantLoyer DOUBLE,
                               MontantCharges DOUBLE,
                               DatePaiement DATE,
                               CheminFichier VARCHAR(50),
                               IdDocument INT NOT NULL,
                               PRIMARY KEY(IdQuittanceLoyer),
                               UNIQUE(IdDocument),
                               FOREIGN KEY(IdDocument) REFERENCES Document(IdDocument)
);

CREATE TABLE TaxesFoncieres(
                               IdTaxesFoncieres INT auto_increment,
                               Montant DOUBLE,
                               Annee INT,
                               IdDocument INT NOT NULL,
                               PRIMARY KEY(IdTaxesFoncieres),
                               UNIQUE(IdDocument),
                               FOREIGN KEY(IdDocument) REFERENCES Document(IdDocument)
);

CREATE TABLE AssocieBailLocataire(
                                     IdLocataire INT,
                                     IdBail INT,
                                     DateEntree DATE,
                                     DateSortie DATE,
                                     PRIMARY KEY(IdLocataire, IdBail),
                                     FOREIGN KEY(IdLocataire) REFERENCES Locataire(IdLocataire),
                                     FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE Realiser(
                         IdTravaux INT,
                         IdBien INT,
                         PRIMARY KEY(IdTravaux, IdBien),
                         FOREIGN KEY(IdTravaux) REFERENCES Travaux(IdTravaux),
                         FOREIGN KEY(IdBien) REFERENCES Bien(IdBien)
);

-- Trigger pour calculer TotalCharges dans la table Bail
DELIMITER //
CREATE TRIGGER CalculTotalCharges
    AFTER INSERT ON Bail
    FOR EACH ROW
BEGIN
    UPDATE Bail
    SET TotalCharges = (
        SELECT IFNULL(SUM(Montant), 0)
        FROM Charges
        WHERE Charges.IdBail = NEW.IdBail
    )
    WHERE Bail.IdBail = NEW.IdBail;
END;
//
DELIMITER ;


DELIMITER //
CREATE TRIGGER CalculMontantADeclarer
    AFTER INSERT ON Travaux
    FOR EACH ROW
BEGIN
    UPDATE Travaux
    SET MontantADeclarer = (NEW.Montant - NEW.MontantNonDeductible) * (1 - NEW.Reduction)
    WHERE Travaux.IdTravaux = NEW.IdTravaux;
END;
//
DELIMITER ;






