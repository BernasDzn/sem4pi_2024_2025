package shodrone.bootstrap.bootstrapers;

import eapli.framework.actions.Action;
import shodrone.core.application.drone_model.AddDroneModelController;

public class DroneModelBootstrapper implements Action {

	String dsl1 = "DroneOne programming language version 0.86\r\n" + //
				"\r\n" + //
				"Types\r\n" + //
				"\r\n" + //
				"Position - (x, y, z), so that x, y and z are floating point numbers in meters\r\n" + //
				"Vector - (x, y, z), so that x, y and z are floating point numbers in meters\r\n" + //
				"LinearVelocity - floating point number in m/s\r\n" + //
				"AngularVelocity - floating point number in rad/s\r\n" + //
				"Distance - floating point number in m\r\n" + //
				"Time - integer number in ms\r\n" + //
				"\r\n" + //
				"Variables\r\n" + //
				"\r\n" + //
				"<type> <variable name> = <initial value>;\r\n" + //
				"Position aPosition = (0, 1, 1.5);\r\n" + //
				"Vector aDirection = (0,0,1)-(0,1,0);\r\n" + //
				"\r\n" + //
				"LinearVelocity aVelocity = 6.2;\r\n" + //
				"AngularVelocity rotVelocity = PI/10;\r\n" + //
				"\r\n" + //
				"Position arrayOfPositions =((0, 0, 0), (0, 0, 20), (0, 20, 20), (0, 30, 20), (-10, 50, 25));\r\n" + //
				"\r\n" + //
				"Instructions\r\n" + //
				"\r\n" + //
				"takeOff(<height>, <velocity>);\r\n" + //
				"land(<velocity>);\r\n" + //
				"move(<final position>, <velocity>);\r\n" + //
				"move(<vector>, <velocity>, <duration>);\r\n" + //
				"movePath(<array of positions>, <velocity>);\r\n" + //
				"hoover(<time>);\r\n" + //
				"\r\n" + //
				"lightsOn();\r\n" + //
				"lightsOff();\r\n" + //
				"blink(<period>);";

	@Override
    public boolean execute() {
        registerDroneModel("Phantom 3 Standard", dsl1);
        return true;
    }

	private void registerDroneModel(String modelName, String droneDSL) {
		try {
			new AddDroneModelController().addDroneModel(modelName, droneDSL);
			System.out.println("»»» " + modelName);
		} catch (final Exception e) {
			System.err.println("Error registering drone model: " + e.getMessage());
		}
	}
	
}
