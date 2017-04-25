package me.tongfei.progressbar;

import org.junit.Test;

/**
 * @author bwittwer
 */
public class Issue13Test {

	private static final int NBR_ELEMENTS = 100;
	private static final int PROGRESSBAR_GRACE_PERIOD = 1000;

	@Test
	public void testOk() {
		ProgressBar pb = new ProgressBar("Test", NBR_ELEMENTS);
		pb.start();

		try {
			Thread.sleep(PROGRESSBAR_GRACE_PERIOD);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < 100; i++) {
			pb.step();
		}

		pb.stop();
	}

	@Test
	public void testKo() {
		ProgressBar pb = new ProgressBar("Test", NBR_ELEMENTS);
		pb.start();

		for (int i = 0; i < 100; i++) {
			pb.step();
		}

		pb.stop();
	}

}
