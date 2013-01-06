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

import android.os.SystemClock;
import android.util.Log;

public class FGHandler {

	/***
	 * Callback interface you can use when instantiating a Handler to avoid
	 * having to implement your own subclass of Handler.
	 */
	public interface Callback {
		public boolean handleMessage(FGMessage msg);
	}

	final FGMessageQueue mQueue;
	final Callback mCallback;
	final FGLoopThread mLooper;

	/***
	 * Default constructor associates this handler with the queue for the
	 * current thread.
	 * 
	 * If there isn't one, this handler won't be able to receive messages.
	 */
	public FGHandler() {

		mLooper = FGLoopThread.myLooper();
		if (mLooper == null) {
			throw new RuntimeException(
					"Can't create handler inside thread that has not called Looper.prepare()");
		}
		mQueue = mLooper.mQueue;
		mCallback = null;
	}

	/***
	 * Constructor associates this handler with the queue for the current thread
	 * and takes a callback interface in which you can handle messages.
	 */
	public FGHandler(Callback callback) {

		mLooper = FGLoopThread.myLooper();
		if (mLooper == null) {
			throw new RuntimeException(
					"Can't create handler inside thread that has not called Looper.prepare()");
		}
		mQueue = mLooper.mQueue;
		mCallback = callback;
	}

	/***
	 * Subclasses must implement this to receive messages.
	 */
	public void handleMessage(FGMessage msg) {
	}

	/***
	 * Handle system messages here.
	 */
	public void dispatchMessage(FGMessage msg) {
		if (msg.callback != null) {
			handleCallback(msg);
		} else {
			if (mCallback != null) {
				if (mCallback.handleMessage(msg)) {
					return;
				}
			}
			handleMessage(msg);
		}
	}

	/***
	 * Returns a new {@link android.os.Message Message} from the global message
	 * pool. More efficient than creating and allocating new instances. The
	 * retrieved message has its handler set to this instance (Message.target ==
	 * this). If you don't want that facility, just call Message.obtain()
	 * instead.
	 */
	public final FGMessage obtainMessage() {
		return FGMessage.obtain(this);
	}

	/***
	 * Same as {@link #obtainMessage()}, except that it also sets the what
	 * member of the returned Message.
	 * 
	 * @param what
	 *            Value to assign to the returned Message.what field.
	 * @return A Message from the global message pool.
	 */
	public final FGMessage obtainMessage(int what) {
		return FGMessage.obtain(this, what);
	}

	/***
	 * 
	 * Same as {@link #obtainMessage()}, except that it also sets the what and
	 * obj members of the returned Message.
	 * 
	 * @param what
	 *            Value to assign to the returned Message.what field.
	 * @param obj
	 *            Value to assign to the returned Message.obj field.
	 * @return A Message from the global message pool.
	 */
	public final FGMessage obtainMessage(int what, Object obj) {
		return FGMessage.obtain(this, what, obj);
	}

	/***
	 * 
	 * Same as {@link #obtainMessage()}, except that it also sets the what, arg1
	 * and arg2 members of the returned Message.
	 * 
	 * @param what
	 *            Value to assign to the returned Message.what field.
	 * @param arg1
	 *            Value to assign to the returned Message.arg1 field.
	 * @param arg2
	 *            Value to assign to the returned Message.arg2 field.
	 * @return A Message from the global message pool.
	 */
	public final FGMessage obtainMessage(int what, int arg1, int arg2) {
		return FGMessage.obtain(this, what, arg1, arg2);
	}

	/***
	 * 
	 * Same as {@link #obtainMessage()}, except that it also sets the what, obj,
	 * arg1,and arg2 values on the returned Message.
	 * 
	 * @param what
	 *            Value to assign to the returned Message.what field.
	 * @param arg1
	 *            Value to assign to the returned Message.arg1 field.
	 * @param arg2
	 *            Value to assign to the returned Message.arg2 field.
	 * @param obj
	 *            Value to assign to the returned Message.obj field.
	 * @return A Message from the global message pool.
	 */
	public final FGMessage obtainMessage(int what, int arg1, int arg2,
			Object obj) {
		return FGMessage.obtain(this, what, arg1, arg2, obj);
	}

	/***
	 * Causes the Runnable r to be added to the message queue. The runnable will
	 * be run on the thread to which this handler is attached.
	 * 
	 * @param r
	 *            The Runnable that will be executed.
	 * 
	 * @return Returns true if the Runnable was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */
	public final boolean post(Runnable r) {
		return sendMessageDelayed(getPostMessage(r), 0);
	}

	/***
	 * Causes the Runnable r to be added to the message queue, to be run at a
	 * specific time given by <var>uptimeMillis</var>. <b>The time-base is
	 * {@link android.os.SystemClock#uptimeMillis}.</b> The runnable will be run
	 * on the thread to which this handler is attached.
	 * 
	 * @param r
	 *            The Runnable that will be executed.
	 * @param uptimeMillis
	 *            The absolute time at which the callback should run, using the
	 *            {@link android.os.SystemClock#uptimeMillis} time-base.
	 * 
	 * @return Returns true if the Runnable was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting. Note that a
	 *         result of true does not mean the Runnable will be processed -- if
	 *         the looper is quit before the delivery time of the message occurs
	 *         then the message will be dropped.
	 */
	public final boolean postAtTime(Runnable r, long uptimeMillis) {
		return sendMessageAtTime(getPostMessage(r), uptimeMillis);
	}

	/***
	 * Causes the Runnable r to be added to the message queue, to be run at a
	 * specific time given by <var>uptimeMillis</var>. <b>The time-base is
	 * {@link android.os.SystemClock#uptimeMillis}.</b> The runnable will be run
	 * on the thread to which this handler is attached.
	 * 
	 * @param r
	 *            The Runnable that will be executed.
	 * @param uptimeMillis
	 *            The absolute time at which the callback should run, using the
	 *            {@link android.os.SystemClock#uptimeMillis} time-base.
	 * 
	 * @return Returns true if the Runnable was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting. Note that a
	 *         result of true does not mean the Runnable will be processed -- if
	 *         the looper is quit before the delivery time of the message occurs
	 *         then the message will be dropped.
	 * 
	 * @see android.os.SystemClock#uptimeMillis
	 */
	public final boolean postAtTime(Runnable r, Object token, long uptimeMillis) {
		return sendMessageAtTime(getPostMessage(r, token), uptimeMillis);
	}

	/***
	 * Causes the Runnable r to be added to the message queue, to be run after
	 * the specified amount of time elapses. The runnable will be run on the
	 * thread to which this handler is attached.
	 * 
	 * @param r
	 *            The Runnable that will be executed.
	 * @param delayMillis
	 *            The delay (in milliseconds) until the Runnable will be
	 *            executed.
	 * 
	 * @return Returns true if the Runnable was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting. Note that a
	 *         result of true does not mean the Runnable will be processed -- if
	 *         the looper is quit before the delivery time of the message occurs
	 *         then the message will be dropped.
	 */
	public final boolean postDelayed(Runnable r, long delayMillis) {
		return sendMessageDelayed(getPostMessage(r), delayMillis);
	}

	/***
	 * Posts a message to an object that implements Runnable. Causes the
	 * Runnable r to executed on the next iteration through the message queue.
	 * The runnable will be run on the thread to which this handler is attached.
	 * <b>This method is only for use in very special circumstances -- it can
	 * easily starve the message queue, cause ordering problems, or have other
	 * unexpected side-effects.</b>
	 * 
	 * @param r
	 *            The Runnable that will be executed.
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */
	public final boolean postAtFrontOfQueue(Runnable r) {
		return sendMessageAtFrontOfQueue(getPostMessage(r));
	}

	/***
	 * Remove any pending posts of Runnable r that are in the message queue.
	 */
	public final void removeCallbacks(Runnable r) {
		mQueue.removeMessages(this, r, null);
	}

	/***
	 * Remove any pending posts of Runnable <var>r</var> with Object
	 * <var>token</var> that are in the message queue.
	 */
	public final void removeCallbacks(Runnable r, Object token) {
		mQueue.removeMessages(this, r, token);
	}

	/***
	 * Pushes a message onto the end of the message queue after all pending
	 * messages before the current time. It will be received in
	 * {@link #handleMessage}, in the thread attached to this handler.
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */
	public final boolean sendMessage(FGMessage msg) {
		return sendMessageDelayed(msg, 0);
	}

	/***
	 * Sends a Message containing only the what value.
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */
	public final boolean sendEmptyMessage(int what) {
		return sendEmptyMessageDelayed(what, 0);
	}

	/***
	 * Sends a Message containing only the what value, to be delivered after the
	 * specified amount of time elapses.
	 * 
	 * @see #sendMessageDelayed(android.os.Message, long)
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */
	public final boolean sendEmptyMessageDelayed(int what, long delayMillis) {
		FGMessage msg = FGMessage.obtain();
		msg.what = what;
		return sendMessageDelayed(msg, delayMillis);
	}

	/***
	 * Sends a Message containing only the what value, to be delivered at a
	 * specific time.
	 * 
	 * @see #sendMessageAtTime(android.os.Message, long)
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */

	public final boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
		FGMessage msg = FGMessage.obtain();
		msg.what = what;
		return sendMessageAtTime(msg, uptimeMillis);
	}

	/***
	 * Enqueue a message into the message queue after all pending messages
	 * before (current time + delayMillis). You will receive it in
	 * {@link #handleMessage}, in the thread attached to this handler.
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting. Note that a
	 *         result of true does not mean the message will be processed -- if
	 *         the looper is quit before the delivery time of the message occurs
	 *         then the message will be dropped.
	 */
	public final boolean sendMessageDelayed(FGMessage msg, long delayMillis) {
		if (delayMillis < 0) {
			delayMillis = 0;
		}
		return sendMessageAtTime(msg, SystemClock.uptimeMillis() + delayMillis);
	}

	/***
	 * Enqueue a message into the message queue after all pending messages
	 * before the absolute time (in milliseconds) <var>uptimeMillis</var>.
	 * <b>The time-base is {@link android.os.SystemClock#uptimeMillis}.</b> You
	 * will receive it in {@link #handleMessage}, in the thread attached to this
	 * handler.
	 * 
	 * @param uptimeMillis
	 *            The absolute time at which the message should be delivered,
	 *            using the {@link android.os.SystemClock#uptimeMillis}
	 *            time-base.
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting. Note that a
	 *         result of true does not mean the message will be processed -- if
	 *         the looper is quit before the delivery time of the message occurs
	 *         then the message will be dropped.
	 */
	public boolean sendMessageAtTime(FGMessage msg, long uptimeMillis) {
		boolean sent = false;
		FGMessageQueue queue = mQueue;
		if (queue != null) {
			msg.target = this;
			sent = queue.enqueueMessage(msg, uptimeMillis);
		} else {
			RuntimeException e = new RuntimeException(this
					+ " sendMessageAtTime() called with no mQueue");
			Log.w("Looper", e.getMessage(), e);
		}
		return sent;
	}

	/***
	 * Enqueue a message at the front of the message queue, to be processed on
	 * the next iteration of the message loop. You will receive it in
	 * {@link #handleMessage}, in the thread attached to this handler. <b>This
	 * method is only for use in very special circumstances -- it can easily
	 * starve the message queue, cause ordering problems, or have other
	 * unexpected side-effects.</b>
	 * 
	 * @return Returns true if the message was successfully placed in to the
	 *         message queue. Returns false on failure, usually because the
	 *         looper processing the message queue is exiting.
	 */
	public final boolean sendMessageAtFrontOfQueue(FGMessage msg) {
		boolean sent = false;
		FGMessageQueue queue = mQueue;
		if (queue != null) {
			msg.target = this;
			sent = queue.enqueueMessage(msg, 0);
		} else {
			RuntimeException e = new RuntimeException(this
					+ " sendMessageAtTime() called with no mQueue");
			Log.w("Looper", e.getMessage(), e);
		}
		return sent;
	}

	/***
	 * Remove any pending posts of messages with code 'what' that are in the
	 * message queue.
	 */
	public final void removeMessages(int what) {
		mQueue.removeMessages(this, what, null, true);
	}

	/***
	 * Remove any pending posts of messages with code 'what' and whose obj is
	 * 'object' that are in the message queue.
	 */
	public final void removeMessages(int what, Object object) {
		mQueue.removeMessages(this, what, object, true);
	}

	/***
	 * Remove any pending posts of callbacks and sent messages whose
	 * <var>obj</var> is <var>token</var>.
	 */
	public final void removeCallbacksAndMessages(Object token) {
		mQueue.removeCallbacksAndMessages(this, token);
	}

	/***
	 * Check if there are any pending posts of messages with code 'what' in the
	 * message queue.
	 */
	public final boolean hasMessages(int what) {
		return mQueue.removeMessages(this, what, null, false);
	}

	/***
	 * Check if there are any pending posts of messages with code 'what' and
	 * whose obj is 'object' in the message queue.
	 */
	public final boolean hasMessages(int what, Object object) {
		return mQueue.removeMessages(this, what, object, false);
	}

	private final FGMessage getPostMessage(Runnable r) {
		FGMessage m = FGMessage.obtain();
		m.callback = r;
		return m;
	}

	private final FGMessage getPostMessage(Runnable r, Object token) {
		FGMessage m = FGMessage.obtain();
		m.obj = token;
		m.callback = r;
		return m;
	}

	private final void handleCallback(FGMessage message) {
		message.callback.run();
	}

	@Override
	public String toString() {
		return "Handler{" + Integer.toHexString(System.identityHashCode(this))
				+ "}";
	}
}
