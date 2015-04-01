package test;

public class TestRunner {

	public static void main(String[] args) {
		String[] testClasses = new String[] { "test.SerializerTest",
				"test.ServerProxyTest",
				"test.PollerTest", "test.CanPlayCardTest",
				"test.MiscClientModelTest", "test.DevCardTest",
				"test.CommandDevCardTests", "test.CommandNonMoveTests",
				"test.CommandOtherMoveTests"};
		org.junit.runner.JUnitCore.main(testClasses);
	}
}
