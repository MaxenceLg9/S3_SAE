Use bdImmo;

CREATE TABLE Locataire(
                          Id_Locataire INT auto_increment,
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
                          TotalRevenus DOUBLE,
                          PRIMARY KEY(Id_Locataire)
);
CREATE TABLE ArchiverLocataire(
                          Id_Locataire INT auto_increment,
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
                          TotalRevenus DOUBLE,
                          PRIMARY KEY(Id_Locataire)
);
CREATE TABLE Travaux(
                        Id_Travaux INT auto_increment,
                        NumeroFacture VARCHAR(50),
                        Côté VARCHAR(50),
                        Entreprise VARCHAR(50),
                        Montant DOUBLE,
                        MontantNonDéductible DOUBLE,
                        MontantADeclarer DOUBLE,
                        Réduction DOUBLE,
                        DateTravaux DATE,
                        Nature VARCHAR(50),
                        PRIMARY KEY(Id_Travaux)
);

CREATE TABLE Assurance(
                          Id_Assurance INT auto_increment,
                          ProtectionJuridique DOUBLE,
                          QuotitéJuridique DOUBLE,
                          Prime DOUBLE,
                          AugmentationAnnuelle DOUBLE,
                          TotalPrime DOUBLE,
                          PRIMARY KEY(Id_Assurance)
);

CREATE TABLE Propriétaire(
                             Id_Propriétaire INT auto_increment,
                             Nom VARCHAR(50),
                             Prenom VARCHAR(50),
                             Telephone CHAR(10),
                             Email VARCHAR(50),
                             MotDePasse VARCHAR(50),
                             Ville VARCHAR(50),
                             CodePostal CHAR(5),
                             Adresse VARCHAR(50),
                             PRIMARY KEY(Id_Propriétaire)
);

CREATE TABLE Bien(
                            IdBien INT auto_increment,
                            Lieu_Immeuble VARCHAR(50),
                            Adresse VARCHAR(50),
                            Ville VARCHAR(50),
                            CodePostal CHAR(5),
                            TypeBien VARCHAR(20),
                            Surface DOUBLE,
                            NombrePieces INT,
                            NumeroFiscal VARCHAR(50),
                            DateAjout DATE,
                            Id_Assurance INT default 0,
                            Id_Propriétaire INT default 0,
                            Id_Immeuble INT default 0,
                            PRIMARY KEY(IdBien)
);
alter table bien
    add constraint bien_immeuble_idImmeuble_fk
        foreign key (Id_Immeuble) references immeuble (idImmeuble);

Alter table Bien
Add constraint check_type_bien
CHECK ( Bien.TypeBien IN('HABITATION','GARAGE','IMMEUBLE') );

CREATE TABLE ArchiverBien(
                     IdBien INT auto_increment,
                     Lieu_Immeuble VARCHAR(50),
                     Adresse VARCHAR(50),
                     Ville VARCHAR(50),
                     CodePostal CHAR(5),
                     TypeBien VARCHAR(20),
                     Surface DOUBLE,
                     NombrePieces INT,
                     NumeroFiscal VARCHAR(50),
                     DateAjout DATE,
                     Id_Assurance INT NOT NULL,
                     Id_Propriétaire INT NOT NULL,
                     PRIMARY KEY(IdBien),
                     FOREIGN KEY(Id_Assurance) REFERENCES Assurance(Id_Assurance),
                     FOREIGN KEY(Id_Propriétaire) REFERENCES Propriétaire(Id_Propriétaire)
);

Alter table ArchiverBien
Add constraint check_type_bienarchive
CHECK ( ArchiverBien.TypeBien IN('BienLouable','Immeuble') );
CREATE TABLE Bail(
                     IdBail INT auto_increment,
                     NbMoisLoues INT,
                     DateDebut DATE,
                     DateFin DATE,
                     TotalCharges DOUBLE,
                     DepotGaranti DOUBLE,
                     MontantLoyer DOUBLE,
                     TypeBail VARCHAR(50),
                     Renouvelable BOOLEAN,
                     CheminDocument VARCHAR(50),
                     DateSignature DATE,
                     Etat VARCHAR(50),
                     Repartition_Entretien DOUBLE,
                     QuotitéLoyer DOUBLE,
                     Repartition_Electricite VARCHAR(50),
                     Repartition_Ordures_Menageres VARCHAR(50),
                     IdBien INT NOT NULL,
                     PRIMARY KEY(IdBail),
                     FOREIGN KEY(IdBien) REFERENCES Bien(IdBien)
);

CREATE TABLE Charges(
                        Id_Charges INT auto_increment,
                        Montant DOUBLE,
                        DateCharge DATE,
                        TypeCharges VARCHAR(50),
                        IdBail INT NOT NULL,
                        PRIMARY KEY(Id_Charges),
                        FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE ChargesEau(
                           Id_ChargesEau INT auto_increment,
                           NouvelIndice INT,
                           AncienIndice INT,
                           PartieFixe DOUBLE,
                           PartieVariable DOUBLE,
                           Id_Charges INT NOT NULL,
                           PRIMARY KEY(Id_ChargesEau),
                           UNIQUE(Id_Charges),
                           FOREIGN KEY(Id_Charges) REFERENCES Charges(Id_Charges)
);

CREATE TABLE Paiement(
                         Id_Paiement INT auto_increment,
                         Montant DOUBLE,
                         DatePaiement DATE,
                         TypePaiement VARCHAR(50),
                         Statut VARCHAR(50),
                         IdBail INT NOT NULL,
                         PRIMARY KEY(Id_Paiement),
                         FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE Document(
                         Id_Document INT auto_increment,
                         TypeDocument VARCHAR(50),
                         CheminFichier VARCHAR(50),
                         DateAjout DATE,
                         IdBail INT NOT NULL,
                         PRIMARY KEY(Id_Document),
                         FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE ChargesEntretien(
                                 Id_ChargesEntretien INT auto_increment,
                                 Pourcentage DOUBLE,
                                 Id_Charges INT NOT NULL,
                                 PRIMARY KEY(Id_ChargesEntretien),
                                 UNIQUE(Id_Charges),
                                 FOREIGN KEY(Id_Charges) REFERENCES Charges(Id_Charges)
);

CREATE TABLE DeclarationFiscale(
                                   Id_DeclarationFiscale INT auto_increment,
                                   Annee INT,
                                   RevenusImmobiliers DOUBLE,
                                   FraisGestion DOUBLE,
                                   MontantTravaux DOUBLE,
                                   Cotisations DOUBLE,
                                   Id_Document INT NOT NULL,
                                   PRIMARY KEY(Id_DeclarationFiscale),
                                   UNIQUE(Id_Document),
                                   FOREIGN KEY(Id_Document) REFERENCES Document(Id_Document)
);

CREATE TABLE EtatDesLieux(
                             Id_EtatDesLieux INT auto_increment,
                             Date_Signature DATE,
                             ElementEDL VARCHAR(50),
                             Etat_Element VARCHAR(50),
                             Id_Document INT NOT NULL,
                             PRIMARY KEY(Id_EtatDesLieux),
                             UNIQUE(Id_Document),
                             FOREIGN KEY(Id_Document) REFERENCES Document(Id_Document)
);

CREATE TABLE QuittanceLoyer(
                               Id_QuittanceLoyer INT auto_increment,
                               MontantLoyer DOUBLE,
                               MontantCharges DOUBLE,
                               DatePaiement DATE,
                               CheminFichier VARCHAR(50),
                               Id_Document INT NOT NULL,
                               PRIMARY KEY(Id_QuittanceLoyer),
                               UNIQUE(Id_Document),
                               FOREIGN KEY(Id_Document) REFERENCES Document(Id_Document)
);

CREATE TABLE TaxesFoncieres(
                               Id_TaxesFoncieres INT auto_increment,
                               Montant DOUBLE,
                               Annee INT,
                               Id_Document INT NOT NULL,
                               PRIMARY KEY(Id_TaxesFoncieres),
                               UNIQUE(Id_Document),
                               FOREIGN KEY(Id_Document) REFERENCES Document(Id_Document)
);

CREATE TABLE AssocieBailLocataire(
                                     Id_Locataire INT,
                                     IdBail INT,
                                     DateEntree DATE,
                                     DateSortie DATE,
                                     PRIMARY KEY(Id_Locataire, IdBail),
                                     FOREIGN KEY(Id_Locataire) REFERENCES Locataire(Id_Locataire),
                                     FOREIGN KEY(IdBail) REFERENCES Bail(IdBail)
);

CREATE TABLE Louer(
                      Id_Locataire INT,
                      IdBien INT,
                      DateDébut DATE,
                      DateFin DATE,
                      PRIMARY KEY(Id_Locataire, IdBien),
                      FOREIGN KEY(Id_Locataire) REFERENCES Locataire(Id_Locataire),
                      FOREIGN KEY(IdBien) REFERENCES Bien(IdBien)
);

CREATE TABLE Réaliser(
                         Id_Travaux INT,
                         IdBien INT,
                         PRIMARY KEY(Id_Travaux, IdBien),
                         FOREIGN KEY(Id_Travaux) REFERENCES Travaux(Id_Travaux),
                         FOREIGN KEY(IdBien) REFERENCES Bien(IdBien)
                     );

CREATE TABLE Immeuble(
    idImmeuble INT auto_increment,
    Adresse VARCHAR(50),
    Ville VARCHAR(50),
    CodePostal CHAR(5),
    PRIMARY KEY (idImmeuble)
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

-- Trigger pour calculer MontantADeclarer dans la table Travaux
DELIMITER //
CREATE TRIGGER CalculMontantADeclarer
AFTER INSERT ON Travaux
FOR EACH ROW
BEGIN
    UPDATE Travaux
    SET MontantADeclarer = (NEW.Montant - NEW.MontantNonDéductible) * (1 - NEW.Réduction)
    WHERE Travaux.Id_Travaux = NEW.Id_Travaux;
END;
//
DELIMITER ;

-- Trigger pour calculer TotalPrime dans la table Assurance
DELIMITER //
/*CREATE TRIGGER CalculTotalPrime
AFTER INSERT ON Assurance
FOR EACH ROW
BEGIN
    UPDATE Assurance
    SET TotalPrime = NEW.ProtectionJuridique + NEW.Prime
    WHERE Assurance.Id_Assurance = NEW.Id_Assurance;
END;
  */

//
DELIMITER ;
