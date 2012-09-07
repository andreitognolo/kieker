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
package kieker.test.monitoring.junit.core.controller;

import org.junit.Assert;
import org.junit.Test;

/**
 * This test is now included in {@link kieker.test.tools.junit.writeRead.jmx.BasicJMXWriterReaderTest}
 * 
 * Otherwise jUnit gets Problems with two open JMX servers.
 * 
 * @author Jan Waller
 */
public final class TestJMXInterface { // NOPMD NOCS

	@Test
	public void testIt() {
		Assert.assertTrue(true); // NOPMD // NOCS // NOGARNIX, tested elsewhere
	}
}