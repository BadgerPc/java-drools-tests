package pl.speednet.drools.api;

import org.slf4j.Logger;

public class DroolsJBossLogger implements IDroolsLogger {

	private Logger log;

	public DroolsJBossLogger() {
	}

	public DroolsJBossLogger(Logger log) {
		this.log = log;
	}

	public void log(String message) {
		if (log != null) {
			log.info(message);
		}
	}
}
