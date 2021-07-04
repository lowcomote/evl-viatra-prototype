# EOL to Viatra Mapping

This repository is built-on the top of Epsilon framework. It supports the following languages:

1. Epsilon Object Language (EOL)
2. Epsilon Validation Language (EVL)

## Getting started

1. Download the latest version of Eclipse and select the **Eclipse IDE for Eclipse Committers** option when prompted during the installation process.

2. Clone the Epsilon Git repository: **git://git.eclipse.org/gitroot/epsilon/org.eclipse.epsilon.git** (**master** branch).

3. Import the following projects into your workspace:

	- org.eclipse.epsilon.common
	- org.eclipse.epsilon.common.dt
	- org.eclipse.epsilon.emc.emf
	- org.eclipse.epsilon.emc.emf.dt
	- org.eclipse.epsilon.emc.emf.virtual
	- org.eclipse.epsilon.emf.dt
	- org.eclipse.epsilon.eol.dt
	- org.eclipse.epsilon.eol.engine
	- org.eclipse.epsilon.erl.dt
	- org.eclipse.epsilon.erl.engine
	- org.eclipse.epsilon.evl.dt
	- org.eclipse.epsilon.evl.emf.validation
	- org.eclipse.epsilon.evl.engine
	- org.eclipse.epsilon.profiling

4. Clone the Static analysis git repository: **https://github.com/epsilonlabs/static-analysis.git** (**master** branch)

5. Import the following projects into your workspace:

	- org.eclipse.epsilon.eol.staticanalyser
	- org.eclipse.epsilon.evl.staticanalyser

6. Clone this repository (**master** branch).

7. Import the following projects into your workspace:

	- org.eclipse.epsilon.eol.viatra
	- org.eclipse.epsilon.eol.viatra.target
	- org.eclipse.epsilon.eol.viatra.tests
	- org.eclipse.epsilon.eol.viatra.ui
	- org.eclipse.epsilon.evl.viatra
	- org.eclipse.epsilon.evl.viatra.standalone
	- org.eclipse.epsilon.evl.viatra.tests

8. Open **org.eclipse.epsilon.eol.viatra.target/org.eclipse.epsilon.viatra.target** and click the **Set as Active Target Platform** link on the top right.


## Try out in a runtime eclipse

1. Start a new runtime eclipse from the projects. (Click on one of the imported projects and select Run As -> Eclipse Application.)

2. Enable static analysis: Window menu -> Preferences -> Epsilon -> Enable static analysis (experimental)

3. Import **org.eclipse.epsilon.evl.viatra.demo** from this repository into the runtime eclipse.

4. In the project, right click on **java.ecore** and select **Register EPackages**.

5. In the project, right click on **findbugs.evl** and select Run As -> Run Configurations.

6. Create a new run configuration in the **EVL Validation** category.

7. On the **Source** pane browse the **findbugs.evl** file.

8. On the **Models** pane, click **Add..**, select **Viatra EMF model** (if not visible then select **Show all model types**) and then click OK.

9. In the *Configure Viatra EMF model* window:

    - set the name to **Java**,
    - remove the checkmark from **Cache model elements to improve execution time**, 
    - at model file browse the workspace and select **eclipseModel-0.1.xmi**, 
    - check that metamodels include the nsURI of the Java metamodel (http://www.eclipse.org/MoDisco/Java/0.2.incubation/java). If the Java metamodel is not included there, then click **Add file** and select **java.ecore**.

10. Click OK and then click Run.

11. Open the **Validation** view to see the validation results: click on the Window menu -> Show View -> Other... -> Other -> Validation.

## Run the unit tests

### EOL to Viatra

1. To test the EOL to Viatra mapping, run the tests in the **EolToViatraRewritingTests** and **EolToRunViatraIntegrationTests** classes as JUnit Plug-in tests.

2. To run the EOL and Viatra equivalence tests, run the tests in the **EolToViatraEquivalenceTests** class as JUnit Plug-in tests.

### EVL to Viatra

1. To run the EVL and Viatra equivalence tests, run the tests in the **EvlToViatraEquivalenceTests** and **EvlToViatraFindbugsEquivalenceTests** classes as JUnit Plug-in tests. *Warning:* these tests take a lot of time to complete, due to the large model size.

## Contributors

* Qurat ul ain Ali
* Benedek Horváth

## Acknowledgement

This research is supported by the Lowcomote Training Network, which has received funding from the European Union’s Horizon 2020 Research and Innovation Programme under the Marie Skłodowska- Curie grant agreement n° 813884. 
