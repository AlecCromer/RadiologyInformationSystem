This README describes the layout and descriptions of the RIS (Radiology Information System) 
and its implementations in order of its creation to better understand the system and its design.

--------------------------------------------------------------------------------------------

1. Requirements Document (Requirements.docx): This document describes the requirements created 
by the developers with the client. All design decisions thereafter will be reflected from these
requirements.

2. Traceability Document (Traceability4.docx): This document describes the traceability for
the corresponding created requirements, its progress in development, affected classes,
and picture of the system that was implemented.

3a. Scenarios (Use Case and Scenarios.zip -> Scenarios.pdf): These files layout the created 
use cases and scenarios by the development team. The scenarios depict annecdotes where the 
RIS would come in place and describe how each actor would use the system.

3b. The Use Cases (Use Case and Scenarios.zip -> UseCase_v.3.PNG, UseCaseRelation_v.1.PNG, .mdj):
Depict actors' relationships and corresponding actions that would include the RIS. These files 
also describe the general workflow from Actors, their actions, and those actions' relative packages.

4. Class Diagrams (Class diagram Main.png, Rest of class diagrams package.zip): This Diagram depicts 
the entirety of the system's packages and their relations to each other, including the Models, Views
and Controllers. The .zip includes early primative diagrams (.zip -> Actual Class Diagram) created
by the developers in the design process. The generated diagrams (.zip -> Auto-Generated Class Diagrams)
include the final class diagrams produced by the final system and are concise version of the original 
Class Diagram Main.PNG file for reference.

5. Database Diagram (RIS phpMyAdmin export diagram and dict.pdf): This document contains the 
description of each table, its columns, type accepted, and other descriptors for the database.
At the end of this document is the relational schematic, depicting primary and foreign keys and general
table relation.

6. Sequence Diagrams (Sequence Diagrams.zip): This .zip contains the Sequence Diagrams for each view
depicted in the system and the archives for their previous versions. These diagrams depict interactions
between objects in a sequential order. Note: The Greek letter Lambda denotes Lambda method calls
throughout each of the diagrams.

7. Testing Documents (RISFX Testing.xlsx): Contains the testing methods for the implemented system,
its corresponding requirement, account / actor affected, pre-requisite condition, actual/expected result
and whether the final system has passed or failed this test. The Requirements listed in order
corresponds to the submitted requirements document (Requirements.docx). All, if any, failed tests
include a suggestion to remedy any error caused by the system.

8. Installation of the System (Installation.docx): This document contains a walkthrough of
installing the RIS and any required files and programs. Any external links to outside programs are
provided in this document. The included database contains testing accounts and are described here.

9. Java Documentations (Javadoc folder -> index.html): This documentation contains the descriptions,
parameters, and throws exceptions for every class and method utilized by the RIS. Class and Method
hierarchies are also described here and are sorted by Package. Any inhereted methods from any class 
are also described and depict which parent it calls from.

10. Git Pull (RadiologyInformationSystem folder): This contains the entire clone of the git repository
as of 4/26/2019, and can be updated at https://github.com/AlecCromer/RadiologyInformationSystem/
This folder contains all of the above documents, source code, prototypes, user manual and 
research documents. Each of these are divided into folders for ease of access.
