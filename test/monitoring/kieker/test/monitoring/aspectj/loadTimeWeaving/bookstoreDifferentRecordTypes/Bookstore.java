/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.monitoring.aspectj.loadTimeWeaving.bookstoreDifferentRecordTypes;

import java.util.ArrayList;
import java.util.List;

import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;

/**
 * A simple test and demonstration scenario for Kieker's
 * monitoring component.
 * 
 * @author Matthias Rohr
 *         History:
 *         2008/01/09: Refactoring for the first release of
 *         Kieker and publication under an open source licence
 *         2007-04-18: Initial version
 * 
 */
public class Bookstore extends Thread {

	public static final List<Bookstore> BOOKSTORE_SCENARIOS = new ArrayList<Bookstore>();

	private static final int NUM_REQUESTS = 3;
	private static final int INTER_REQUEST_TIME = 1000;

	public Bookstore() {
		// nothing to do
	}

	/**
	 * 
	 * main is the load driver for the Bookstore. It creates
	 * request which all request a search from the bookstore.
	 * A fixed time delay is between two request. Requests
	 * are likely to overlap, which leads to request processing
	 * in more than one thread.
	 * 
	 * Both the number of requests and arrival rate are defined
	 * by the local variables above the method.
	 * (default: 100 requests; interRequestTime 5 (millisecs))
	 */
	public static void main(final String[] args) throws InterruptedException {

		for (int i = 0; i < Bookstore.NUM_REQUESTS; i++) {
			System.out.println("Bookstore.main: Starting request " + i);
			final Bookstore newBookstore = new Bookstore();
			synchronized (Bookstore.BOOKSTORE_SCENARIOS) {
				Bookstore.BOOKSTORE_SCENARIOS.add(newBookstore);
			}
			newBookstore.start();
			Bookstore.waitabit(Bookstore.INTER_REQUEST_TIME);
		}
		System.out.println("Bookstore.main: Finished with starting all requests.");
		System.out.println("Bookstore.main: Waiting for threads to terminate");
		synchronized (Bookstore.BOOKSTORE_SCENARIOS) {
			while (!Bookstore.BOOKSTORE_SCENARIOS.isEmpty()) {
				Bookstore.BOOKSTORE_SCENARIOS.wait();
			}
		}
	}

	@Override
	public void run() {
		Bookstore.searchBook();
		synchronized (Bookstore.BOOKSTORE_SCENARIOS) {
			Bookstore.BOOKSTORE_SCENARIOS.remove(this);
			Bookstore.BOOKSTORE_SCENARIOS.notifyAll();
		}
	}

	@OperationExecutionMonitoringProbe
	public static void searchBook() {
		for (int i = 0; i < 1; i++) {
			Catalog.getBook(false);
			CRM.getOffers();
		}
	}

	/**
	 * Only encapsulates Thread.sleep()
	 */
	public static void waitabit(final long waittime) {
		if (waittime > 0) {
			try {
				Thread.sleep(waittime);
			} catch (final InterruptedException e) {
			}
		}
	}
}