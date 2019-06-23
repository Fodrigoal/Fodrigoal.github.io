/**
 * Project: A00979176Assignment2
 * File: Gis.java
 * Date: July 1, 2017
 */

package a00979176;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import a00979176.data.AllData;
import a00979176.database.Database;
import a00979176.ui.MainFrame;

/**
 * @author Rodrigo Silva, A00979176
 *
 */
public class Gis {

	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	static {
		configureLogging();
	}
	private static final Logger LOG = LogManager.getLogger();
	private Database db;
	public static final String DB_PROPERTIES_FILENAME = "db.properties";
	private Properties properties;

	public Gis(String[] args) throws ApplicationException {
		LOG.info("Created Gis");
	}

	public static void main(String[] args) {
		Instant startTime = Instant.now();
		LOG.info(startTime);

		try {
			Gis gis = new Gis(args);
			gis.run();
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}

		Instant endTime = Instant.now();
		LOG.info(endTime);
		LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
	}

	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			LOG.error(String.format(
					"WARNING! Can't find the log4j logging configuration file %s; using DefaultConfiguration for logging.",
					LOG4J_CONFIG_FILENAME));
			Configurator.initialize(new DefaultConfiguration());
		}
	}

	private void run() throws ApplicationException, FileNotFoundException {
		LOG.debug("run()");
		try {
			properties = new Properties();
			properties.load(new FileInputStream(DB_PROPERTIES_FILENAME));
			db = new Database(properties);
			AllData.loadData(db);
			createUI();
		} catch (IOException e) {
			LOG.debug(e.getMessage());
		}
	}

	public static void createUI() {
		LOG.debug("Creating the UI");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LOG.debug("Setting the Look & Feel");
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		});
	}
}
