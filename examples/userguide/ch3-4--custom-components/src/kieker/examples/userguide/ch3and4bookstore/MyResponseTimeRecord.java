/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.userguide.ch3and4bookstore;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Bits;

public class MyResponseTimeRecord extends AbstractMonitoringRecord
		implements IMonitoringRecord.Factory {

	private static final long serialVersionUID = 1775L;
	private static final Class<?>[] TYPES = { String.class, String.class, long.class };

	// Attributes storing the actual monitoring data:
	private final String className;
	private final String methodName;
	private final long responseTimeNanos;

	public MyResponseTimeRecord(final String clazz, final String method, final long rtNano) {
		this.className = clazz;
		this.methodName = method;
		this.responseTimeNanos = rtNano;
	}

	public MyResponseTimeRecord(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, MyResponseTimeRecord.TYPES);

		this.className = (String) values[0];
		this.methodName = (String) values[1];
		this.responseTimeNanos = (Long) values[2];
	}

	@Deprecated
	// Will not be used because the record implements IMonitoringRecord.Factory
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromByteArray(final byte[] values) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		return new Object[] { this.getClassName(), this.getMethodName(), this.getResponseTimeNanos() };
	}

	public byte[] toByteArray() {
		final byte[] arr = new byte[8 + 8 + 8];
		Bits.putString(arr, 0, this.getClassName());
		Bits.putString(arr, 8, this.getMethodName());
		Bits.putLong(arr, 8 + 8, this.getResponseTimeNanos());
		return arr;
	}

	public Class<?>[] getValueTypes() {
		return MyResponseTimeRecord.TYPES.clone();
	}

	public final String getClassName() {
		return this.className;
	}

	public final String getMethodName() {
		return this.methodName;
	}

	public final long getResponseTimeNanos() {
		return this.responseTimeNanos;
	}

}
