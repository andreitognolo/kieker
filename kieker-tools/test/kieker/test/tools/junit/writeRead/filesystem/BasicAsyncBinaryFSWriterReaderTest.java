/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.junit.writeRead.filesystem;

import kieker.common.configuration.Configuration;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.filesystem.AsyncBinaryFsWriter;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.5
 */
public class BasicAsyncBinaryFSWriterReaderTest extends AbstractTestFSWriterReader { // NOPMD (TestClassWithoutTestCases) // NOCS (MissingCtorCheck)

	@Override
	protected Class<? extends IMonitoringWriter> getTestedWriterClazz() {
		return AsyncBinaryFsWriter.class;
	}

	@Override
	protected void refineWriterConfiguration(final Configuration config, final int numRecordsWritten) {
		// not needed
	}

	@Override
	protected boolean terminateBeforeLogInspection() {
		return true; // because the AsyncBinaryFsWriter does not flush
	}

	@Override
	protected void doSomethingBeforeReading(final String[] monitoringLogs) {
		// we'll keep the log untouched
	}

	@Override
	protected void refineFSReaderConfiguration(final Configuration config) {
		// no need to refine
	}
}
