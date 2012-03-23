package kieker.test.analysis.junit.plugin;

import java.util.List;

import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.analysis.repository.annotation.Repository;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionFilter.TimestampFilter;
import kieker.tools.traceAnalysis.filter.executionFilter.TraceIdFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

import org.junit.Assert;
import org.junit.Test;

/**
 * A simple test for the plugins in general. It tests for example if the
 * chaining of different plugins does work.
 * 
 * @author Nils Christian Ehmke
 */
public class GeneralPluginTest extends TestCase {

	private OperationExecutionRecord createOperationExecutionRecord(final String opString, final long traceId, final long tin, final long tout) {
		return new OperationExecutionRecord("", OperationExecutionRecord.NO_SESSION_ID, traceId, tin, tout, OperationExecutionRecord.NO_HOSTNAME,
				OperationExecutionRecord.NO_EOI_ESS, OperationExecutionRecord.NO_EOI_ESS);
	}

	@Test
	public void testPluginAttributes() {
		final Configuration myPluginConfig = new Configuration();
		// Set a name in order to test the getName() function below */
		final String myPluginName = "name-ieuIyxLG";
		myPluginConfig.setProperty(AbstractPlugin.CONFIG_NAME, myPluginName);
		final MyPlugin sourcePlugin = new MyPlugin(myPluginConfig);
		Assert.assertEquals("Unexpected plugin name", myPluginName, sourcePlugin.getName());

		/* Test if name and description from the annotation are returned correctly */
		Assert.assertEquals("Unexpected plugin type name", MyPlugin.PLUGIN_NAME, sourcePlugin.getPluginName());
		Assert.assertEquals("Unexpected plugin description", MyPlugin.PLUGIN_DESCRIPTION, sourcePlugin.getPluginDescription());
	}

	@Test
	public void testRepository() {
		final Configuration myRepoConfig = new Configuration();
		// Set a name in order to test the getName() function below */
		final String myRepoName = "name-22db22rLQ";
		myRepoConfig.setProperty(AbstractRepository.CONFIG_NAME, myRepoName);
		final MyRepository myRepo = new MyRepository(myRepoConfig);
		Assert.assertEquals("Unexpected repository name", myRepoName, myRepo.getName());

		/* Test if name and description from the annotation are returned correctly */
		Assert.assertEquals("Unexpected repository type name", MyRepository.REPOSITORY_NAME, myRepo.getRepositoryName());
		Assert.assertEquals("Unexpected repository description", MyRepository.REPOSITORY_DESCRIPTION, myRepo.getRepositoryDescription());
	}

	@Test
	public void testChaining() {
		final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());

		final ExecutionRecordTransformationFilter transformer = new ExecutionRecordTransformationFilter(new Configuration());

		final Configuration filter1byTraceIDConfig = new Configuration();
		filter1byTraceIDConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECT_ALL_TRACES, Boolean.FALSE.toString());
		filter1byTraceIDConfig.setProperty(TraceIdFilter.CONFIG_PROPERTY_NAME_SELECTED_TRACES, Configuration.toProperty(new Long[] { 1l }));
		final TraceIdFilter filter1byTraceID = new TraceIdFilter(filter1byTraceIDConfig);

		final Configuration filter2ByTimestampConfiguration = new Configuration();
		filter2ByTimestampConfiguration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_BEFORE_TIMESTAMP, Long.toString(10l));
		filter2ByTimestampConfiguration.setProperty(TimestampFilter.CONFIG_PROPERTY_NAME_IGNORE_AFTER_TIMESTAMP, Long.toString(20l));
		final TimestampFilter filter2ByTimestamp = new TimestampFilter(filter2ByTimestampConfiguration);

		/* The records we will send. */
		final OperationExecutionRecord opExRec1 = this.createOperationExecutionRecord("", 1, 14, 15);
		final OperationExecutionRecord opExRec2 = this.createOperationExecutionRecord("", 2, 14, 15);
		final OperationExecutionRecord opExRec3 = this.createOperationExecutionRecord("", 1, 9, 15);
		final OperationExecutionRecord opExRec4 = this.createOperationExecutionRecord("", 1, 11, 21);
		final OperationExecutionRecord opExRec5 = this.createOperationExecutionRecord("", 2, 9, 15);
		final OperationExecutionRecord opExRec6 = this.createOperationExecutionRecord("", 2, 14, 21);
		final OperationExecutionRecord opExRec7 = this.createOperationExecutionRecord("", 1, 9, 21);
		final OperationExecutionRecord opExRec8 = this.createOperationExecutionRecord("", 1, 10, 20);
		final SourceClass src = new SourceClass(opExRec1, opExRec2, opExRec3, opExRec4, opExRec5, opExRec6, opExRec7, opExRec8);
		final ExecutionSinkClass dst = new ExecutionSinkClass(new Configuration());

		final AnalysisController controller = new AnalysisController();
		controller.registerReader(src);
		controller.registerFilter(transformer);
		controller.registerFilter(transformer);
		controller.registerFilter(filter1byTraceID);
		controller.registerFilter(filter2ByTimestamp);
		controller.registerFilter(dst);

		/* Connect the plugins. */
		Assert.assertTrue(controller.connect(src, SourceClass.OUTPUT_PORT_NAME, transformer, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS));
		Assert.assertTrue(controller.connect(transformer, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, systemModelRepository));
		Assert.assertTrue(controller.connect(transformer, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS, filter1byTraceID, TraceIdFilter.INPUT_PORT_NAME_EXECUTION));
		Assert.assertTrue(controller.connect(filter1byTraceID, TraceIdFilter.OUTPUT_PORT_NAME_MATCH, filter2ByTimestamp, TimestampFilter.INPUT_PORT_NAME_EXECUTION));
		Assert.assertTrue(controller.connect(filter2ByTimestamp, TimestampFilter.OUTPUT_PORT_NAME_WITHIN_PERIOD, dst, ExecutionSinkClass.INPUT_PORT_NAME));

		src.read();

		final List<Execution> execs = dst.getExecutions();
		Assert.assertEquals(2, execs.size());

		boolean okay1 = false, okay2 = false;

		for (final Execution ex : execs) {
			if ((ex.getTraceId() == 1) && (ex.getTin() == 14) && (ex.getTout() == 15)) {
				okay1 = true;
			} else {
				if ((ex.getTraceId() == 1) && (ex.getTin() == 10) && (ex.getTout() == 20)) {
					okay2 = true;
				}
			}
		}

		Assert.assertTrue(okay1);
		Assert.assertTrue(okay2);
	}
}

/**
 * This is just a helper class used for testing the plugin structure. It should not be used outside this test class.
 * 
 * @author Nils Christian Ehmke
 * @version 1.0
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = SourceClass.OUTPUT_PORT_NAME, eventTypes = { OperationExecutionRecord.class })
		})
class SourceClass extends AbstractReaderPlugin {

	/**
	 * The array containing the records to be delivered.
	 */
	private final OperationExecutionRecord records[];
	/**
	 * The name of the default output port.
	 */
	public static final String OUTPUT_PORT_NAME = "output";

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param records
	 *            The records to be delivered via the output port.
	 */
	public SourceClass(final OperationExecutionRecord... records) {
		super(new Configuration());
		this.records = records;
	}

	public boolean read() {
		for (final OperationExecutionRecord record : this.records) {
			Assert.assertTrue(super.deliver(SourceClass.OUTPUT_PORT_NAME, record));
		}
		return true;
	}

	public void terminate(final boolean error) {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}

@Plugin(
		name = MyPlugin.PLUGIN_NAME,
		description = MyPlugin.PLUGIN_DESCRIPTION)
class MyPlugin extends AbstractPlugin {
	public static final String PLUGIN_NAME = "pluginName-EfpvPSE0";

	public static final String PLUGIN_DESCRIPTION = "pluginDescription-TB5UV1LdSz";

	public MyPlugin(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

}

@Repository(
		name = MyRepository.REPOSITORY_NAME,
		description = MyRepository.REPOSITORY_DESCRIPTION)
class MyRepository extends AbstractRepository {

	public static final String REPOSITORY_NAME = "repoName-hNcuzIKc8e";

	public static final String REPOSITORY_DESCRIPTION = "repoDescription-DEYmVN6sEp";

	public MyRepository(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
