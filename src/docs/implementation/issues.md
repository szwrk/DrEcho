
# Run
- normal
mvn clean javafx:run
- with asserions
with assserts: mvn clean javafx:run -DargLine="-ea"
- 
# Diary—Issues
## Issues
### java.lang.NoSuchMethodException
Each JavaFX controller class requires an empty constructor.

### Issue: Complex view nested VM is null
Ensure that each new nested view is registered. Refer to the documentation of the QuickVisitController class. Utilize the View initializer to load the view model instance.

### IllegalAccessError exception on start
Caused by: java.lang.IllegalAccessError: class com.sun.javafx.fxml.FXMLLoaderHelper (in unnamed module @0x3737a69b) cannot access class com.sun.javafx.util.Utils (in module javafx.graphics) because module javafx.graphics does not export com.sun.javafx.u
Solution:
Add Run configuratiom
--module-path "C:\your\path\javafx-sdk-12.0.1\lib" --add-modules javafx.controls,javafx.fxml
