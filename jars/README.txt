How to generate JAR files for Grants.gov forms and other useful things

To generate a JAR file for Grants.gov forms:
In Eclipse, select the files to be included in the JAR.
Right-click and select Export.
Under the Java node, select JAR file.  Click Next.
The files are already selected.  
Only select the checkbox to export class files and resources - uncheck the checkbox that includes source files.
In the Browse box, select the WEB-INF/lib directory.
Select the checkboxes for "compress" and "overwrite".  Click Next.
Select both "export" options, "build", and "save description".
In the Browse box, select the project_name/jars directory.  Click Next.

For New jars, select "Generate the manifest" and select both "save" options.
In the Browse box, select the project_name/jars directory.

If Regenerating the jar, select "use existing manifest" and select the appropriate mf file.

Click Finish to compile and generate.  
The JAR file will be generated in the WEB-INF/lib directory.
The JAR description file and manifest files will be generated in the project_name/jars directory.

Make sure to exclude the gov.grants.apply.forms directories associated with the form
from the WAR file build.

To rebuild the JAR file when changes are made, right-click the jardesc file and 
select Create Jar.


To sign the JAR file:

c:\>c:\java\jdk\bin\jarsigner.exe -keystore c:\Coeus451local\keystore\coeus102013.keystore 
c:\Coeus451local\Source\Server\web\WEB-INF\lib\cumulative_inclusion_report_V1_0.jar coeus.keystore


To verify the JAR file:

c:\>c:\java\jdk\bin\jarsigner.exe -verify -verbose -certs 
c:\Coeus451local\Source\Server\web\WEB-INF\lib\cumulative_inclusion_report_V1_0.jar


Password is "cupcake".