/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.test.tools.util.filter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * This is just a simple helper class which collects {@link Execution}s.
 * 
 * @author Nils Christian Ehmke
 */
public class ExecutionSinkClass extends AbstractFilterPlugin {

	/**
	 * The name of the default input port.
	 */
	public static final String INPUT_PORT_NAME = "doJob";

	/**
	 * This list will contain the records this plugin received.
	 */
	private final CopyOnWriteArrayList<Execution> lst = new CopyOnWriteArrayList<Execution>();

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this plugin. It will not be used.
	 */
	public ExecutionSinkClass(final Configuration configuration) {
		super(configuration);
	}

	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(
			name = ExecutionSinkClass.INPUT_PORT_NAME,
			eventTypes = { Execution.class })
	public void doJob(final Object data) {
		this.lst.add((Execution) data);
	}

	/**
	 * Delivers the list containing the received records.
	 * 
	 * @return The list with the records.
	 */
	public List<Execution> getExecutions() {
		return this.lst;
	}
}