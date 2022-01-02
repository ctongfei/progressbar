package me.tongfei.progressbar;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author bwittwer
 */
@ExtendWith(StreamSetupExtension.class)
class Issue13Test {

	private static final int NBR_ELEMENTS = 100;
	private static final int PROGRESSBAR_GRACE_PERIOD = 1000;

	@Test
	void testOk() {
		try (ProgressBar pb = new ProgressBar("Test", NBR_ELEMENTS)) {

			try {
				Thread.sleep(PROGRESSBAR_GRACE_PERIOD);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

			for (int i = 0; i < 100; i++) {
				pb.step();
			}
		}
	}

	@Test
	void testKo() {
		try (ProgressBar pb = new ProgressBar("Test", NBR_ELEMENTS)) {

			for (int i = 0; i < 100; i++) {
				pb.step();
			}

		}
	}

}
