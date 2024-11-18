CREATE TABLE Locataire(
    id_Locataire INTEGER not null primary key,
    genre varchar(10),
    téléphone varchar(10),
    email varchar(30),
    nom varchar(20),
    prenom varchar(20)
);

CREATE TABLE Bail(
    id_bail INTEGER not null primary key,
    nbMoisLoues INTEGER,
    ProvisionSurCharge real,
    FactureEau real,
    TotalCharge real,
    Loyer real,
    RegularisationCharge real,
    DateDebut Date,
    DateFin Date
);

CREATE TABLE AssocieBailLocataire (
     id_bail INTEGER,
     id_Locataire INTEGER,
     PRIMARY KEY (id_bail, id_Locataire),
     FOREIGN KEY (id_bail) REFERENCES Bail(id_bail),
     FOREIGN KEY (id_Locataire) REFERENCES Locataire(id_Locataire)
);

CREATE TABLE Etat_des_Lieux (
    Date_Dignature Date,
    Nom_Bailleur  varchar(20),
    Prenom_Bailleur varchar(20),
    Element varchar(20),
    Etat_Element varchar(20)
)