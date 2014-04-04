/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis;

import java.util.List;

import kieker.panalysis.base.IPipe;
import kieker.panalysis.base.ISink;
import kieker.panalysis.base.ISource;
import kieker.panalysis.base.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 * 
 * @param T
 */
public class MethodCallPipe<T> implements IPipe<T> {

	private final IStage<?> targetStage;
	private T storedRecord;

	public MethodCallPipe(final IStage<?> targetStage) {
		this.targetStage = targetStage;
	}

	public void put(final T record) {
		this.storedRecord = record;
		this.targetStage.execute();
	}

	public T take() {
		final T temp = this.storedRecord;
		this.storedRecord = null;
		return temp;
	}

	public T tryTake() {
		return this.take();
	}

	public boolean isEmpty() {
		return this.storedRecord == null;
	}

	public static <O extends Enum<O>, I extends Enum<I>> void connect(final ISource<O> sourceStage, final O sourcePort, final ISink<I> targetStage,
			final I targetPort) {
		final IPipe<Object> pipe = new MethodCallPipe<Object>(targetStage);
		sourceStage.setPipeForOutputPort(sourcePort, pipe);
		targetStage.setPipeForInputPort(targetPort, pipe);
	}

	public List<T> tryTakeMultiple(final int numItemsToTake) {
		throw new IllegalStateException("Taking more than one element is not possible. You tried to take " + numItemsToTake + " items.");
	}

	public void putMultiple(final List<T> items) {
		throw new IllegalStateException("Putting more than one element is not possible. You tried to put " + items.size() + " items.");
	}

}
