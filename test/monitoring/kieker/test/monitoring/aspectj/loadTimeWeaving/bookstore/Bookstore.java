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

package kieker.test.monitoring.aspectj.loadTimeWeaving.bookstore;

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
	private static final int NUM_REQUESTS = 1000;
	private static final int INTER_REQUEST_TIME = 5;

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
	 * 
	 * This will be monitored by Kieker, since it has the
	 * OperationExecutionMonitoringProbe annotation.
	 */
	@OperationExecutionMonitoringProbe
	public static void main(final String[] args) {
		for (int i = 0; i < Bookstore.NUM_REQUESTS; i++) {
			System.out.println("Bookstore.main: Starting request " + i);
			final Bookstore newBookstore = new Bookstore();
			newBookstore.start();
			Bookstore.waitabit(Bookstore.INTER_REQUEST_TIME);
		}
		System.out.println("Bookstore.main: Finished with starting all requests.");
		System.out.println("Bookstore.main: Waiting 5 secs before calling system.exit");
		Bookstore.waitabit(5000); // NOCS (MagicNumber)
		System.exit(0);
	}

	@Override
	public void run() {
		Bookstore.searchBook();
	}

	@OperationExecutionMonitoringProbe
	public static void searchBook() {
		Catalog.getBook(false);
		CRM.getOffers();
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