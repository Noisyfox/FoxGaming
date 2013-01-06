/**
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * This is a modified version of Message from android-2.2-froyo
 */
package org.foxteam.noisyfox.FoxGaming.Core.LoopThread;

import android.os.Process;

public abstract class FGLoopThread extends Thread {

	// sThreadLocal.get() will return null unless this is a LoopThread.
	private static final ThreadLocal<FGLoopThread> sThreadLocal = new ThreadLocal<FGLoopThread>();
	final FGMessageQueue mQueue;

	private boolean looping = false;
	private int mPriority;
	private int mTid = -1;

	/***
	 * Initialize the current thread as a looper. This gives you a chance to
	 * create handlers that then reference this looper, before actually starting
	 * the loop. Be sure to call {@link #loop()} after calling this method, and
	 * end it by calling {@link #quit()}.
	 */
	private final void prepare() {
		if (sThreadLocal.get() != null) {
			throw new RuntimeException(
					"Only one Looper may be created per thread");
		}
		sThreadLocal.set(this);
	}

	/***
	 * Return the Looper object associated with the current thread. Returns null
	 * if the calling thread is not associated with a Looper.
	 */
	public static final FGLoopThread myLooper() {
		return sThreadLocal.get();
	}

	/***
	 * Return the {@link MessageQueue} object associated with the current
	 * thread. This must be called from a thread running a Looper, or a
	 * NullPointerException will be thrown.
	 */
	public static final FGMessageQueue myQueue() {
		return myLooper().mQueue;
	}

	public FGLoopThread() {
		mQueue = new FGMessageQueue();
	}

	public FGLoopThread(String name) {
		super(name);
		mQueue = new FGMessageQueue();
		mPriority = Process.THREAD_PRIORITY_DEFAULT;
	}

	/***
	 * Constructs a FGLoopThread.
	 * 
	 * @param name
	 * @param priority
	 *            The priority to run the thread at. The value supplied must be
	 *            from {@link android.os.Process} and not from java.lang.Thread.
	 */
	public FGLoopThread(String name, int priority) {
		super(name);
		mQueue = new FGMessageQueue();
		mPriority = priority;
	}

	@Override
	public final void run() {
		looping = true;
		mTid = Process.myTid();
		Process.setThreadPriority(mPriority);
		prepare();

		onLooperPrepared();
		while (looping) {
			processMessage();
			if (looping)
				loop();
		}
		onLooperExited();
		mTid = -1;

	}

	/***
	 * Run the message queue in this thread. Be sure to call {@link #quit()} to
	 * end the loop.
	 */
	public final void processMessage() {
		FGMessage msg;
		while ((msg = mQueue.next()) != null) {
			// if (!me.mRun) {
			// break;
			// }
			if (msg != null) {
				if (msg.target == null) {
					// No target is a magic identifier for the quit message.
					looping = false;
					return;
				}
				msg.target.dispatchMessage(msg);
				msg.recycle();
			}
		}
	}

	protected void loop() {

	}

	/***
	 * Call back method that can be explicitly over ridden if needed to execute
	 * some setup before Looper loops.
	 */
	protected void onLooperPrepared() {
	}

	/***
	 * Call back method that can be explicitly over ridden if needed to execute
	 * some clean up after Looper loops.
	 */
	protected void onLooperExited() {
	}

	public final void exit() {
		FGMessage msg = FGMessage.obtain();
		// NOTE: By enqueueing directly into the message queue, the
		// message is left with a null target. This is how we know it is
		// a quit message.
		mQueue.enqueueMessage(msg, 0);
	}

	/***
	 * Returns the identifier of this thread. See Process.myTid().
	 */
	public int getThreadId() {
		return mTid;
	}

}
