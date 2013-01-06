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
import android.util.AndroidRuntimeException;
import android.util.Log;

public class FGMessageQueue {

	FGMessage mMessages;
	boolean mQuiting = false;

	final boolean enqueueMessage(FGMessage msg, long when) {
		if (msg.when != 0) {
			throw new AndroidRuntimeException(msg
					+ " This message is already in use.");
		}
		// if (msg.target == null && !mQuitAllowed) {
		// throw new RuntimeException("Main thread not allowed to quit");
		// }
		synchronized (this) {
			if (mQuiting) {
				RuntimeException e = new RuntimeException(msg.target
						+ " sending message to a Handler on a dead thread");
				Log.w("MessageQueue", e.getMessage(), e);
				return false;
			} else if (msg.target == null) {
				mQuiting = true;
			}

			msg.when = when;
			// Log.d("MessageQueue", "Enqueing: " + msg);
			FGMessage p = mMessages;
			if (p == null || when == 0 || when < p.when) {
				msg.next = p;
				mMessages = msg;
				this.notify();
			} else {
				FGMessage prev = null;
				while (p != null && p.when <= when) {
					prev = p;
					p = p.next;
				}
				msg.next = prev.next;
				prev.next = msg;
				this.notify();
			}
		}
		return true;
	}

	final boolean removeMessages(FGHandler h, int what, Object object,
			boolean doRemove) {
		synchronized (this) {
			FGMessage p = mMessages;
			boolean found = false;

			// Remove all messages at front.
			while (p != null && p.target == h && p.what == what
					&& (object == null || p.obj == object)) {
				if (!doRemove)
					return true;
				found = true;
				FGMessage n = p.next;
				mMessages = n;
				p.recycle();
				p = n;
			}

			// Remove all messages after front.
			while (p != null) {
				FGMessage n = p.next;
				if (n != null) {
					if (n.target == h && n.what == what
							&& (object == null || n.obj == object)) {
						if (!doRemove)
							return true;
						found = true;
						FGMessage nn = n.next;
						n.recycle();
						p.next = nn;
						continue;
					}
				}
				p = n;
			}

			return found;
		}
	}

	final void removeMessages(FGHandler h, Runnable r, Object object) {
		if (r == null) {
			return;
		}

		synchronized (this) {
			FGMessage p = mMessages;

			// Remove all messages at front.
			while (p != null && p.target == h && p.callback == r
					&& (object == null || p.obj == object)) {
				FGMessage n = p.next;
				mMessages = n;
				p.recycle();
				p = n;
			}

			// Remove all messages after front.
			while (p != null) {
				FGMessage n = p.next;
				if (n != null) {
					if (n.target == h && n.callback == r
							&& (object == null || n.obj == object)) {
						FGMessage nn = n.next;
						n.recycle();
						p.next = nn;
						continue;
					}
				}
				p = n;
			}
		}
	}

	final void removeCallbacksAndMessages(FGHandler h, Object object) {
		synchronized (this) {
			FGMessage p = mMessages;

			// Remove all messages at front.
			while (p != null && p.target == h
					&& (object == null || p.obj == object)) {
				FGMessage n = p.next;
				mMessages = n;
				p.recycle();
				p = n;
			}

			// Remove all messages after front.
			while (p != null) {
				FGMessage n = p.next;
				if (n != null) {
					if (n.target == h && (object == null || n.obj == object)) {
						FGMessage nn = n.next;
						n.recycle();
						p.next = nn;
						continue;
					}
				}
				p = n;
			}
		}
	}

	final FGMessage next() {

		synchronized (this) {
			FGMessage msg = mMessages;
			if (msg != null) {
				if (SystemClock.uptimeMillis() >= msg.when) {
					mMessages = msg.next;
					return msg;
				}
			}
		}

		return null;
	}

}
